package de.oglimmer.cyc.web.actions;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.ProtocolException;
import java.net.URL;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.util.Date;
import java.util.List;
import java.util.concurrent.TimeUnit;

import javax.crypto.Mac;
import javax.crypto.spec.SecretKeySpec;

import lombok.Value;
import lombok.extern.slf4j.Slf4j;
import net.sourceforge.stripes.action.DefaultHandler;
import net.sourceforge.stripes.action.RedirectResolution;
import net.sourceforge.stripes.action.Resolution;

import org.apache.commons.codec.binary.Base64;
import org.json.JSONException;
import org.json.JSONObject;
import org.json.JSONTokener;

import de.oglimmer.cyc.dao.UserDao;
import de.oglimmer.cyc.dao.couchdb.CouchDbUtil;
import de.oglimmer.cyc.dao.couchdb.UserCouchDb;
import de.oglimmer.cyc.model.User;
import de.oglimmer.cyc.web.CyrProperties;
import de.oglimmer.cyc.web.DoesNotRequireLogin;
import de.oglimmer.cyc.web.exception.FBException;

@Slf4j
@DoesNotRequireLogin
public class FBLoginActionBean extends BaseAction {

	private static final String GRAPH_FACEBOOK_ME_URL = "https://graph.facebook.com/me?access_token=";
	private static final String HASH_ALGO = "HmacSHA256";

	private UserDao userDao = new UserCouchDb(CouchDbUtil.getDatabase());

	@DefaultHandler
	public Resolution show() {
		try {
			JSONObject jsonUrlParameter = getJsonUrlParameter();
			log.debug("jsonUrlParameter={}", jsonUrlParameter.toString(2));

			String signedRequest = jsonUrlParameter.getString("signedRequest");

			ParameterData paramData = new ParameterData(signedRequest);
			log.debug("paramData={}", paramData);

			validateResponse(paramData);

			String userId = paramData.getPayloadAsJson().getString("user_id");
			List<User> userList = userDao.findByFBUserId(userId);
			if (userList.size() == 1) {
				User user = userList.get(0);
				loginUser(user);
			} else {
				createNewFBUser(jsonUrlParameter, paramData);
			}
		} catch (JSONException | IOException | InvalidKeyException | NoSuchAlgorithmException e) {
			log.error("show failed", e);
		}

		return new RedirectResolution(PortalActionBean.class);
	}

	private void loginUser(User user) {
		getContext().getRequest().getSession(true).setAttribute("userid", user.getId());
		user.setLastLoginDate(new Date());
		userDao.update(user);
	}

	private void createNewFBUser(JSONObject jsonUrlParameter, ParameterData paramData) throws JSONException,
			MalformedURLException, IOException, ProtocolException {
		JSONObject user = getUserFromFB(jsonUrlParameter.getString("accessToken"));
		log.debug("user={}", user.toString(2));

		validateUser(jsonUrlParameter, paramData, user);

		String email = user.getString("email");
		String name = user.getString("name");
		String firstName = user.getString("first_name");
		String lastName = user.getString("last_name");
		String facebookId = user.getString("id");

		User newUser = new User(name, firstName, lastName, facebookId, email);
		newUser.setMainJavaScript(RegisterActionBean.DEFAULT_CODE);
		newUser.setCreatedDate(new Date());
		userDao.add(newUser);
	}

	private void validateUser(JSONObject jsonUrlParameter, ParameterData paramData, JSONObject user)
			throws JSONException {
		String unsecUserId = jsonUrlParameter.getString("userID");
		String securedUserId = paramData.getPayloadAsJson().getString("user_id");
		String graphApiUserId = user.getString("id");
		if (!unsecUserId.equals(securedUserId) || !securedUserId.equals(graphApiUserId)) {
			throw new FBException("Validate user failed. " + unsecUserId + "≠" + unsecUserId + "≠" + graphApiUserId);
		}
	}

	private JSONObject byteToJson(byte[] payload) throws JSONException {
		StringBuilder buff = new StringBuilder();
		for (byte b : payload) {
			buff.append((char) b);
		}

		return new JSONObject(new JSONTokener(buff.toString()));
	}

	private void validateResponse(ParameterData paramData) throws NoSuchAlgorithmException, InvalidKeyException,
			JSONException {
		validateSignature(paramData);
		validateIssuedAt(paramData);
	}

	private void validateIssuedAt(ParameterData paramData) throws JSONException {
		Date now = new Date();
		Date issuedAt = new Date(paramData.getPayloadAsJson().getInt("issued_at") * 1000);

		long maxDuration = TimeUnit.MILLISECONDS.convert(15, TimeUnit.MINUTES);

		long duration = now.getTime() - issuedAt.getTime();

		if (duration >= maxDuration) {
			throw new FBException("Issued_at too old " + issuedAt);
		}
	}

	private void validateSignature(ParameterData paramData) throws NoSuchAlgorithmException, InvalidKeyException {
		Mac mac = Mac.getInstance(HASH_ALGO);
		SecretKeySpec secret = new SecretKeySpec(CyrProperties.INSTANCE.getFbSecret().getBytes(), HASH_ALGO);
		mac.init(secret);
		byte[] digest = mac.doFinal(paramData.getEncodedPayload().getBytes());

		String digHex = convertToHex(digest);
		String sigHex = convertToHex(paramData.getSig());

		if (!sigHex.equals(digHex)) {
			throw new FBException("Sig failed. " + digHex + "≠" + sigHex);
		}
	}

	private String convertToHex(byte[] byteArray) {
		StringBuilder buff = new StringBuilder();
		for (byte by : byteArray) {
			buff.append(String.format("%02x", by));
		}
		return buff.toString();
	}

	private JSONObject getUserFromFB(String accessToken) throws JSONException, MalformedURLException, IOException,
			ProtocolException {
		String url = GRAPH_FACEBOOK_ME_URL + accessToken;
		URL obj = new URL(url);
		HttpURLConnection con = (HttpURLConnection) obj.openConnection();
		con.setRequestMethod("GET");
		StringBuilder respBuff = new StringBuilder();
		try (BufferedReader in = new BufferedReader(new InputStreamReader(con.getInputStream()))) {
			String inputLine;
			while ((inputLine = in.readLine()) != null) {
				respBuff.append(inputLine);
			}
		}
		return new JSONObject(new JSONTokener(respBuff.toString()));
	}

	private JSONObject getJsonUrlParameter() throws JSONException {
		String urlParameter = getContext().getRequest().getParameter("data");
		return new JSONObject(new JSONTokener(urlParameter));
	}

	@Value
	private class ParameterData {

		private JSONObject payloadAsJson;
		private String encodedPayload;
		private byte[] sig;

		public ParameterData(String signedRequest) throws JSONException {
			String[] splittedSignedReq = signedRequest.split("\\.");
			String encodedSig = splittedSignedReq[0];

			this.encodedPayload = splittedSignedReq[1];
			this.sig = encode(encodedSig);
			byte[] payload = encode(encodedPayload);
			this.payloadAsJson = byteToJson(payload);
		}

		private byte[] encode(String str) {
			return Base64.decodeBase64(str.replaceAll("-", "+").replaceAll("_", "/"));
		}

		@Override
		public String toString() {
			return payloadAsJson.toString();
		}
	}

}
