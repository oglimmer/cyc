package de.oglimmer.cyc.web.action;

import de.oglimmer.cyc.dao.UserDao;
import de.oglimmer.cyc.dao.couchdb.CouchDbUtil;
import de.oglimmer.cyc.dao.couchdb.UserCouchDb;
import de.oglimmer.cyc.web.DoesNotRequireLogin;
import de.oglimmer.cyc.web.WebContainerProperties;
import lombok.Getter;
import lombok.extern.slf4j.Slf4j;
import net.sourceforge.stripes.action.DefaultHandler;
import net.sourceforge.stripes.action.RedirectResolution;
import net.sourceforge.stripes.action.Resolution;

import java.util.HashSet;
import java.util.Set;

@Slf4j
@DoesNotRequireLogin
public class DiscordLoginActionBean extends BaseAction {

    @Getter
    private static Set<String> states = new HashSet<>();

    private static final String AUTHORIZE_URL = "https://discord.com/oauth2/authorize?";
    //    public static final String REDIRECT_URL = "http%3A%2F%2Flocalhost:8080%2Fcyc%2FDiscordUserDoor.action";
    public static final String REDIRECT_URL = "https%3A%2F%2F" + WebContainerProperties.INSTANCE.getHttpsDomain() + "%2FDiscordUserDoor.action";

    private UserDao userDao = new UserCouchDb(CouchDbUtil.INSTANCE.getDatabase());

    @DefaultHandler
    public Resolution show() {
        String url = AUTHORIZE_URL;

        String randomState = "" + Math.random();
        states.add(randomState);

        url += "response_type=code&"
                + "client_id=" + WebContainerProperties.INSTANCE.getDiscordClientId() + "&"
                + "scope=identify&"
                + "state=" + randomState + "&"
                + "redirect_uri=" + REDIRECT_URL + "&"
                + "prompt=none";

        return new RedirectResolution(url, false);
    }


}
