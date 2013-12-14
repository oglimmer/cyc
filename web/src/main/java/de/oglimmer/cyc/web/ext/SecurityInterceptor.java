package de.oglimmer.cyc.web.ext;

import javax.servlet.http.HttpSession;

import net.sourceforge.stripes.action.ActionBeanContext;
import net.sourceforge.stripes.action.RedirectResolution;
import net.sourceforge.stripes.action.Resolution;
import net.sourceforge.stripes.controller.ExecutionContext;
import net.sourceforge.stripes.controller.Interceptor;
import net.sourceforge.stripes.controller.Intercepts;
import net.sourceforge.stripes.controller.LifecycleStage;
import de.oglimmer.cyc.web.DoesNotRequireLogin;
import de.oglimmer.cyc.web.actions.LandingActionBean;

@Intercepts(LifecycleStage.HandlerResolution)
public class SecurityInterceptor implements Interceptor {

	@Override
	public Resolution intercept(ExecutionContext ctx) throws Exception {

		Resolution res = ctx.proceed();
		Class<?> currentBean = ctx.getActionBean().getClass();
		if (currentBean.isAnnotationPresent(DoesNotRequireLogin.class)) {
			return res;
		}

		if (isLoggedIn(ctx.getActionBeanContext())) {
			return res;
		} else {
			return new RedirectResolution(LandingActionBean.class);
		}
	}

	private boolean isLoggedIn(ActionBeanContext ctx) {
		HttpSession httpSession = ctx.getRequest().getSession(false);
		return httpSession != null && httpSession.getAttribute("userid") != null;
	}

}
