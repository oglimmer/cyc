package de.oglimmer.cyc.web.action;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.ProtocolException;
import java.net.URL;
import java.util.Date;
import java.util.List;

import lombok.extern.slf4j.Slf4j;
import net.sourceforge.stripes.action.DefaultHandler;
import net.sourceforge.stripes.action.RedirectResolution;
import net.sourceforge.stripes.action.Resolution;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.json.JSONTokener;

import de.oglimmer.cyc.dao.UserDao;
import de.oglimmer.cyc.dao.couchdb.CouchDbUtil;
import de.oglimmer.cyc.dao.couchdb.UserCouchDb;
import de.oglimmer.cyc.model.User;
import de.oglimmer.cyc.util.DefaultCode;
import de.oglimmer.cyc.web.DoesNotRequireLogin;

@Slf4j
@DoesNotRequireLogin
public class GoogleLoginActionBean extends BaseAction {

	private static final String GOOGLE_ME_URL = " https://www.googleapis.com/plus/v1/people/me?access_token=";

	private UserDao userDao = new UserCouchDb(CouchDbUtil.getDatabase());

	@DefaultHandler
	public Resolution show() {
		try {
			String accessToken = getAccessTokenParameter();
			log.debug("accessToken={}", accessToken);

			JSONObject userGoogle = getUserFromGoogle(accessToken);
			log.debug("userGoogle={}", userGoogle);

			String gameUserId;
			List<User> userList = userDao.findByGoogleUserId(userGoogle.getString("id"));
			if (userList.size() == 1) {
				User user = userList.get(0);
				gameUserId = loginUser(user);
			} else {
				gameUserId = createNewFBUser(userGoogle);
			}
			getContext().getRequest().getSession(true).setAttribute("userid", gameUserId);
		} catch (JSONException | IOException e) {
			log.error("show failed", e);
		}

		return new RedirectResolution(PortalActionBean.class);
	}

	private String loginUser(User user) {
		user.setLastLoginDate(new Date());
		userDao.update(user);
		return user.getId();
	}

	private String createNewFBUser(JSONObject jsonUrlParameter) throws JSONException, MalformedURLException,
			IOException, ProtocolException {

		String name = jsonUrlParameter.getString("displayName");
		String firstName = jsonUrlParameter.getJSONObject("name").getString("givenName");
		String lastName = jsonUrlParameter.getJSONObject("name").getString("familyName");
		String googleId = jsonUrlParameter.getString("id");

		String firstEmail = null;
		JSONArray emails = jsonUrlParameter.getJSONArray("emails");
		for (int i = 0; i < emails.length(); i++) {
			JSONObject emailObj = emails.getJSONObject(i);
			String email = emailObj.getString("value");
			if (firstEmail == null) {
				firstEmail = email;
			}
			List<User> users = userDao.findByEmail(email.toLowerCase());
			if (users.size() == 1) {
				User userFromDb = users.get(0);
				userFromDb.setFacebookId(googleId);
				userFromDb.setFirstName(firstName);
				userFromDb.setLastName(lastName);
				userDao.update(userFromDb);
				return userFromDb.getId();
			}
		}

		User newUser = new User(name, firstName, lastName, googleId, firstEmail, true);
		newUser.setMainJavaScript(DefaultCode.INSTANCE.getDefaultCode());
		newUser.setCreatedDate(new Date());
		userDao.add(newUser);
		return newUser.getId();
	}

	private JSONObject getUserFromGoogle(String accessToken) throws JSONException, MalformedURLException, IOException,
			ProtocolException {
		String url = GOOGLE_ME_URL + accessToken;
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

	private String getAccessTokenParameter() throws JSONException {
		return getContext().getRequest().getParameter("data");
	}

}
