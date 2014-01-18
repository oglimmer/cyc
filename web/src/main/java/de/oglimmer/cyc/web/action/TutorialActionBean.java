package de.oglimmer.cyc.web.action;

import java.util.Date;

import lombok.Getter;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;
import net.sourceforge.stripes.action.DefaultHandler;
import net.sourceforge.stripes.action.ForwardResolution;
import net.sourceforge.stripes.action.RedirectResolution;
import net.sourceforge.stripes.action.Resolution;
import de.oglimmer.cyc.dao.UserDao;
import de.oglimmer.cyc.dao.couchdb.CouchDbUtil;
import de.oglimmer.cyc.dao.couchdb.UserCouchDb;
import de.oglimmer.cyc.model.User;
import de.oglimmer.cyc.web.DoesNotRequireLogin;
import de.oglimmer.cyc.web.ext.SecurityInterceptor;

@Slf4j
@DoesNotRequireLogin
public class TutorialActionBean extends BaseAction {

	private static final String VIEW = "/WEB-INF/jsp/tutorial.jsp";

	private UserDao userDao = new UserCouchDb(CouchDbUtil.getDatabase());

	@Getter
	@Setter
	private String output;

	public boolean isLoggedIn() {
		return SecurityInterceptor.isLoggedIn(getContext());
	}

	@DefaultHandler
	public Resolution show() {
		return new ForwardResolution(VIEW);
	}

	public Resolution back() {
		return new RedirectResolution(PortalActionBean.class);
	}

	public Resolution replaceCode() {

		try {
			User user = userDao.get((String) getContext().getRequest().getSession().getAttribute("userid"));
			user.setMainJavaScript(RegisterActionBean.DEFAULT_CODE);
			user.setActive(true);
			user.setLastCodeChangeDate(new Date());
			userDao.update(user);
			output = "Code successfully replaced";
		} catch (Exception e) {
			log.error("Failed to update code", e);
			output = "Internal server error";
		}

		return new ForwardResolution("/WEB-INF/jsp/ajax/portalSave.jsp");
	}

}
