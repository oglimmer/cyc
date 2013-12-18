package de.oglimmer.cyc.engineContainer;

import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.lang.management.ManagementFactory;
import java.lang.management.MemoryPoolMXBean;
import java.lang.management.MemoryType;
import java.lang.management.MemoryUsage;
import java.lang.reflect.InvocationTargetException;
import java.net.InetSocketAddress;
import java.net.ServerSocket;
import java.net.Socket;
import java.net.SocketException;
import java.text.NumberFormat;
import java.util.Date;
import java.util.Iterator;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

import lombok.extern.slf4j.Slf4j;

@Slf4j
public class GameServer {
	/* used in security.policy as well & de.oglimmer.cyc.web.GameExecutor.getClientSocket() */
	public static final int SERVER_PORT = 9998;

	public static void main(String[] args) throws Exception {
		assert System.getProperty("cyc.home") != null;

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
	private EngineLoader engineLoader;

	public GameServer() {
		tpe = new ThreadPoolExecutor(1, 1, 60, TimeUnit.SECONDS, new LinkedBlockingQueue<Runnable>());
		if ("true".equalsIgnoreCase(System.getProperty("cyc.debug"))) {
			engineLoader = new DebugEngineLoader();
		} else {
			engineLoader = new EngineLoader();
		}
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
			tpe.shutdownNow();
			try {
				tpe.awaitTermination(2, TimeUnit.MINUTES);
			} catch (InterruptedException e) {
				log.error("Shutdown interrupted", e);
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
						} else if ("status".equals(clientRequest)) {
							handleStatus(outToClient, false);
						} else if ("extstatus".equals(clientRequest)) {
							handleStatus(outToClient, true);
						} else if ("version".equals(clientRequest)) {
							handleVersion(outToClient);
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

		private void handleVersion(DataOutputStream outToClient) throws IOException {
			try {
				String version = engineLoader.getVersion();
				log.info("Version: {}", version);
				outToClient.writeBytes("Version: " + version + "\n");
			} catch (IllegalAccessException | IllegalArgumentException | InvocationTargetException
					| NoSuchMethodException | SecurityException | InstantiationException | ClassNotFoundException e) {
				log.error("Failed to get version from engine", e);
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
					} catch (InvocationTargetException e) {
						if (!(e.getCause() instanceof InterruptedException)) {
							log.error("Uncaught throwable", e);
						}
					} catch (Throwable e) {
						log.error("Uncaught throwable", e);
					}
				}

			});
			outToClient.writeBytes("ok\n");
		}

		private void handleStatus(DataOutputStream outToClient, boolean extended) throws IOException {
			boolean lRunning = running;
			int queueSize = tpe.getQueue().size();
			int active = tpe.getActiveCount();
			NumberFormat nf = NumberFormat.getIntegerInstance();
			String freeMem = nf.format(Runtime.getRuntime().freeMemory());
			String maxMem = nf.format(Runtime.getRuntime().maxMemory());
			String totalMem = nf.format(Runtime.getRuntime().totalMemory());
			String curDir = engineLoader.getBaseDir() + engineLoader.getCurrentDir();
			log.info("Running: {}", lRunning);
			log.info("Queue-size: {}", queueSize);
			log.info("Active: {}", active);
			log.info("Current dir: {}", curDir);
			log.info("Uptime: {}", startTime);
			log.info("Memory(free/total/max): {}/{}/{}", freeMem, totalMem, maxMem);
			outToClient.writeBytes("Running: " + lRunning + "\n");
			outToClient.writeBytes("Queue-size: " + queueSize + "\n");
			outToClient.writeBytes("Active: " + active + "\n");
			outToClient.writeBytes("Current dir: " + curDir + "\n");
			outToClient.writeBytes("Uptime: " + startTime + "\n");
			outToClient.writeBytes("Memory(free/total/max): " + freeMem + "/" + totalMem + "/" + maxMem + "\n");
			if (extended) {
				Iterator<MemoryPoolMXBean> iter = ManagementFactory.getMemoryPoolMXBeans().iterator();
				while (iter.hasNext()) {
					MemoryPoolMXBean item = iter.next();
					String name = item.getName();
					MemoryType type = item.getType();
					MemoryUsage usage = item.getUsage();
					MemoryUsage peak = item.getPeakUsage();
					MemoryUsage collections = item.getCollectionUsage();
					log.debug("{} ({})", name, type);
					outToClient.writeBytes(name + " (" + type + ")\n");
					log.debug("Usage:{}", usage.toString());
					outToClient.writeBytes("Usage:" + usage + "\n");
					log.debug("Peak:{}", peak.toString());
					outToClient.writeBytes("Peak:" + peak + "\n");
					if (collections != null) {
						log.debug("Collections:{}", collections.toString());
						outToClient.writeBytes("Collections:" + collections + "\n");
					}
				}
			}
		}

		private void handleExit(DataOutputStream outToClient) throws IOException {
			running = false;
			serverSocket.close();
			engineLoader.stop();
			outToClient.writeBytes("shutdown in progress\n");
		}

	}
}
