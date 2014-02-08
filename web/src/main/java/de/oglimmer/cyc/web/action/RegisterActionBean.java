package de.oglimmer.cyc.web.action;

import java.util.Date;
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
	public static final String DEFAULT_CODE = "//click the Tutorial link in the upper right\n\ncompany.launch = function() {\n	company.menu.add(\"Kebab\", [\"CHICKEN_MEAT\", \"BREAD\"], 3);\n};\n\ncompany.realEstateAgent = function(realEstateProfiles) {\n	if(company.establishments.size() < 1) {\n		realEstateProfiles.get(0).tryLease(0);\n	}\n};\n\ncompany.humanResources.hiringProcess = function(applicationProfiles) {    \n	if (company.humanResources.getEmployees(\"WAITER\").size() < 1) {\n		applicationProfiles.subList(\"WAITER\").get(0).offer(\n			company.establishments.get(0));\n	}\n	if (company.humanResources.getEmployees(\"CHEF\").size() < 1) {\n		applicationProfiles.subList(\"CHEF\").get(0).offer(\n			company.establishments.get(0));\n	}\n};\n\ncompany.doMonthly = function() {\n	if(company.establishments.size()==1) {\n		company.establishments.get(0).buyInteriorAccessoriesNotExist(\"COUNTER\");\n	}	\n};\n\ncompany.doWeekly = function() {\n};\n\ncompany.doDaily = function(dailyStatistics) {\n	company.grocer.order(\"CHICKEN_MEAT\", 100);\n	company.grocer.order(\"BREAD\", 100);	\n};\n\ncompany.foodDelivery = function(foodDelivery) {\n	foodDelivery.each(function(foodUnit) {\n		foodUnit.distributeEqually();\n	});\n};\n";
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
		user.setMainJavaScript(DEFAULT_CODE);
		user.setCreatedDate(new Date());
		userDao.add(user);
		return new RedirectResolution(LandingActionBean.class);
	}
}