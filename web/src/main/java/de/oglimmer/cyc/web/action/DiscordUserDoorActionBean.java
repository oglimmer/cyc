package de.oglimmer.cyc.web.action;

import de.oglimmer.cyc.dao.UserDao;
import de.oglimmer.cyc.dao.couchdb.CouchDbUtil;
import de.oglimmer.cyc.dao.couchdb.UserCouchDb;
import de.oglimmer.cyc.model.User;
import de.oglimmer.cyc.util.DefaultCode;
import de.oglimmer.cyc.web.DoesNotRequireLogin;
import de.oglimmer.cyc.web.WebContainerProperties;
import lombok.extern.slf4j.Slf4j;
import net.sourceforge.stripes.action.DefaultHandler;
import net.sourceforge.stripes.action.RedirectResolution;
import net.sourceforge.stripes.action.Resolution;
import okhttp3.*;
import org.jetbrains.annotations.NotNull;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.Date;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import static de.oglimmer.cyc.web.action.DiscordLoginActionBean.REDIRECT_URL;

@Slf4j
@DoesNotRequireLogin
public class DiscordUserDoorActionBean extends BaseAction {

    private Set<String> states = new HashSet<>();

    private static final String TOKEN_URL = "https://discord.com/api/oauth2/token";
    private static final String USERS_ME_URL = "https://discord.com/api/users/@me";
    ;

    private UserDao userDao = new UserCouchDb(CouchDbUtil.INSTANCE.getDatabase());

    @DefaultHandler
    public Resolution show() throws JSONException {
        validateState();
        OkHttpClient client = new OkHttpClient();
        String accessToken = getAccessToken(client);
        JSONObject jsonUser = getUserDataFromDiscordApi(client, accessToken);
        String gameUserId = createUserOrLogin(jsonUser);
        getContext().getRequest().getSession(true).setAttribute("userid", gameUserId);
        return new RedirectResolution(PortalActionBean.class);
    }

    private void validateState() {
        if (!DiscordLoginActionBean.getStates().contains(getStateParameter())) {
            throw new RuntimeException("failed to validate state");
        }
        DiscordLoginActionBean.getStates().remove(getStateParameter());
    }

    @NotNull
    private String getAccessToken(OkHttpClient client) throws JSONException {
        String wwwFormUrlencoded = "client_id=" + WebContainerProperties.INSTANCE.getDiscordClientId()
                + "&client_secret=" + WebContainerProperties.INSTANCE.getDiscordSecret()
                + "&grant_type=authorization_code"
                + "&code=" + getCodeParameter()
                + "&redirect_uri=" + REDIRECT_URL;


        RequestBody body = RequestBody.create(wwwFormUrlencoded, MediaType.get("application/x-www-form-urlencoded"));
        Request request = new Request.Builder()
                .url(TOKEN_URL)
                .post(body)
                .build();
        try (Response response = client.newCall(request).execute()) {
            String responseBody = response.body().string();
            JSONObject jsonTokenResponse = new JSONObject(responseBody);
            return jsonTokenResponse.get("access_token").toString();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    private JSONObject getUserDataFromDiscordApi(OkHttpClient client, String accessToken) throws JSONException {
        Request requestUsersMe = new Request.Builder()
                .url(USERS_ME_URL)
                .addHeader("Authorization", "Bearer " + accessToken)
                .build();

        try (Response responseUsersMe = client.newCall(requestUsersMe).execute()) {
            return new JSONObject(responseUsersMe.body().string());
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    private String createUserOrLogin(JSONObject jsonUser) throws JSONException {
        String gameUserId;
        List<User> userList = userDao.findByDiscordUserId(jsonUser.getString("id"));
        if (userList.size() == 1) {
            User user = userList.get(0);
            gameUserId = loginUser(user);
        } else {
            gameUserId = createDiscordUser(jsonUser);
        }
        return gameUserId;
    }

    private String getCodeParameter() {
        return getContext().getRequest().getParameter("code");
    }

    private String getStateParameter() {
        return getContext().getRequest().getParameter("state");
    }

    private String loginUser(User user) {
        user.setLastLoginDate(new Date());
        userDao.update(user);
        return user.getId();
    }

    private String createDiscordUser(JSONObject jsonUrlParameter) throws JSONException {

        String name = jsonUrlParameter.getString("username") + "#" + jsonUrlParameter.getString("discriminator");
        String discordId = jsonUrlParameter.getString("id");

        String fakeEmail = (name + "@discord.fake").toLowerCase();
        List<User> users = userDao.findByEmail(fakeEmail);
        if (users.size() == 1) {
            User userFromDb = users.get(0);
            userFromDb.setDiscordId(discordId);
            userDao.update(userFromDb);
            return userFromDb.getId();
        }

        User newUser = new User(name, discordId, fakeEmail, false);
        newUser.setMainJavaScript(DefaultCode.INSTANCE.getDefaultCode());
        newUser.setCreatedDate(new Date());
        userDao.add(newUser);
        return newUser.getId();
    }

}
