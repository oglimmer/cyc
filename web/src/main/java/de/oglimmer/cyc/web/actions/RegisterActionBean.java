package de.oglimmer.cyc.web.actions;

import java.util.List;

import lombok.Getter;
import lombok.Setter;
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

import com.google.common.html.HtmlEscapers;

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
	@Getter
	@Setter
	private String username;
	@Validate(required = true)
	@Getter
	@Setter
	private String password;
	@Validate(required = true)
	@Getter
	@Setter
	private String password2;
	@Validate(required = true)
	@Getter
	@Setter
	private String email;

	@DefaultHandler
	@DontValidate
	public Resolution show() {
		return new ForwardResolution(VIEW);
	}

	@ValidationMethod
	public void validateUser(ValidationErrors errors) {
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
		User user = new User(HtmlEscapers.htmlEscaper().escape(getUsername()), hashed, getEmail());
		user.setMainJavaScript("//click the Tutorial link in the upper right\r\rcompany.humanResources.hiringProcess = function(applicationProfiles) {    \r};\r\rcompany.realEstateAgent = function(realEstateProfiles) {\r};\r\rcompany.doMonthly = function() {\r};\r\rcompany.doWeekly = function() {\r};\r\rcompany.doDaily = function() {\r};\r\rcompany.foodDelivery = function(foodDelivery) {\r};\r");
		userDao.add(user);
		return new RedirectResolution(LandingActionBean.class);
	}
}