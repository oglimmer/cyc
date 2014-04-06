package de.oglimmer.cyc.web;

import java.lang.management.ManagementFactory;

import javax.management.MBeanServer;
import javax.management.ObjectName;
import javax.management.StandardMBean;

import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;

/**
 * This class works beyond classloader boundaries. So only one instance of GlobalGameExecutor in the JVM can be the
 * master, all others (in other classloaders) are master=false
 * 
 * @author oli
 */
@Slf4j
public enum GlobalGameExecutor {
	INSTANCE;

	private static final String MBEAN_NAME = "de.oglimmer.cyc:type=GameScheduler";

	private boolean isMaster;

	@SneakyThrows
	public boolean isMaster() {
		if (isMaster) {
			log.debug("Running as Master (1, {}): true", GameExecutor.INSTANCE.getWarVersion());
			return true;
		}
		ObjectName oname = new javax.management.ObjectName(GlobalGameExecutor.MBEAN_NAME);
		MBeanServer mbs = ManagementFactory.getPlatformMBeanServer();
		synchronized (Object.class) {
			if (!mbs.isRegistered(oname)) {
				IDummy mbean = new DummyCls();
				StandardMBean standardmbean = new StandardMBean(mbean, IDummy.class);
				mbs.registerMBean(standardmbean, oname);
				isMaster = true;
				log.debug("Created Master ({})", GameExecutor.INSTANCE.getWarVersion());
			}
		}
		log.debug("Running as Master (2, {}): {}", GameExecutor.INSTANCE.getWarVersion(), isMaster);
		return isMaster;
	}

	@SneakyThrows
	public void close() {
		if (isMaster) {
			MBeanServer mbs = ManagementFactory.getPlatformMBeanServer();
			ObjectName name = new ObjectName(GlobalGameExecutor.MBEAN_NAME);
			mbs.unregisterMBean(name);
			log.debug("Closed Master ({})", GameExecutor.INSTANCE.getWarVersion());
		}
	}

	class DummyCls implements IDummy {
		// no code
	}
}
