package de.oglimmer.cyc.web.action;

import de.oglimmer.cyc.web.DoesNotRequireLogin;
import de.oglimmer.cyc.web.WebContainerProperties;
import lombok.Getter;
import net.sourceforge.stripes.action.DefaultHandler;
import net.sourceforge.stripes.action.ForwardResolution;
import net.sourceforge.stripes.action.RedirectResolution;
import net.sourceforge.stripes.action.Resolution;

@DoesNotRequireLogin
public class ImpressumActionBean extends BaseAction {

	private static final String VIEW = "/WEB-INF/jsp/impressum.jsp";

	@Getter
	private String addressPageOwner = WebContainerProperties.INSTANCE.getAddressPageOwner();

	
	@DefaultHandler
	public Resolution show() {
		return new ForwardResolution(VIEW);
	}

	public Resolution back() {
		return new RedirectResolution(PortalActionBean.class);
	}

}
