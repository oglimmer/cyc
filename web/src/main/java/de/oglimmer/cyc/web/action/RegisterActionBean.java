package de.oglimmer.cyc.web.action;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.Date;
import java.util.List;

import org.mindrot.jbcrypt.BCrypt;

import com.google.common.html.HtmlEscapers;

import de.oglimmer.cyc.dao.UserDao;
import de.oglimmer.cyc.dao.couchdb.CouchDbUtil;
import de.oglimmer.cyc.dao.couchdb.UserCouchDb;
import de.oglimmer.cyc.model.User;
import de.oglimmer.cyc.util.DefaultCode;
import de.oglimmer.cyc.web.DoesNotRequireLogin;
import de.oglimmer.cyc.web.WebContainerProperties;
import de.oglimmer.cyc.web.ext.CaptchaServlet;
import de.oglimmer.cyc.web.util.EmailService;
import lombok.Getter;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;
import net.sourceforge.stripes.action.After;
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

@Slf4j
@DoesNotRequireLogin
public class RegisterActionBean extends BaseAction {
	private static final String VIEW = "/WEB-INF/jsp/register.jsp";
	private static final String POST = "/WEB-INF/jsp/registerPost.jsp";

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
	@Getter
	@Setter
	private String captchaTokenCrypted;
	@Getter
	@Setter
	private String captchaTokenCryptedUrl;
	@Getter
	@Setter
	private String captchaTokenEntered;

	@Getter
	private String addressPageOwner = WebContainerProperties.INSTANCE.getAddressPageOwner();

	@After(stages = { LifecycleStage.BindingAndValidation })
	public void generateCaptcha() {
		if (WebContainerProperties.INSTANCE.isCaptchaEnabled()
				&& (captchaTokenCrypted == null || captchaTokenCrypted.isEmpty())) {
			captchaTokenCrypted = CaptchaServlet.getService().encrypt(CaptchaServlet.generateToken());
		}
	}

	@Before(stages = { LifecycleStage.ResolutionExecution })
	public void encodeCaptchaTokenCrypted() {
		try {
			if (WebContainerProperties.INSTANCE.isCaptchaEnabled()) {
				captchaTokenCryptedUrl = URLEncoder.encode(captchaTokenCrypted, "UTF-8");
			}
		} catch (UnsupportedEncodingException e) {
			log.error("Failed to encode captchaTokenCrypted", e);
		}
	}

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
		if (WebContainerProperties.INSTANCE.isCaptchaEnabled()
				&& !captchaTokenEntered.equalsIgnoreCase(CaptchaServlet.getService().decrypt(captchaTokenCrypted))) {
			errors.add("captcha", new SimpleError("The captcha doesn't match!"));
			captchaTokenCrypted = CaptchaServlet.getService().encrypt(CaptchaServlet.generateToken());
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
		user.setActive(false);
		user.setEmailConfirmed(false);
		userDao.add(user);
		EmailService.INSTANCE.sendNewAccount(user.getEmail(), user.getId(), user.getUsername());
		return new ForwardResolution(POST);
	}

	@DontValidate
	public Resolution cancel() {
		return new RedirectResolution(LandingActionBean.class);
	}
}