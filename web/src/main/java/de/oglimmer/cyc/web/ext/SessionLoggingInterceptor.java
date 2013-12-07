package de.oglimmer.cyc.web.ext;

import javax.servlet.http.HttpServletRequest;

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
		req.getSession().setAttribute("IP", req.getRemoteHost());
		Resolution res = ctx.proceed();
		return res;
	}

}
