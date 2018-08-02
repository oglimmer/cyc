package de.oglimmer.cyc.web.action;

import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.Date;

import org.ektorp.DocumentNotFoundException;

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
import net.sourceforge.stripes.action.ForwardResolution;
import net.sourceforge.stripes.action.RedirectResolution;
import net.sourceforge.stripes.action.Resolution;

@DoesNotRequireLogin
public class RegisterConfirmActionBean extends BaseAction {
	private static final String VIEW = "/WEB-INF/jsp/registerConfirm.jsp";

	private UserDao userDao = new UserCouchDb(CouchDbUtil.INSTANCE.getDatabase());

	@Getter
	@Setter
	private String confirmId;

	@DefaultHandler
	public Resolution show() {

		if (WebContainerProperties.INSTANCE.getSystemDisabledDate().before(new Date())) {
			return new RedirectResolution(LandingActionBean.class);
		}

		User user = null;
		try {
			if(confirmId != null && !confirmId.isEmpty()) {
				user = userDao.get(confirmId);
			}
		} catch (DocumentNotFoundException e) {
			// this means the confirmId doesn't exist at all
		}
		if (user == null || olderThan48h(user.getCreatedDate()) || user.isEmailConfirmed()) {
			return new ForwardResolution(VIEW);
		} else {
			user.setActive(true);
			user.setEmailConfirmed(true);
			user.setLastLoginDate(new Date());
			userDao.update(user);
			getContext().getRequest().getSession(true).setAttribute("userid", user.getId());
			EmailService.INSTANCE.informAdminConfirm(user.getUsername(), user.getEmail());
			return new RedirectResolution(PortalActionBean.class);
		}
	}

	private boolean olderThan48h(Date createdDate) {
		Instant twoDaysAgo = Instant.now().minus(48, ChronoUnit.HOURS);
		Instant createdDateInstant = Instant.ofEpochMilli(createdDate.getTime());
		return createdDateInstant.isBefore(twoDaysAgo);
	}

}