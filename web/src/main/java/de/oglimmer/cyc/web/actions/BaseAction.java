package de.oglimmer.cyc.web.actions;

import net.sourceforge.stripes.action.ActionBean;
import net.sourceforge.stripes.action.ActionBeanContext;
import net.sourceforge.stripes.action.Before;

abstract public class BaseAction implements ActionBean {

	private static final String VERSION = "1.0.1";

	private ActionBeanContext context;

	@Override
	public ActionBeanContext getContext() {
		return context;
	}

	@Override
	public void setContext(ActionBeanContext context) {
		this.context = context;
	}

	@Before
	public void setVersion() {
		getContext().getRequest().setAttribute("currentVersion", VERSION);
	}
}
