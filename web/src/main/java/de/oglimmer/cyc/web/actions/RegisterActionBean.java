package de.oglimmer.cyc.web.actions;

import java.util.List;

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
import de.oglimmer.cyc.web.DoesNotRequireLogin;

@DoesNotRequireLogin
public class RegisterActionBean extends BaseAction {
	private static final String VIEW = "/WEB-INF/jsp/register.jsp";

	private UserDao userDao = new UserCouchDb(CouchDbUtil.getDatabase());

	@Validate(required = true)
	private String username;
	@Validate(required = true)
	private String password;
	@Validate(required = true)
	private String password2;
	@Validate(required = true)
	private String email;

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

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public String getPassword2() {
		return password2;
	}

	public void setPassword2(String password2) {
		this.password2 = password2;
	}

	@DefaultHandler
	@DontValidate
	public Resolution show() {
		return new ForwardResolution(VIEW);
	}

	@ValidationMethod
	public void validateUser(ValidationErrors errors) {
		if (getUsername().contains("<") || getUsername().contains(">")) {
			errors.add("password", new SimpleError("The username must not contain any HTML"));
		}
		if (getPassword() == null || !getPassword().equals(getPassword2())) {
			errors.add("password", new SimpleError("The confirmation password doesn''t match the password."));
		}
		if (!getEmail().matches("(?i)^[_a-z0-9-]+(\\.[_a-z0-9-]+)*@[a-z0-9-]+(\\.[a-z0-9-]+)*(\\.[a-z]{2,4})$")) {
			errors.add("email", new SimpleError("The email address isn''t valid."));
		}
		List<User> userList = userDao.findByUsername(getUsername());
		if (userList.size() == 1) {
			errors.add("username", new SimpleError("The username is already in use."));
		}
	}

	public Resolution register() {
		String hashed = BCrypt.hashpw(password, BCrypt.gensalt());
		User user = new User(getUsername(), hashed, getEmail());
		user.setMainJavaScript("//click the Tutorial link in the upper right\r\rcompany.humanResources.hiringProcess = function(applicationProfiles) {    \r};\r\rcompany.realEstateAgent = function(realEstateProfiles) {\r};\r\rcompany.doMonthly = function() {\r};\r\rcompany.doWeekly = function() {\r};\r\rcompany.doDaily = function() {\r};\r\rcompany.foodDelivery = function(foodDelivery) {\r};\r");
		userDao.add(user);
		return new RedirectResolution(LandingActionBean.class);
	}
}