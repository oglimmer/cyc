package de.oglimmer.cyc.engineContainer;

import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.InetSocketAddress;
import java.net.ServerSocket;
import java.net.Socket;
import java.net.SocketException;
import java.text.NumberFormat;
import java.util.Date;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class GameServer {
	/* used in security.policy as well & de.oglimmer.cyc.web.GameExecutor.getClientSocket() */
	public static final int SERVER_PORT = 9998;

	private static Logger log = LoggerFactory.getLogger(GameServer.class);

	public static void main(String[] args) throws Exception {
		try {
			GameServer gs = new GameServer();
			gs.runServer();
		} catch (IOException e) {
			e.printStackTrace();
			log.error("Failed to start StartEngine", e);
		}
	}

	private boolean running;
	private ServerSocket serverSocket;
	private ThreadPoolExecutor tpe;
	private Date startTime;
	private EngineLoader engineLoader = new EngineLoader();

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
							engineLoader.startGame(null);
						} else {
							engineLoader.startGame(clientRequest);
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
			NumberFormat nf = NumberFormat.getIntegerInstance();
			log.info("Memory(free/max/total): {}/{}/{}", nf.format(Runtime.getRuntime().freeMemory()),
					nf.format(Runtime.getRuntime().maxMemory()), nf.format(Runtime.getRuntime().totalMemory()));
			log.info("Current dir: {}", engineLoader.getBaseDir() + engineLoader.getCurrentDir());
			outToClient.writeBytes("Running: " + running + "\n");
			outToClient.writeBytes("Queue-size: " + tpe.getQueue().size() + "\n");
			outToClient.writeBytes("Memory(free/max/total): " + nf.format(Runtime.getRuntime().freeMemory()) + "/"
					+ nf.format(Runtime.getRuntime().maxMemory()) + "/" + nf.format(Runtime.getRuntime().totalMemory())
					+ "\n");
			outToClient.writeBytes("Current dir: " + engineLoader.getBaseDir() + engineLoader.getCurrentDir() + "\n");
		}

		private void handleUp(DataOutputStream outToClient) throws IOException {
			log.info("Uptime: {}", startTime);
			outToClient.writeBytes("Uptime: " + startTime + "\n");
		}

		private void handleExit(DataOutputStream outToClient) throws IOException {
			running = false;
			serverSocket.close();
			engineLoader.stop();
			outToClient.writeBytes("shutdown in progress\n");
		}

	}
}
