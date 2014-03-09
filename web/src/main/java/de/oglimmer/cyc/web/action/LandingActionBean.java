package de.oglimmer.cyc.web.action;

import java.util.Date;
import java.util.List;

import javax.servlet.http.HttpSession;

import lombok.Getter;
import lombok.Setter;
import net.sourceforge.stripes.action.ActionBeanContext;
import net.sourceforge.stripes.action.Before;
import net.sourceforge.stripes.action.DefaultHandler;
import net.sourceforge.stripes.action.DontValidate;
import net.sourceforge.stripes.action.ForwardResolution;
import net.sourceforge.stripes.action.RedirectResolution;
import net.sourceforge.stripes.action.Resolution;
import net.sourceforge.stripes.controller.LifecycleStage;
import net.sourceforge.stripes.validation.SimpleError;
import net.sourceforge.stripes.validation.Validate;
import net.sourceforge.stripes.validation.ValidationErrors;
import net.sourceforge.stripes.validation.ValidationMethod;

import org.ektorp.DocumentNotFoundException;
import org.mindrot.jbcrypt.BCrypt;

import com.google.common.html.HtmlEscapers;

import de.oglimmer.cyc.dao.UserDao;
import de.oglimmer.cyc.dao.couchdb.CouchDbUtil;
import de.oglimmer.cyc.dao.couchdb.UserCouchDb;
import de.oglimmer.cyc.model.User;
import de.oglimmer.cyc.web.WebContainerProperties;
import de.oglimmer.cyc.web.DoesNotRequireLogin;
import de.oglimmer.cyc.web.ThreeDaysWinner;

@DoesNotRequireLogin
public class LandingActionBean extends BaseAction {
	private static final String VIEW = "/WEB-INF/jsp/landing.jsp";

	private UserDao userDao = new UserCouchDb(CouchDbUtil.getDatabase());

	@Validate(required = true)
	@Getter
	@Setter
	private String username;

	@Validate(required = true)
	@Getter
	@Setter
	private String password;

	@Getter
	private String threeDayWinner;

	@Getter
	private String fbAppId;

	@Getter
	private String googleClientId;

	@Getter
	private boolean showCycLogin;

	@Before(stages = { LifecycleStage.HandlerResolution })
	public void beforeHandlerResolution() {
		fbAppId = WebContainerProperties.INSTANCE.getFbAppId();
		googleClientId = WebContainerProperties.INSTANCE.getGoogleClientId();
		if (fbAppId.isEmpty() && googleClientId.isEmpty()) {
			showCycLogin = true;
		}

		ThreeDaysWinner.Result result = ThreeDaysWinner.INSTANCE.calcThreeDayWinner();
		threeDayWinner = result.getThreeDayWinner();
	}

	@ValidationMethod
	public void validateUser(ValidationErrors errors) {
		HttpSession httpSession = getContext().getRequest().getSession(false);
		if (httpSession != null) {
			httpSession.removeAttribute("userid");
		}
		try {
			String uname = HtmlEscapers.htmlEscaper().escape(getUsername()).toLowerCase();
			List<User> userList = userDao.findByUsername(uname);
			if (userList.size() == 1) {
				User user = userList.get(0);
				checkPassword(errors, getContext(), user, password);
				getContext().getRequest().getSession(true).setAttribute("userid", user.getId());
				user.setLastLoginDate(new Date());
				userDao.update(user);
			} else {
				errors.add("password", new SimpleError("The password is incorrect."));
			}
		} catch (DocumentNotFoundException e) {
			errors.add("password", new SimpleError("The password is incorrect."));
		}
		showCycLogin = true;
	}

	static void checkPassword(ValidationErrors errors, ActionBeanContext context, User user, String password) {
		if (user == null || !BCrypt.checkpw(password, user.getPassword())) {
			errors.add("password", new SimpleError("The password is incorrect."));
		}
	}

	@DefaultHandler
	@DontValidate
	public Resolution show() {
		HttpSession httpSession = getContext().getRequest().getSession(false);
		if (httpSession != null && httpSession.getAttribute("userid") != null) {
			return new RedirectResolution(PortalActionBean.class);
		} else {
			return new ForwardResolution(VIEW);
		}
	}

	public Resolution login() {
		return new RedirectResolution(PortalActionBean.class);
	}

	@DontValidate
	public Resolution forgot() {
		return new RedirectResolution(PasswordForgottenActionBean.class);
	}
}