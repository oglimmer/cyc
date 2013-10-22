package de.oglimmer.cyc.web.ext;

import javax.servlet.ServletContextEvent;
import javax.servlet.ServletContextListener;
import javax.servlet.annotation.WebListener;

import de.oglimmer.cyc.dao.couchdb.CouchDbUtil;
import de.oglimmer.cyc.web.GameExecutor;

@WebListener
public class GameListener implements ServletContextListener {

	@Override
	public void contextInitialized(ServletContextEvent sce) {
		GameExecutor.INSTANCE.setRootPath(sce.getServletContext().getRealPath("/"));
	}

	@Override
	public void contextDestroyed(ServletContextEvent sce) {
		GameExecutor.INSTANCE.stop();
		CouchDbUtil.shutdown();
	}

}
