package de.oglimmer.cyc.web.actions;

import net.sourceforge.stripes.action.ActionBean;
import net.sourceforge.stripes.action.ActionBeanContext;
import net.sourceforge.stripes.action.Before;
import de.oglimmer.cyc.GameRunStarter;

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

	@Before
	public void setVersion() {
		getContext().getRequest().setAttribute("currentVersion", GameRunStarter.VERSION);
	}
}
