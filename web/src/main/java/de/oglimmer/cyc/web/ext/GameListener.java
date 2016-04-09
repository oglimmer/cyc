package de.oglimmer.cyc.web.ext;

import javax.servlet.ServletContextEvent;
import javax.servlet.ServletContextListener;
import javax.servlet.annotation.WebListener;

import org.apache.commons.lang.StringUtils;

import de.oglimmer.cyc.dao.couchdb.CouchDbUtil;
import de.oglimmer.cyc.web.GameScheduler;
import de.oglimmer.cyc.web.WebContainerProperties;
import de.oglimmer.cyc.web.util.EmailService;

@WebListener
public class GameListener implements ServletContextListener {

	@Override
	public void contextInitialized(ServletContextEvent sce) {
		String realPath = sce.getServletContext().getRealPath("/");
		GameScheduler.INSTANCE
				.setWarVersion(StringUtils.substringBefore(StringUtils.substringAfter(realPath, "##"), "/"));
	}

	@Override
	public void contextDestroyed(ServletContextEvent sce) {
		GameScheduler.INSTANCE.stop();
		CouchDbUtil.shutdown();
		WebContainerProperties.INSTANCE.shutdown();
		EmailService.INSTANCE.shutdown();
	}

}
