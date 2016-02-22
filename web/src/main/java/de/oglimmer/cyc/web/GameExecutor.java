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

	public void startFullRun() throws IOException {
		startRun("full");
	}
	
	public void startTestRun(String userId) throws IOException {
		startRun(userId);
	}
	
	private void startRun(String userId) throws IOException {
		if (!WebContainerProperties.INSTANCE.getSystemHaltDate().after(new Date())) {
			return;
		}
		Socket clientSocket = null;
		try {
			clientSocket = getClientSocket(userId);
			DataOutputStream outToServer = new DataOutputStream(clientSocket.getOutputStream());
			BufferedReader inFromServer = new BufferedReader(new InputStreamReader(clientSocket.getInputStream()));
			String enginePassword = WebContainerProperties.INSTANCE.getEnginePassword();
			if (enginePassword != null && !enginePassword.isEmpty()) {
				outToServer.writeBytes("Authorization:" + enginePassword + ';');
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

	private synchronized Socket getClientSocket(String userId) throws IOException {
		try {
			return new Socket(getEngineHost(userId), getEnginePort(userId));
		} catch (ConnectException e) {
			return startNewServerAndCreateSocket(e, userId);
		}
	}

	private Socket startNewServerAndCreateSocket(ConnectException e, String userId) throws IOException, UnknownHostException {
		if (isLocalEngine(userId)) {
			WebGameEngineStarter.INSTANCE.startServer();
			return connectToStartingEngine(userId);
		} else {
			throw new RuntimeException(e);
		}
	}

	private Socket connectToStartingEngine(String userId) throws IOException, ConnectException {
		int retries = 0;
		while (true) {
			try {
				return new Socket(getEngineHost(userId), getEnginePort(userId));
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

	private boolean isLocalEngine(String userId) {
		String engineHost = getEngineHost(userId);
		return "localhost".equalsIgnoreCase(engineHost) || "127.0.0.1".equals(engineHost);
	}
	
	private String getEngineHost(String userId) {
		return "full".equalsIgnoreCase(userId) ? WebContainerProperties.INSTANCE.getFullEngineHost()
				: WebContainerProperties.INSTANCE.getTestEngineHost();
	}

	private int getEnginePort(String userId) {
		return "full".equalsIgnoreCase(userId) ? WebContainerProperties.INSTANCE.getFullEnginePort()
				: WebContainerProperties.INSTANCE.getTestEnginePort();
	}
}
