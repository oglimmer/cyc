package de.oglimmer.cyc.web.action;

import java.util.Date;
import java.util.List;

import de.oglimmer.cyc.dao.UserDao;
import de.oglimmer.cyc.dao.couchdb.CouchDbUtil;
import de.oglimmer.cyc.dao.couchdb.UserCouchDb;
import de.oglimmer.cyc.model.User;
import de.oglimmer.cyc.web.DoesNotRequireLogin;
import de.oglimmer.cyc.web.WebContainerProperties;
import de.oglimmer.cyc.web.util.EmailService;
import lombok.Getter;
import lombok.Setter;
import net.sourceforge.stripes.action.DefaultHandler;
import net.sourceforge.stripes.action.DontValidate;
import net.sourceforge.stripes.action.ForwardResolution;
import net.sourceforge.stripes.action.RedirectResolution;
import net.sourceforge.stripes.action.Resolution;

@DoesNotRequireLogin
public class ResendActionBean extends BaseAction {
	private static final String POST = "/WEB-INF/jsp/registerPost.jsp";

	private UserDao userDao = new UserCouchDb(CouchDbUtil.getDatabase());

	@Getter
	@Setter
	private String email;

	@DefaultHandler
	@DontValidate
	public Resolution show() {
		if (WebContainerProperties.INSTANCE.getSystemDisabledDate().before(new Date())) {
			return new RedirectResolution(LandingActionBean.class);
		}
		List<User> users = userDao.findByEmail(email);
		for (User user : users) {
			user.setCreatedDate(new Date());
			userDao.update(user);
			EmailService.INSTANCE.sendNewAccount(user.getEmail(), user.getId(), user.getUsername());
		}
		return new ForwardResolution(POST);
	}

}