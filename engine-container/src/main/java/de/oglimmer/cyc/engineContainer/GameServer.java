package de.oglimmer.cyc.engineContainer;

import java.io.IOException;

import lombok.extern.slf4j.Slf4j;

@Slf4j
public class GameServer {

	public static void main(String[] args) {
		assert System.getProperty("cyc.home") != null;

		try (TcpHandler tcpHandler = new TcpHandler()) {
			tcpHandler.runServer();
		} catch (IOException | InterruptedException e) {
			e.printStackTrace();
			log.error("Failed to start StartEngine", e);
		}
	}

}
