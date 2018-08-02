package de.oglimmer.cyc.web.ext;

import java.util.concurrent.TimeUnit;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import com.google.common.cache.CacheBuilder;
import com.google.common.cache.CacheLoader;
import com.google.common.cache.LoadingCache;

import de.oglimmer.cyc.dao.UserDao;
import de.oglimmer.cyc.dao.couchdb.CouchDbUtil;
import de.oglimmer.cyc.dao.couchdb.UserCouchDb;
import de.oglimmer.cyc.model.User;
import net.sourceforge.stripes.action.Resolution;
import net.sourceforge.stripes.controller.ExecutionContext;
import net.sourceforge.stripes.controller.Interceptor;
import net.sourceforge.stripes.controller.Intercepts;
import net.sourceforge.stripes.controller.LifecycleStage;

@Intercepts(LifecycleStage.HandlerResolution)
public class SessionLoggingInterceptor implements Interceptor {

	private UserDao userDao = new UserCouchDb(CouchDbUtil.INSTANCE.getDatabase());

	private LoadingCache<String, String> userRecordCache = CacheBuilder.newBuilder()
			.expireAfterWrite(5, TimeUnit.MINUTES).build(new CacheLoader<String, String>() {
				public String load(String userid) {
					User user = userDao.get(userid);
					if (user != null) {
						return user.getUsername();
					}
					return null;
				}
			});

	@Override
	public Resolution intercept(ExecutionContext ctx) throws Exception {
		HttpServletRequest req = ctx.getActionBeanContext().getRequest();
		HttpSession httpSession = req.getSession(false);
		if (httpSession != null) {
			httpSession.setAttribute("IP", getRemoteIP(req));
			httpSession.setAttribute("UA", req.getHeader("User-Agent"));
			httpSession.setAttribute("userName", userRecordCache.get((String) httpSession.getAttribute("userid")));
		}
		return ctx.proceed();
	}

	private String getRemoteIP(HttpServletRequest request) {
		final String remoteAddr;
		String httpXForwardedFor = request.getHeader("HTTP_X_FORWARDED_FOR");
		if (httpXForwardedFor != null) {
			remoteAddr = httpXForwardedFor;
		} else {
			String ipFromRequest = request.getRemoteAddr();
			if ("127.0.0.1".equals(ipFromRequest)) {
				remoteAddr = request.getHeader("X-Forwarded-For");
			} else {
				remoteAddr = ipFromRequest;
			}
		}
		return remoteAddr;
	}

}
