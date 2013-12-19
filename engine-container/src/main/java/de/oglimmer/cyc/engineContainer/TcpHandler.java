package de.oglimmer.cyc.engineContainer;

import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.nio.NioServerSocketChannel;

import java.io.Closeable;
import java.io.IOException;
import java.lang.management.ManagementFactory;
import java.lang.management.MemoryPoolMXBean;
import java.lang.management.MemoryType;
import java.lang.management.MemoryUsage;
import java.lang.reflect.InvocationTargetException;
import java.text.NumberFormat;
import java.util.Date;
import java.util.Iterator;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;

@Slf4j
public class TcpHandler implements Closeable {

	/* used in security.policy as well & de.oglimmer.cyc.web.GameExecutor.getClientSocket() */
	public static final int SERVER_PORT = 9998;

	private ThreadPoolExecutor tpe;
	private Date startTime;
	private EngineLoader engineLoader;
	private EventLoopGroup bossGroup;
	private EventLoopGroup workerGroup;

	public TcpHandler() {
		bossGroup = new NioEventLoopGroup();
		workerGroup = new NioEventLoopGroup();
		tpe = new ThreadPoolExecutor(1, 1, 60, TimeUnit.SECONDS, new LinkedBlockingQueue<Runnable>());
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

		log.debug("Bind on 127.0.0.1:" + SERVER_PORT);
		b.bind(SERVER_PORT).sync().channel().closeFuture().sync();

		log.debug("Server shutdown in progress...");
	}

	@SneakyThrows
	public String handleVersion() throws IOException {
		String version = engineLoader.getVersion();
		log.info("Version: {}", version);
		return "Version: " + version + "\n";
	}

	public String handleRunGame(final String clientRequest) throws IOException {
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
		return "ok\n";
	}

	public String handleStatus(boolean extended) throws IOException {
		StringBuilder buff = new StringBuilder();
		int queueSize = tpe.getQueue().size();
		int active = tpe.getActiveCount();
		NumberFormat nf = NumberFormat.getIntegerInstance();
		String freeMem = nf.format(Runtime.getRuntime().freeMemory());
		String maxMem = nf.format(Runtime.getRuntime().maxMemory());
		String totalMem = nf.format(Runtime.getRuntime().totalMemory());
		String curDir = engineLoader.getBaseDir() + engineLoader.getCurrentDir();
		log.info("Queue-size: {}", queueSize);
		log.info("Active: {}", active);
		log.info("Current dir: {}", curDir);
		log.info("Uptime: {}", startTime);
		log.info("Memory(free/total/max): {}/{}/{}", freeMem, totalMem, maxMem);
		buff.append("Queue-size: " + queueSize + "\n");
		buff.append("Active: " + active + "\n");
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
				log.debug("{} ({})", name, type);
				buff.append(name + " (" + type + ")\n");
				log.debug("Usage:{}", usage.toString());
				buff.append("Usage:" + usage + "\n");
				log.debug("Peak:{}", peak.toString());
				buff.append("Peak:" + peak + "\n");
				if (collections != null) {
					log.debug("Collections:{}", collections.toString());
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

		engineLoader.stop();
	}
}
