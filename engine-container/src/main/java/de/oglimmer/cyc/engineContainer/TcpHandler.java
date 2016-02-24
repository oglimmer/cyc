package de.oglimmer.cyc.engineContainer;

import java.io.Closeable;
import java.io.IOException;
import java.lang.management.ManagementFactory;
import java.lang.management.MemoryPoolMXBean;
import java.lang.management.MemoryType;
import java.lang.management.MemoryUsage;
import java.lang.reflect.InvocationTargetException;
import java.text.NumberFormat;
import java.util.Collections;
import java.util.Date;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Set;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

import com.google.common.util.concurrent.RateLimiter;

import de.oglimmer.cyc.util.NamedThreadFactory;
import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.nio.NioServerSocketChannel;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;

@Slf4j
public class TcpHandler implements Closeable {

	/* used in security.policy as well & de.oglimmer.cyc.web.GameExecutor.getClientSocket() */
	public static final int SERVER_PORT = 9998;

	@Getter
	private ThreadPoolExecutor tpeTestRun;
	@Getter
	private ThreadPoolExecutor tpeFullRun;
	private Date startTime;
	private EngineLoader engineLoader;
	private EventLoopGroup bossGroup;
	private EventLoopGroup workerGroup;
	private Set<String> runningClientIds = Collections.synchronizedSet(new HashSet<>());
	private RateLimiter rateLimiter = RateLimiter.create(EngineContainerProperties.INSTANCE.getMaxRateTestRuns());

	public TcpHandler() {
		startTime = new Date();
		bossGroup = new NioEventLoopGroup();
		workerGroup = new NioEventLoopGroup();

		tpeTestRun = new ThreadPoolExecutor(1, 3, 60, TimeUnit.HOURS, new LinkedBlockingQueue<Runnable>(),
				new NamedThreadFactory("TestRun"));
		tpeFullRun = new ThreadPoolExecutor(1, 1, 60, TimeUnit.HOURS, new LinkedBlockingQueue<Runnable>(),
				new NamedThreadFactory("FullRun"));
		if ("true".equalsIgnoreCase(System.getProperty("cyc.debug"))) {
			engineLoader = new DebugEngineLoader();
		} else {
			engineLoader = new EngineLoader();
		}
	}

	public void runServer() throws IOException, InterruptedException {

		ServerBootstrap b = new ServerBootstrap();
		b.group(bossGroup, workerGroup).channel(NioServerSocketChannel.class)
				.childHandler(new GameServerInitializer(this));

		log.debug("Bind on {}:{}", EngineContainerProperties.INSTANCE.getBindAddress(), SERVER_PORT);
		b.bind(EngineContainerProperties.INSTANCE.getBindAddress(), SERVER_PORT).sync().channel().closeFuture().sync();

		log.debug("Server shutdown in progress...");
	}

	@SneakyThrows(value = InvocationTargetException.class)
	public String handleVersion() {
		String version = engineLoader.getVersion();
		return "Version: " + version + "\n";
	}

	public String handleRunGame(final String clientRequest) {
		log.debug("Received: " + clientRequest);

		ThreadPoolExecutor executor = getExecutor(clientRequest);
		if (!isFullRun(clientRequest)) {
			if (runningClientIds.contains(clientRequest)) {
				return "alreadyInQueue\n";
			}
			if (!rateLimiter.tryAcquire()) {
				return "tooFast\n";
			}
			runningClientIds.add(clientRequest);
		}
		executor.submit(new GameExecutionRunnable(clientRequest));
		return "ok\n";
	}

	private ThreadPoolExecutor getExecutor(final String clientRequest) {
		if (isFullRun(clientRequest)) {
			return tpeFullRun;
		} else {
			return tpeTestRun;
		}
	}

	private boolean isFullRun(final String clientRequest) {
		return "full".equals(clientRequest);
	}

	public String handleStatus(boolean extended) {
		StringBuilder buff = new StringBuilder();
		int queueSizeTestRun = tpeTestRun.getQueue().size();
		int activeTestRun = tpeTestRun.getActiveCount();
		int queueSizeFullRun = tpeFullRun.getQueue().size();
		int activeFullRun = tpeFullRun.getActiveCount();
		NumberFormat nf = NumberFormat.getIntegerInstance();
		String freeMem = nf.format(Runtime.getRuntime().freeMemory());
		String maxMem = nf.format(Runtime.getRuntime().maxMemory());
		String totalMem = nf.format(Runtime.getRuntime().totalMemory());
		String curDir = engineLoader.getBaseDir() + engineLoader.getCurrentDir();
		buff.append("Queue-size(test): " + queueSizeTestRun + "\n");
		buff.append("Active(test): " + activeTestRun + "\n");
		buff.append("Queue-size(full): " + queueSizeFullRun + "\n");
		buff.append("Active(full): " + activeFullRun + "\n");
		buff.append("Current dir: " + curDir + "\n");
		buff.append("Uptime: " + startTime + "\n");
		buff.append("Memory(free/total/max): " + freeMem + "/" + totalMem + "/" + maxMem + "\n");
		if (extended) {
			Iterator<MemoryPoolMXBean> iter = ManagementFactory.getMemoryPoolMXBeans().iterator();
			while (iter.hasNext()) {
				MemoryPoolMXBean item = iter.next();
				String name = item.getName();
				MemoryType type = item.getType();
				MemoryUsage usage = item.getUsage();
				MemoryUsage peak = item.getPeakUsage();
				MemoryUsage collections = item.getCollectionUsage();
				buff.append(name + " (" + type + ")\n");
				buff.append("Usage:" + usage + "\n");
				buff.append("Peak:" + peak + "\n");
				if (collections != null) {
					buff.append("Collections:" + collections + "\n");
				}
			}
		}
		return buff.toString();
	}

	public String handleExit() {
		close();
		return "shutdown in progress\n";
	}

	@Override
	public void close() {
		bossGroup.shutdownGracefully();
		workerGroup.shutdownGracefully();
		tpeTestRun.shutdown();
		tpeFullRun.shutdown();
		engineLoader.stop();
	}

	@AllArgsConstructor
	class GameExecutionRunnable implements Runnable {
		@Getter
		private String clientRequest;

		@Override
		public void run() {
			try {
				if (isFullRun(clientRequest)) {
					engineLoader.startGame(null);
				} else {
					engineLoader.startGame(clientRequest);
				}
			} catch (InvocationTargetException e) {
				if (!(e.getCause() instanceof InterruptedException)) {
					log.error("Uncaught Exception", e);
				}
			} catch (Exception e) {
				log.error("Uncaught Exception", e);
			} finally {
				runningClientIds.remove(clientRequest);
			}
		}
	}
}
