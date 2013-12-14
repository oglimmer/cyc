package de.oglimmer.cyc.web.ext;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import net.sourceforge.stripes.action.Resolution;
import net.sourceforge.stripes.controller.ExecutionContext;
import net.sourceforge.stripes.controller.Interceptor;
import net.sourceforge.stripes.controller.Intercepts;
import net.sourceforge.stripes.controller.LifecycleStage;

@Intercepts(LifecycleStage.HandlerResolution)
public class SessionLoggingInterceptor implements Interceptor {

	@Override
	public Resolution intercept(ExecutionContext ctx) throws Exception {
		HttpServletRequest req = ctx.getActionBeanContext().getRequest();
		HttpSession httpSession = req.getSession(false);
		if (httpSession != null) {
			httpSession.setAttribute("IP", req.getHeader("X-Forwarded-For"));
			httpSession.setAttribute("UA", req.getHeader("User-Agent"));
		}
		Resolution res = ctx.proceed();
		return res;
	}

}
