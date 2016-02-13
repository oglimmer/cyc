package de.oglimmer.cyc.web.action;

import java.util.Date;
import java.util.List;

import org.apache.commons.lang.StringEscapeUtils;
import org.mindrot.jbcrypt.BCrypt;

import com.google.common.html.HtmlEscapers;

import de.oglimmer.cyc.dao.UserDao;
import de.oglimmer.cyc.dao.couchdb.CouchDbUtil;
import de.oglimmer.cyc.dao.couchdb.UserCouchDb;
import de.oglimmer.cyc.model.User;
import de.oglimmer.cyc.web.WebContainerProperties;
import lombok.Getter;
import lombok.Setter;
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

public class ChangePasswordActionBean extends BaseAction {
	private static final String VIEW = "/WEB-INF/jsp/changePassword.jsp";

	private UserDao userDao = new UserCouchDb(CouchDbUtil.getDatabase());

	@Validate(required = true)
	@Getter
	@Setter
	private String passwordOld;

	@Getter
	@Setter
	private String passwordNew;

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
	private String username;

	private User user;

	@Before(stages = { LifecycleStage.BindingAndValidation })
	public void load() {
		user = userDao.get((String) getContext().getRequest().getSession().getAttribute("userid"));
		email = user.getEmail();
		username = StringEscapeUtils.unescapeHtml(user.getUsername());
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
		if (getPasswordNew() != null && !getPasswordNew().isEmpty() && !getPasswordNew().equals(getPassword2())) {
			errors.add("password", new SimpleError("The confirmation password doesn''t match the password."));
		}
		if (!getEmail().matches("(?i)^[_a-z0-9-]+(\\.[_a-z0-9-]+)*@[a-z0-9-]+(\\.[a-z0-9-]+)*(\\.[a-z]{2,4})$")) {
			errors.add("email", new SimpleError("The email address isn''t valid."));
		}
		List<User> userList = userDao.findByUsername(getUsername());
		if (!userList.isEmpty() && !userList.get(0).getId().equals(user.getId())) {
			errors.add("username", new SimpleError("The username is already in use."));
		}
		List<User> emailList = userDao.findByEmail(getEmail());
		if (!emailList.isEmpty() && !emailList.get(0).getId().equals(user.getId())) {
			errors.add("email", new SimpleError("The email address is already registered. Please use 'I forgot my passwort' to reset your password."));
		}
	}
	
	public Resolution change() {
		if (getPasswordNew() != null && !getPasswordNew().isEmpty()) {
			String hashed = BCrypt.hashpw(passwordNew, BCrypt.gensalt());
			user.setPassword(hashed);
		}
		user.setEmail(email);
		if (!WebContainerProperties.INSTANCE.getSystemDisabledDate().before(new Date())) {
			user.setUsername(HtmlEscapers.htmlEscaper().escape(username));
		}
		userDao.update(user);
		return new RedirectResolution(PortalActionBean.class);
	}
	
	@DontValidate
	public Resolution cancel() {
		return new RedirectResolution(PortalActionBean.class);
	}
}