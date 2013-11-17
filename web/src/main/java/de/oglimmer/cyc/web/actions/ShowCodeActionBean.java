package de.oglimmer.cyc.web.actions;

import java.util.List;

import net.sourceforge.stripes.action.Before;
import net.sourceforge.stripes.action.DefaultHandler;
import net.sourceforge.stripes.action.ForwardResolution;
import net.sourceforge.stripes.action.Resolution;
import de.oglimmer.cyc.dao.UserDao;
import de.oglimmer.cyc.dao.couchdb.CouchDbUtil;
import de.oglimmer.cyc.dao.couchdb.UserCouchDb;
import de.oglimmer.cyc.model.User;

public class ShowCodeActionBean extends BaseAction {
	// private static Logger log = LoggerFactory.getLogger(ShowCodeActionBean.class);

	private static final String VIEW = "/WEB-INF/jsp/showCode.jsp";

	private UserDao userDao = new UserCouchDb(CouchDbUtil.getDatabase());

	private String companyCode;
	private String companyName;

	public String getCompanyCode() {
		return companyCode;
	}

	public void setCompanyCode(String companyCode) {
		this.companyCode = companyCode;
	}

	public String getCompanyName() {
		return companyName;
	}

	public void setCompanyName(String companyName) {
		this.companyName = companyName;
	}

	@Before(on = { "show" })
	public void loadFromDb() {
		List<User> userList = userDao.findByUsername(getContext().getRequest().getParameter("username").toLowerCase());
		if (userList.size() == 1) {
			User user = userList.get(0);
			if (user.getOpenSource() > 0) {
				companyCode = user.getMainJavaScript();
				companyName = user.getUsername();
			}
		}
	}

	@DefaultHandler
	public Resolution show() {
		return new ForwardResolution(VIEW);
	}

}