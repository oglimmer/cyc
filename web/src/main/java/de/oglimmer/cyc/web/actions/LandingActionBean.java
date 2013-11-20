package de.oglimmer.cyc.web.actions;

import java.util.List;

import net.sourceforge.stripes.action.ActionBeanContext;
import net.sourceforge.stripes.action.DefaultHandler;
import net.sourceforge.stripes.action.DontValidate;
import net.sourceforge.stripes.action.ForwardResolution;
import net.sourceforge.stripes.action.RedirectResolution;
import net.sourceforge.stripes.action.Resolution;
import net.sourceforge.stripes.validation.SimpleError;
import net.sourceforge.stripes.validation.Validate;
import net.sourceforge.stripes.validation.ValidationErrors;
import net.sourceforge.stripes.validation.ValidationMethod;

import org.ektorp.DocumentNotFoundException;
import org.mindrot.jbcrypt.BCrypt;

import de.oglimmer.cyc.dao.UserDao;
import de.oglimmer.cyc.dao.couchdb.CouchDbUtil;
import de.oglimmer.cyc.dao.couchdb.UserCouchDb;
import de.oglimmer.cyc.model.User;
import de.oglimmer.cyc.web.DoesNotRequireLogin;

@DoesNotRequireLogin
public class LandingActionBean extends BaseAction {
	private static final String VIEW = "/WEB-INF/jsp/landing.jsp";

	private UserDao userDao = new UserCouchDb(CouchDbUtil.getDatabase());

	@Validate(required = true)
	private String username;

	@Validate(required = true)
	private String password;

	public String getUsername() {
		return username;
	}

	public void setUsername(String username) {
		this.username = username;
	}

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}

	@ValidationMethod
	public void validateUser(ValidationErrors errors) {
		getContext().getRequest().getSession().removeAttribute("userid");
		try {
			List<User> userList = userDao.findByUsername(username.toLowerCase());
			if (userList.size() == 1) {
				User user = userList.get(0);
				checkPassword(errors, getContext(), user, password);
				getContext().getRequest().getSession().setAttribute("userid", user.getId());
			} else {
				errors.add("password", new SimpleError("The password is incorrect."));
			}
		} catch (DocumentNotFoundException e) {
			errors.add("password", new SimpleError("The password is incorrect."));
		}
	}

	static void checkPassword(ValidationErrors errors, ActionBeanContext context, User user, String password) {
		if (user == null || !BCrypt.checkpw(password, user.getPassword())) {
			errors.add("password", new SimpleError("The password is incorrect."));
		}
	}

	@DefaultHandler
	@DontValidate
	public Resolution show() {
		if (getContext().getRequest().getSession().getAttribute("userid") != null) {
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