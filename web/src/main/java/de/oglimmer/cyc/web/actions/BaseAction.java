package de.oglimmer.cyc.web.actions;

import net.sourceforge.stripes.action.ActionBean;
import net.sourceforge.stripes.action.ActionBeanContext;

abstract public class BaseAction implements ActionBean {
	private ActionBeanContext context;

	@Override
	public ActionBeanContext getContext() {
		return context;
	}

	@Override
	public void setContext(ActionBeanContext context) {
		this.context = context;
	}
}
