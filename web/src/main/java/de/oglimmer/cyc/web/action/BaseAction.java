package de.oglimmer.cyc.web.action;

import de.oglimmer.utils.VersionFromManifest;
import lombok.Getter;
import lombok.Setter;
import net.sourceforge.stripes.action.ActionBean;
import net.sourceforge.stripes.action.ActionBeanContext;
import net.sourceforge.stripes.action.Before;

public abstract class BaseAction implements ActionBean {

	private static final String API_VERSION = "1.0.7";
	private static String longVersionCache;

	@Getter
	@Setter
	private ActionBeanContext context;

	@Getter
	@Setter
	private String longVersion;

	@Before
	public void retrieveVersion() {
		if (longVersionCache == null) {
			VersionFromManifest vfm = new VersionFromManifest();
			vfm.initFromFile(getContext().getServletContext().getRealPath("/META-INF/MANIFEST.MF"));
			longVersionCache = vfm.getLongVersion();
		}

		longVersion = longVersionCache;

		getContext().getRequest().setAttribute("API_Version", API_VERSION);
	}
}
