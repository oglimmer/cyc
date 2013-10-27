package de.oglimmer.cyc;

import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.InetSocketAddress;
import java.net.ServerSocket;
import java.net.Socket;
import java.net.SocketException;
import java.util.Date;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import de.oglimmer.cyc.api.GroovyInitializer;

public class GameServer {

	/* used in security.policy as well */
	public static final int SERVER_PORT = 9998;

	private static Logger log = LoggerFactory.getLogger(GameServer.class);

	public static void main(String[] args) {

		if (!"disable".equals(System.getProperty("cyc.security"))) {
			System.setSecurityManager(new SecurityManager() {
				@Override
				public void checkExit(int status) {
					throw new RuntimeException("Exit not allowed");
				}
			});
		} else {
			log.info("No SecurityManager set!");
		}

		GroovyInitializer.init();

		try {
			GameServer gs = new GameServer();
			gs.runServer();
		} catch (IOException e) {
			e.printStackTrace();
			log.error("Failed to start GameServer", e);
		}
	}

	private boolean running;
	private ServerSocket serverSocket;
	private ThreadPoolExecutor tpe;
	private Date startTime;

	public GameServer() {
		tpe = new ThreadPoolExecutor(1, 1, 60, TimeUnit.SECONDS, new LinkedBlockingQueue<Runnable>());
	}

	public void runServer() throws IOException, SocketException {
		try {
			running = true;
			startTime = new Date();
			serverSocket = new ServerSocket();
			serverSocket.bind(new InetSocketAddress("127.0.0.1", SERVER_PORT));
			log.debug("Bind on 127.0.0.1:" + SERVER_PORT + " successful.");
			while (running) {
				new Thread(new SocketHandler(serverSocket.accept())).start();
			}
		} catch (SocketException e) {
			if (!"Socket closed".equals(e.getMessage())) {
				throw e;
			}
		} finally {
			running = false;
			log.debug("Server shutdown in progress...");
			tpe.shutdown();
			try {
				tpe.awaitTermination(1, TimeUnit.MINUTES);
			} catch (InterruptedException e) {
				// we dont care if we are interrupted
			}
			log.debug("Server shutdown completed.");
		}
	}

	class SocketHandler implements Runnable {

		private Socket connectionSocket;

		public SocketHandler(Socket connectionSocket) {
			this.connectionSocket = connectionSocket;
		}

		@Override
		public void run() {
			try {
				try (BufferedReader inFromClient = new BufferedReader(new InputStreamReader(
						connectionSocket.getInputStream()))) {
					try (DataOutputStream outToClient = new DataOutputStream(connectionSocket.getOutputStream())) {
						final String clientRequest = inFromClient.readLine();
						if ("exit".equals(clientRequest)) {
							handleExit(outToClient);
						} else if ("up".equals(clientRequest)) {
							handleUp(outToClient);
						} else if ("status".equals(clientRequest)) {
							handleStatus(outToClient);
						} else {
							handleRunGame(outToClient, clientRequest);
						}
					}
				}
			} catch (IOException e) {
				log.debug("Failed to process client connection", e);
			} finally {
				try {
					connectionSocket.close();
				} catch (IOException e) {
					log.debug("Failed to close server socket", e);
				}
			}
		}

		private void handleRunGame(DataOutputStream outToClient, final String clientRequest) throws IOException {
			log.debug("Received: " + clientRequest);

			tpe.submit(new Runnable() {
				@Override
				public void run() {
					try {
						if ("full".equals(clientRequest)) {
							GameRunStarter.INSTANCE.startFullGame();
						} else {
							GameRunStarter.INSTANCE.startCheckRun(clientRequest);
						}
					} catch (Throwable e) {
						log.error("Uncaught throwable", e);
					}
				}
			});
			outToClient.writeBytes("ok\n");
		}

		private void handleStatus(DataOutputStream outToClient) throws IOException {
			log.info("Running: {}", running);
			log.info("Queue-size: {}", tpe.getQueue().size());
			outToClient.writeBytes("Running: " + running + "\n");
			outToClient.writeBytes("Queue-size: " + tpe.getQueue().size() + "\n");
		}

		private void handleUp(DataOutputStream outToClient) throws IOException {
			log.info("Uptime: {}", startTime);
			outToClient.writeBytes("Uptime: " + startTime + "\n");
		}

		private void handleExit(DataOutputStream outToClient) throws IOException {
			running = false;
			serverSocket.close();
			outToClient.writeBytes("shutdown in progress\n");
		}
	}

}
