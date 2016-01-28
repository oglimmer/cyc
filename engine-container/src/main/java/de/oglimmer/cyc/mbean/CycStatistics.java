package de.oglimmer.cyc.mbean;

import java.lang.management.ManagementFactory;

import javax.management.InstanceAlreadyExistsException;
import javax.management.MBeanRegistrationException;
import javax.management.MBeanServer;
import javax.management.MalformedObjectNameException;
import javax.management.NotCompliantMBeanException;
import javax.management.ObjectName;

import lombok.Getter;
import lombok.SneakyThrows;

public enum CycStatistics {
	INSTANCE;

	@Getter
	private GameRunStatsMBean mbean;

	@SneakyThrows(value = { MalformedObjectNameException.class, InstanceAlreadyExistsException.class,
			MBeanRegistrationException.class, NotCompliantMBeanException.class })
	CycStatistics() {
		MBeanServer server = ManagementFactory.getPlatformMBeanServer();
		ObjectName mbeanName = new ObjectName("de.oglimmer.cyc:type=GameRunStats");
		mbean = new GameRunStats();
		server.registerMBean(mbean, mbeanName);
	}

}
