package de.oglimmer.cyc.web;

import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.ConnectException;
import java.net.Socket;
import java.net.UnknownHostException;
import java.util.Date;
import java.util.concurrent.TimeUnit;

import lombok.extern.slf4j.Slf4j;

@Slf4j
public enum GameExecutor {
	INSTANCE;

	public void runGame(String userId) throws IOException {
		if (!WebContainerProperties.INSTANCE.getSystemHaltDate().after(new Date())) {
			return;
		}
		Socket clientSocket = null;
		try {
			clientSocket = getClientSocket();
			DataOutputStream outToServer = new DataOutputStream(clientSocket.getOutputStream());
			BufferedReader inFromServer = new BufferedReader(new InputStreamReader(clientSocket.getInputStream()));
			if (userId == null) {
				userId = "full";
			}
			outToServer.writeBytes(userId + '\n');
			String serverResponse = inFromServer.readLine();
			if (!"ok".equals(serverResponse)) {
				log.error("Call to game server returned error:{}", serverResponse);
				throw new IOException("Server returned:" + serverResponse);
			}

		} finally {
			if (clientSocket != null) {
				try {
					clientSocket.close();
				} catch (IOException e) {
					log.debug("Failed to close to game-server", e);
				}
			}
		}
	}

	private synchronized Socket getClientSocket() throws IOException {
		try {
			return new Socket(WebContainerProperties.INSTANCE.getEngineHost(),
					WebContainerProperties.INSTANCE.getEnginePort());
		} catch (ConnectException e) {
			return startNewServerAndCreateSocket(e);
		}
	}

	private Socket startNewServerAndCreateSocket(ConnectException e) throws IOException, UnknownHostException {
		if (isLocalEngine()) {
			WebGameEngineStarter.INSTANCE.startServer();
			return connectToStartingEngine();
		} else {
			throw new RuntimeException(e);
		}
	}

	private Socket connectToStartingEngine() throws IOException, ConnectException {
		int retries = 0;
		while (true) {
			try {
				return new Socket(WebContainerProperties.INSTANCE.getEngineHost(),
						WebContainerProperties.INSTANCE.getEnginePort());
			} catch (ConnectException ce) {
				retries++;
				if (retries > 30) {
					throw ce;
				}
				try {
					TimeUnit.SECONDS.sleep(1);
				} catch (InterruptedException e) {
					throw new ConnectException("CONNECT ABORTED DUE TO INTERRUPT EXCEPTION");
				}
			}
		}
	}

	private boolean isLocalEngine() {
		String engineHost = WebContainerProperties.INSTANCE.getEngineHost();
		return "localhost".equalsIgnoreCase(engineHost) || "127.0.0.1".equals(engineHost);
	}
}
