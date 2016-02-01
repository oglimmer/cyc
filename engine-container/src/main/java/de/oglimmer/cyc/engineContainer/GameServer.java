package de.oglimmer.cyc.engineContainer;

import java.io.IOException;

import de.oglimmer.cyc.mbean.CycStatistics;
import lombok.Getter;
import lombok.extern.slf4j.Slf4j;

@Slf4j
public class GameServer {
	
	@Getter
	private static TcpHandler tcpHandler;

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
		tcpHandler = new TcpHandler();
		try {
			tcpHandler.runServer();
		} catch (IOException | InterruptedException e) {
			log.error("Failed to start StartEngine", e);
		} finally {
			tcpHandler.close();
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
