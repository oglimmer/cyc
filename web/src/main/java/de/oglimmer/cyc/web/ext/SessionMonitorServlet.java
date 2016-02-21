package de.oglimmer.cyc.web.ext;

import java.beans.PropertyChangeListener;
import java.io.IOException;
import java.util.Enumeration;
import java.util.Set;

import javax.management.ObjectName;
import javax.naming.directory.DirContext;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.apache.catalina.AccessLog;
import org.apache.catalina.Cluster;
import org.apache.catalina.Container;
import org.apache.catalina.ContainerListener;
import org.apache.catalina.LifecycleException;
import org.apache.catalina.LifecycleListener;
import org.apache.catalina.LifecycleState;
import org.apache.catalina.Loader;
import org.apache.catalina.Manager;
import org.apache.catalina.Pipeline;
import org.apache.catalina.Realm;
import org.apache.catalina.Session;
import org.apache.catalina.connector.Request;
import org.apache.catalina.connector.Response;
import org.apache.juli.logging.Log;

import com.radiadesign.catalina.session.JavaSerializer;
import com.radiadesign.catalina.session.RedisSession;

import de.oglimmer.cyc.dao.UserDao;
import de.oglimmer.cyc.dao.couchdb.CouchDbUtil;
import de.oglimmer.cyc.dao.couchdb.UserCouchDb;
import de.oglimmer.cyc.model.User;
import lombok.extern.slf4j.Slf4j;
import redis.clients.jedis.Jedis;

@Slf4j
@WebServlet(urlPatterns = "/SessionMonitor")
public class SessionMonitorServlet extends HttpServlet {

	private static final long serialVersionUID = 1L;

	private UserDao userDao = new UserCouchDb(CouchDbUtil.getDatabase());

