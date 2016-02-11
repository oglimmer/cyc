package de.oglimmer.cyc.web.action;

import java.util.Date;
import java.util.List;

import org.mindrot.jbcrypt.BCrypt;

import com.google.common.html.HtmlEscapers;

import de.oglimmer.cyc.dao.UserDao;
import de.oglimmer.cyc.dao.couchdb.CouchDbUtil;
import de.oglimmer.cyc.dao.couchdb.UserCouchDb;
import de.oglimmer.cyc.model.User;
import de.oglimmer.cyc.web.DoesNotRequireLogin;
import de.oglimmer.cyc.web.WebContainerProperties;
import de.oglimmer.cyc.util.DefaultCode;
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
	@Validate(required = true)
	@Getter
	@Setter
	private boolean agreetermsandconditions;

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
		if (!userList.isEmpty()) {
			errors.add("username", new SimpleError("The username is already in use."));
		}
		List<User> emailList = userDao.findByEmail(getEmail());
		if (!emailList.isEmpty()) {
			errors.add("email", new SimpleError(
					"The email address is already registered. Please use 'I forgot my passwort' to reset your password."));
		}
	}

	public Resolution register() {
		if (WebContainerProperties.INSTANCE.getSystemDisabledDate().before(new Date())) {
			return new RedirectResolution(LandingActionBean.class);
		}
		String hashed = BCrypt.hashpw(password, BCrypt.gensalt());
		User user = new User(HtmlEscapers.htmlEscaper().escape(getUsername()), hashed, getEmail());
		user.setMainJavaScript(DefaultCode.INSTANCE.getDefaultCode());
		user.setCreatedDate(new Date());
		user.setLastLoginDate(new Date());
		user.setActive(true);
		userDao.add(user);
		getContext().getRequest().getSession(true).setAttribute("userid", user.getId());
		return new RedirectResolution(PortalActionBean.class);
	}

	@DontValidate
	public Resolution cancel() {
		return new RedirectResolution(LandingActionBean.class);
	}
}