package de.oglimmer.cyc.web.actions;

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
	private String passwordOld;
	@Validate(required = true)
	private String passwordNew;
	@Validate(required = true)
	private String password2;
	@Validate(required = true)
	private String email;

	private User user;

	public String getPasswordOld() {
		return passwordOld;
	}

	public void setPasswordOld(String passwordOld) {
		this.passwordOld = passwordOld;
	}

	public String getPasswordNew() {
		return passwordNew;
	}

	public void setPasswordNew(String passwordNew) {
		this.passwordNew = passwordNew;
	}

	public String getPassword2() {
		return password2;
	}

	public void setPassword2(String password2) {
		this.password2 = password2;
	}

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

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