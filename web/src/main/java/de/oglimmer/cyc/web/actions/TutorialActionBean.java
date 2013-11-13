package de.oglimmer.cyc.web.actions;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import net.sourceforge.stripes.action.DefaultHandler;
import net.sourceforge.stripes.action.ForwardResolution;
import net.sourceforge.stripes.action.RedirectResolution;
import net.sourceforge.stripes.action.Resolution;
import de.oglimmer.cyc.dao.UserDao;
import de.oglimmer.cyc.dao.couchdb.CouchDbUtil;
import de.oglimmer.cyc.dao.couchdb.UserCouchDb;
import de.oglimmer.cyc.model.User;
import de.oglimmer.cyc.web.DoesNotRequireLogin;

@DoesNotRequireLogin
public class TutorialActionBean extends BaseAction {

	private static Logger log = LoggerFactory.getLogger(TutorialActionBean.class);

	private static final String VIEW = "/WEB-INF/jsp/tutorial.jsp";

	private UserDao userDao = new UserCouchDb(CouchDbUtil.getDatabase());

	private String output;

	public String getOutput() {
		return output;
	}

	public void setOutput(String output) {
		this.output = output;
	}

	@DefaultHandler
	public Resolution show() {
		return new ForwardResolution(VIEW);
	}

	public Resolution back() {
		return new RedirectResolution(PortalActionBean.class);
	}

	public Resolution replaceCode() {

		try {
			User user = userDao.get((String) getContext().getRequest().getSession().getAttribute("userid"));
			user.setMainJavaScript("//click the Tutorial link in the upper right\n\ncompany.launch = function() {\n	company.menu.add(\"Kebab\", [\"CHICKEN_MEAT\", \"BREAD\"], 3);\n};\n\ncompany.realEstateAgent = function(realEstateProfiles) {\n	if(company.establishments.size() < 1) {\n		realEstateProfiles.get(0).tryLease(0);\n	}\n};\n\ncompany.humanResources.hiringProcess = function(applicationProfiles) {    \n	if (company.humanResources.getEmployees(\"WAITER\").size() < 1) {\n		applicationProfiles.subList(\"WAITER\").get(0).offer(\n			company.establishments.get(0));\n	}\n	if (company.humanResources.getEmployees(\"CHEF\").size() < 1) {\n		applicationProfiles.subList(\"CHEF\").get(0).offer(\n			company.establishments.get(0));\n	}\n};\n\ncompany.doMonthly = function() {\n	if(company.establishments.size()==1) {\n		company.establishments.get(0).buyInteriorAccessoriesNotExist(\"COUNTER\");\n	}	\n};\n\ncompany.doWeekly = function() {\n};\n\ncompany.doDaily = function() {\n	company.grocer.order(\"CHICKEN_MEAT\", 100);\n	company.grocer.order(\"BREAD\", 100);	\n};\n\ncompany.foodDelivery = function(foodDelivery) {\n	foodDelivery.each(function(foodUnit) {\n		foodUnit.distributeEqually();\n	});\n};\n");
			user.setActive(true);
			userDao.update(user);
			output = "Code successfully replaced";
		} catch (Exception e) {
			log.error("Failed to update code", e);
			output = "Internal server error";
		}

		return new ForwardResolution("/WEB-INF/jsp/ajax/portalSave.jsp");
	}

}
