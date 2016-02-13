package de.oglimmer.cyc.web.action;

import java.util.List;

import org.apache.commons.lang.RandomStringUtils;
import org.mindrot.jbcrypt.BCrypt;

import de.oglimmer.cyc.dao.UserDao;
import de.oglimmer.cyc.dao.couchdb.CouchDbUtil;
import de.oglimmer.cyc.dao.couchdb.UserCouchDb;
import de.oglimmer.cyc.model.User;
import de.oglimmer.cyc.web.DoesNotRequireLogin;
import de.oglimmer.cyc.web.util.EmailService;
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

@DoesNotRequireLogin
public class PasswordForgottenActionBean extends BaseAction {
	private static final String VIEW = "/WEB-INF/jsp/passwordForgotten.jsp";

	private UserDao userDao = new UserCouchDb(CouchDbUtil.getDatabase());

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
		if (!getEmail().matches("(?i)^[_a-z0-9-]+(\\.[_a-z0-9-]+)*@[a-z0-9-]+(\\.[a-z0-9-]+)*(\\.[a-z]{2,4})$")) {
			errors.add("email", new SimpleError("The email address isn''t valid."));
		}
	}

	public Resolution sendPassword() {

		List<User> users = userDao.findByEmail(email.toLowerCase());
		for (User user : users) {
			String newPass = RandomStringUtils.random(36, true, true);
			String hashed = BCrypt.hashpw(newPass, BCrypt.gensalt());
			user.setPassword(hashed);
			userDao.update(user);
			EmailService.INSTANCE.sendPasswordReset(user.getEmail(), newPass);			
		}

		return new RedirectResolution(LandingActionBean.class);
	}
}