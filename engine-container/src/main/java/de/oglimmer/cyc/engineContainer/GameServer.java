package de.oglimmer.cyc.engineContainer;

import java.io.IOException;

import de.oglimmer.cyc.mbean.CycStatistics;
import lombok.extern.slf4j.Slf4j;

@Slf4j
public class GameServer {

	private GameServer() {
		// no code here
	}

	public static void main(String[] args) {
		assert System.getProperty("cyc.home") != null;
		CycStatistics.INSTANCE.toString();
		armSecurityManager();
		startServer();
	}

	private static void startServer() {
		try (TcpHandler tcpHandler = new TcpHandler()) {
			tcpHandler.runServer();
		} catch (IOException | InterruptedException e) {
			log.error("Failed to start StartEngine", e);
		}
	}

	private static void armSecurityManager() {
		if ("disable".equalsIgnoreCase(System.getProperty("cyc.security", "enabled"))) {
			log.error("SecurityManager disbaled!");
		} else {
			System.setSecurityManager(new SecurityManager());
		}
	}

}