	@Override
	protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
		resp.setContentType("text/html");
		resp.getWriter().write("<html><body><h1>SessionMonitor (redis based + couchdb lookup)</h1>");
		HttpSession httpSession = req.getSession(false);
		if (httpSession != null && httpSession.getAttribute("userid") != null) {
			resp.getWriter()
					.write("<div>You're logged in. It is not allowed to view this page, while logged in.</div>");
		} else {
			JavaSerializer javaSerializer = new JavaSerializer();
			try (Jedis jedis = new Jedis("localhost")) {
				Set<String> allKeysInRedis = jedis.keys("*");
				resp.getWriter().write("<div>Total number of KEYS " + allKeysInRedis.size() + "</div>");
				for (String redisKey : allKeysInRedis) {
					if (!redisKey.startsWith("key") && !redisKey.startsWith("counter")
							&& !redisKey.startsWith("mylist")) {
						resp.getWriter().write("<div style='border:1px solid black'>");
						Long ttl = jedis.ttl(redisKey);
						resp.getWriter().write("TTL=" + ttl + "<br/>");
						byte[] sessionAsBytes = jedis.get(redisKey.getBytes());
						RedisSession redisSession = new RedisSession(new FakeManager());
						try {
							javaSerializer.deserializeInto(sessionAsBytes, redisSession);
						} catch (ClassNotFoundException | IOException e) {
							log.error("Failed to deserializeInto", e);
						}
						for (Enumeration<String> attEnum = redisSession.getAttributeNames(); attEnum
								.hasMoreElements();) {
							String key = attEnum.nextElement();
							resp.getWriter().write(key + "=" + redisSession.getAttribute(key) + "<br/>");
						}
						if (!redisSession.getAttributeNames().hasMoreElements()) {
							log.error("Failed to get any data for redis key {}", redisKey);
						}
						Object userid = redisSession.getAttribute("userid");
						if (userid != null && userid instanceof String) {
							User user = userDao.get((String) userid);
							resp.getWriter().write("LastCodeChangeDate=" + user.getLastCodeChangeDate() + "<br/>");
							resp.getWriter().write("LastLoginDate=" + user.getLastLoginDate() + "<br/>");
							resp.getWriter().write("LastPrivateRun=" + user.getLastPrivateRun() + "<br/>");
						} else {
							log.error("Failed to get userid for redis key {}", redisKey);
						}
						resp.getWriter().write("</div>");
					}
				}
			}
		}
		resp.getWriter().write("</body></html>");
	}

	class FakeManager implements Manager {

		@Override
		public boolean willAttributeDistribute(String name, Object value) {
			return true;
		}

		@Override
		public void unload() throws IOException {
			// TODO Auto-generated method stub

		}

		@Override
		public void setSessionMaxAliveTime(int sessionMaxAliveTime) {
			// TODO Auto-generated method stub

		}

		@Override
		public void setSessionIdLength(int idLength) {
			// TODO Auto-generated method stub

		}

		@Override
		public void setSessionCounter(long sessionCounter) {
			// TODO Auto-generated method stub

		}

		@Override
		public void setMaxInactiveInterval(int interval) {
			// TODO Auto-generated method stub

		}

		@Override
		public void setMaxActive(int maxActive) {
			// TODO Auto-generated method stub

		}

		@Override
		public void setExpiredSessions(long expiredSessions) {
			// TODO Auto-generated method stub

		}

		@Override
		public void setDistributable(boolean distributable) {
			// TODO Auto-generated method stub

		}

		@Override
		public void setContainer(Container container) {
			// TODO Auto-generated method stub

		}

		@Override
		public void removePropertyChangeListener(PropertyChangeListener listener) {
			// TODO Auto-generated method stub

		}

		@Override
		public void remove(Session session, boolean update) {
			// TODO Auto-generated method stub

		}

		@Override
		public void remove(Session session) {
			// TODO Auto-generated method stub

		}

		@Override
		public void load() throws ClassNotFoundException, IOException {
			// TODO Auto-generated method stub

		}

		@Override
		public int getSessionMaxAliveTime() {
			// TODO Auto-generated method stub
			return 0;
		}

		@Override
		public int getSessionIdLength() {
			// TODO Auto-generated method stub
			return 0;
		}

		@Override
		public int getSessionExpireRate() {
			// TODO Auto-generated method stub
			return 0;
		}

		@Override
		public int getSessionCreateRate() {
			// TODO Auto-generated method stub
			return 0;
		}

		@Override
		public long getSessionCounter() {
			// TODO Auto-generated method stub
			return 0;
		}

		@Override
		public int getSessionAverageAliveTime() {
			// TODO Auto-generated method stub
			return 0;
		}

		@Override
		public int getRejectedSessions() {
			// TODO Auto-generated method stub
			return 0;
		}

		@Override
		public int getMaxInactiveInterval() {
			// TODO Auto-generated method stub
			return 0;
		}

		@Override
		public int getMaxActive() {
			// TODO Auto-generated method stub
			return 0;
		}

		@Override
		public String getInfo() {
			// TODO Auto-generated method stub
			return null;
		}

		@Override
		public long getExpiredSessions() {
			// TODO Auto-generated method stub
			return 0;
		}

		@Override
		public boolean getDistributable() {
			// TODO Auto-generated method stub
			return false;
		}

		@Override
		public Container getContainer() {
			return new Container() {

				@Override
				public void stop() throws LifecycleException {
					// TODO Auto-generated method stub

				}

				@Override
				public void start() throws LifecycleException {
					// TODO Auto-generated method stub

				}

				@Override
				public void removeLifecycleListener(LifecycleListener listener) {
					// TODO Auto-generated method stub

				}

				@Override
				public void init() throws LifecycleException {
					// TODO Auto-generated method stub

				}

				@Override
				public String getStateName() {
					// TODO Auto-generated method stub
					return null;
				}

				@Override
				public LifecycleState getState() {
					// TODO Auto-generated method stub
					return null;
				}

				@Override
				public LifecycleListener[] findLifecycleListeners() {
					// TODO Auto-generated method stub
					return null;
				}

				@Override
				public void destroy() throws LifecycleException {
					// TODO Auto-generated method stub

				}

				@Override
				public void addLifecycleListener(LifecycleListener listener) {
					// TODO Auto-generated method stub

				}

				@Override
				public void setStartStopThreads(int startStopThreads) {
					// TODO Auto-generated method stub

				}

				@Override
				public void setResources(DirContext resources) {
					// TODO Auto-generated method stub

				}

				@Override
				public void setRealm(Realm realm) {
					// TODO Auto-generated method stub

				}

				@Override
				public void setParentClassLoader(ClassLoader parent) {
					// TODO Auto-generated method stub

				}

				@Override
				public void setParent(Container container) {
					// TODO Auto-generated method stub

				}

				@Override
				public void setName(String name) {
					// TODO Auto-generated method stub

				}

				@Override
				public void setManager(Manager manager) {
					// TODO Auto-generated method stub

				}

				@Override
				public void setLoader(Loader loader) {
					// TODO Auto-generated method stub

				}

				@Override
				public void setCluster(Cluster cluster) {
					// TODO Auto-generated method stub

				}

				@Override
				public void setBackgroundProcessorDelay(int delay) {
					// TODO Auto-generated method stub

				}

				@Override
				public void removePropertyChangeListener(PropertyChangeListener listener) {
					// TODO Auto-generated method stub

				}

				@Override
				public void removeContainerListener(ContainerListener listener) {
					// TODO Auto-generated method stub

				}

				@Override
				public void removeChild(Container child) {
					// TODO Auto-generated method stub

				}

				@Override
				public void logAccess(Request request, Response response, long time, boolean useDefault) {
					// TODO Auto-generated method stub

				}

				@Override
				public void invoke(Request request, Response response) throws IOException, ServletException {
					// TODO Auto-generated method stub

				}

				@Override
				public int getStartStopThreads() {
					// TODO Auto-generated method stub
					return 0;
				}

				@Override
				public DirContext getResources() {
					// TODO Auto-generated method stub
					return null;
				}

				@Override
				public Realm getRealm() {
					// TODO Auto-generated method stub
					return null;
				}

				@Override
				public Pipeline getPipeline() {
					// TODO Auto-generated method stub
					return null;
				}

				@Override
				public ClassLoader getParentClassLoader() {
					// TODO Auto-generated method stub
					return null;
				}

				@Override
				public Container getParent() {
					// TODO Auto-generated method stub
					return null;
				}

				@Override
				public ObjectName getObjectName() {
					// TODO Auto-generated method stub
					return null;
				}

				@Override
				public String getName() {
					// TODO Auto-generated method stub
					return null;
				}

				@Override
				public Object getMappingObject() {
					// TODO Auto-generated method stub
					return null;
				}

				@Override
				public Manager getManager() {
					// TODO Auto-generated method stub
					return null;
				}

				@Override
				public Log getLogger() {
					// TODO Auto-generated method stub
					return new Log() {

						@Override
						public void warn(Object message, Throwable t) {
							// TODO Auto-generated method stub

						}

						@Override
						public void warn(Object message) {
							// TODO Auto-generated method stub

						}

						@Override
						public void trace(Object message, Throwable t) {
							// TODO Auto-generated method stub

						}

						@Override
						public void trace(Object message) {
							// TODO Auto-generated method stub

						}

						@Override
						public boolean isWarnEnabled() {
							// TODO Auto-generated method stub
							return false;
						}

						@Override
						public boolean isTraceEnabled() {
							// TODO Auto-generated method stub
							return false;
						}

						@Override
						public boolean isInfoEnabled() {
							// TODO Auto-generated method stub
							return false;
						}

						@Override
						public boolean isFatalEnabled() {
							// TODO Auto-generated method stub
							return false;
						}

						@Override
						public boolean isErrorEnabled() {
							// TODO Auto-generated method stub
							return false;
						}

						@Override
						public boolean isDebugEnabled() {
							// TODO Auto-generated method stub
							return false;
						}

						@Override
						public void info(Object message, Throwable t) {
							// TODO Auto-generated method stub

						}

						@Override
						public void info(Object message) {
							// TODO Auto-generated method stub

						}

						@Override
						public void fatal(Object message, Throwable t) {
							// TODO Auto-generated method stub

						}

						@Override
						public void fatal(Object message) {
							// TODO Auto-generated method stub

						}

						@Override
						public void error(Object message, Throwable t) {
							// TODO Auto-generated method stub

						}

						@Override
						public void error(Object message) {
							// TODO Auto-generated method stub

						}

						@Override
						public void debug(Object message, Throwable t) {
							// TODO Auto-generated method stub

						}

						@Override
						public void debug(Object message) {
							// TODO Auto-generated method stub

						}
					};
				}

				@Override
				public Loader getLoader() {
					// TODO Auto-generated method stub
					return null;
				}

				@Override
				public String getInfo() {
					// TODO Auto-generated method stub
					return null;
				}

				@Override
				public Cluster getCluster() {
					// TODO Auto-generated method stub
					return null;
				}

				@Override
				public int getBackgroundProcessorDelay() {
					// TODO Auto-generated method stub
					return 0;
				}

				@Override
				public AccessLog getAccessLog() {
					// TODO Auto-generated method stub
					return null;
				}

				@Override
				public void fireContainerEvent(String type, Object data) {
					// TODO Auto-generated method stub

				}

				@Override
				public ContainerListener[] findContainerListeners() {
					// TODO Auto-generated method stub
					return null;
				}

				@Override
				public Container[] findChildren() {
					// TODO Auto-generated method stub
					return null;
				}

				@Override
				public Container findChild(String name) {
					// TODO Auto-generated method stub
					return null;
				}

				@Override
				public void backgroundProcess() {
					// TODO Auto-generated method stub

				}

				@Override
				public void addPropertyChangeListener(PropertyChangeListener listener) {
					// TODO Auto-generated method stub

				}

				@Override
				public void addContainerListener(ContainerListener listener) {
					// TODO Auto-generated method stub

				}

				@Override
				public void addChild(Container child) {
					// TODO Auto-generated method stub

				}
			};
		}

		@Override
		public int getActiveSessions() {
			// TODO Auto-generated method stub
			return 0;
		}

		@Override
		public Session[] findSessions() {
			// TODO Auto-generated method stub
			return null;
		}

		@Override
		public Session findSession(String id) throws IOException {
			// TODO Auto-generated method stub
			return null;
		}

		@Override
		public Session createSession(String sessionId) {
			// TODO Auto-generated method stub
			return null;
		}

		@Override
		public Session createEmptySession() {
			// TODO Auto-generated method stub
			return null;
		}

		@Override
		public void changeSessionId(Session session) {
			// TODO Auto-generated method stub

		}

		@Override
		public void backgroundProcess() {
			// TODO Auto-generated method stub

		}

		@Override
		public void addPropertyChangeListener(PropertyChangeListener listener) {
			// TODO Auto-generated method stub

		}

		@Override
		public void add(Session session) {
			// TODO Auto-generated method stub

		}
	};
}
