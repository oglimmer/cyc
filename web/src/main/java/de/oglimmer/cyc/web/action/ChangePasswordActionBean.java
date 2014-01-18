package de.oglimmer.cyc.web.action;

import lombok.Getter;
import lombok.Setter;
import net.sourceforge.stripes.action.Before;
import net.sourceforge.stripes.action.DefaultHandler;
import net.sourceforge.stripes.action.DontValidate;
import net.sourceforge.stripes.action.ForwardResolution;
import net.sourceforge.stripes.action.RedirectResolution;
import net.sourceforge.stripes.action.Resolution;
import net.sourceforge.stripes.validation.SimpleError;
import net.sourceforge.stripes.validation.Validate;
import net.sourceforge.stripes.validation.ValidationErrors;
import net.sourceforge.stripes.validation.ValidationMethod;

import org.mindrot.jbcrypt.BCrypt;

import de.oglimmer.cyc.dao.UserDao;
import de.oglimmer.cyc.dao.couchdb.CouchDbUtil;
import de.oglimmer.cyc.dao.couchdb.UserCouchDb;
import de.oglimmer.cyc.model.User;

public class ChangePasswordActionBean extends BaseAction {
	private static final String VIEW = "/WEB-INF/jsp/changePassword.jsp";

	private UserDao userDao = new UserCouchDb(CouchDbUtil.getDatabase());

	@Validate(required = true)
	@Getter
	@Setter
	private String passwordOld;

	@Validate(required = true)
	@Getter
	@Setter
	private String passwordNew;

	@Validate(required = true)
	@Getter
	@Setter
	private String password2;

	@Validate(required = true)
	@Getter
	@Setter
	private String email;

	private User user;

	@Before
	public void load() {
		user = userDao.get((String) getContext().getRequest().getSession().getAttribute("userid"));
		email = user.getEmail();
	}

	@DefaultHandler
	@DontValidate
	public Resolution show() {
		return new ForwardResolution(VIEW);
	}

	@ValidationMethod
	public void validateUser(ValidationErrors errors) {
		user = userDao.get((String) getContext().getRequest().getSession().getAttribute("userid"));
		LandingActionBean.checkPassword(errors, getContext(), user, passwordOld);
		if (getPasswordNew() == null || !getPasswordNew().equals(getPassword2())) {
			errors.add("password", new SimpleError("The confirmation password doesn''t match the password."));
		}
	}

	public Resolution change() {
		String hashed = BCrypt.hashpw(passwordNew, BCrypt.gensalt());
		user.setPassword(hashed);
		user.setEmail(email);
		userDao.update(user);
		return new RedirectResolution(PortalActionBean.class);
	}
}