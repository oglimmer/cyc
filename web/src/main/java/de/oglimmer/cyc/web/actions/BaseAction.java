package de.oglimmer.cyc.web.actions;

import java.io.FileInputStream;
import java.io.InputStream;
import java.text.DateFormat;
import java.util.Date;
import java.util.jar.Attributes;
import java.util.jar.Manifest;

import lombok.Getter;
import lombok.Setter;
import net.sourceforge.stripes.action.ActionBean;
import net.sourceforge.stripes.action.ActionBeanContext;
import net.sourceforge.stripes.action.Before;

abstract public class BaseAction implements ActionBean {

	private static final String API_VERSION = "1.0.2";
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
			String commit;
			String version;
			String creationDate;
			try (InputStream is = new FileInputStream(getContext().getServletContext().getRealPath(
					"/META-INF/MANIFEST.MF"))) {
				Manifest mf = new Manifest(is);
				Attributes attr = mf.getMainAttributes();
				commit = attr.getValue("SVN-Revision-No");
				version = attr.getValue("CYC-Version");
				long time = Long.parseLong(attr.getValue("Creation-Date"));
				creationDate = DateFormat.getDateTimeInstance(DateFormat.SHORT, DateFormat.SHORT)
						.format(new Date(time));
			} catch (Exception e) {
				commit = "?";
				creationDate = "?";
				version = "?";
			}

			longVersionCache = "V" + version + " [Commit#" + commit + "] build " + creationDate;
		}

		longVersion = longVersionCache;

		getContext().getRequest().setAttribute("API_Version", API_VERSION);
	}
}
