package de.oglimmer.cyc.web.action;

import java.util.List;

import lombok.Getter;
import lombok.Setter;
import net.sourceforge.stripes.action.Before;
import net.sourceforge.stripes.action.DefaultHandler;
import net.sourceforge.stripes.action.ForwardResolution;
import net.sourceforge.stripes.action.Resolution;
import de.oglimmer.cyc.dao.UserDao;
import de.oglimmer.cyc.dao.couchdb.CouchDbUtil;
import de.oglimmer.cyc.dao.couchdb.UserCouchDb;
import de.oglimmer.cyc.model.User;
import de.oglimmer.cyc.web.DoesNotRequireLogin;

@DoesNotRequireLogin
public class ShowCodeActionBean extends BaseAction {

	private static final String VIEW = "/WEB-INF/jsp/showCode.jsp";

	private UserDao userDao = new UserCouchDb(CouchDbUtil.getDatabase());

	@Getter
	@Setter
	private String companyCode;
	@Getter
	@Setter
	private String companyName;

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