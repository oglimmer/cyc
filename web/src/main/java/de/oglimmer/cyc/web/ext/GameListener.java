package de.oglimmer.cyc.web.ext;

import javax.servlet.ServletContextEvent;
import javax.servlet.ServletContextListener;
import javax.servlet.annotation.WebListener;

import org.apache.commons.lang.StringUtils;

import de.oglimmer.cyc.dao.couchdb.CouchDbUtil;
import de.oglimmer.cyc.mbean.CycStatistics;
import de.oglimmer.cyc.web.GameExecutor;

@WebListener
public class GameListener implements ServletContextListener {

	@Override
	public void contextInitialized(ServletContextEvent sce) {
		CycStatistics.INSTANCE.toString();
		String realPath = sce.getServletContext().getRealPath("/");
		GameExecutor.INSTANCE
				.setWarVersion(StringUtils.substringBefore(StringUtils.substringAfter(realPath, "##"), "/"));
	}

	@Override
	public void contextDestroyed(ServletContextEvent sce) {
		GameExecutor.INSTANCE.stop();
		CouchDbUtil.shutdown();
	}

}
