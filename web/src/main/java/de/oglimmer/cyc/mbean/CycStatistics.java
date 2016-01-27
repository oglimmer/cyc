package de.oglimmer.cyc.mbean;

import java.lang.management.ManagementFactory;
import java.util.Set;

import javax.management.InstanceAlreadyExistsException;
import javax.management.MBeanRegistrationException;
import javax.management.MBeanServer;
import javax.management.MalformedObjectNameException;
import javax.management.NotCompliantMBeanException;
import javax.management.ObjectInstance;
import javax.management.ObjectName;

import lombok.Getter;

public enum CycStatistics {
	INSTANCE;

	@Getter
	private GameRunStats mbean;

	CycStatistics() {
		try {
			MBeanServer server = ManagementFactory.getPlatformMBeanServer();

			String objectName = "de.oglimmer.cyc:type=GameRunStats";

			ObjectName mbeanName = new ObjectName(objectName);

			mbean = new GameRunStats();

			server.registerMBean(mbean, mbeanName);

			Set<ObjectInstance> instances = server.queryMBeans(new ObjectName(objectName), null);

			ObjectInstance instance = (ObjectInstance) instances.toArray()[0];
			System.out.println("Class Name:t" + instance.getClassName());
			System.out.println("Object Name:t" + instance.getObjectName());

		} catch (MalformedObjectNameException | InstanceAlreadyExistsException | MBeanRegistrationException
				| NotCompliantMBeanException e) {
			e.printStackTrace();
		}

	}

}
