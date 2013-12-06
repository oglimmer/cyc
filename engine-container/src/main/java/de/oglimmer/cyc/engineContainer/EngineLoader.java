package de.oglimmer.cyc.engineContainer;

import java.io.File;
import java.io.FilenameFilter;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.concurrent.TimeUnit;

import lombok.extern.slf4j.Slf4j;

import org.xeustechnologies.jcl.JarClassLoader;
import org.xeustechnologies.jcl.JclObjectFactory;

@Slf4j
public class EngineLoader {

	protected String currentDir = null;
	protected String baseDir;
	private Thread dirScannerThread;
	private boolean running;
	private JarClassLoader jcl;

	public EngineLoader() {
		init();
	}

	protected void init() {
		baseDir = System.getProperty("cyc.home", ".");
		if (!baseDir.endsWith("/")) {
			baseDir += "/";
		}
		log.debug("Using baseDir={}", baseDir);
		findLatestDir();
		dirScannerThread = new Thread(new DirectoryScanner());
		dirScannerThread.start();
	}

	public String getCurrentDir() {
		return currentDir;
	}

	public String getBaseDir() {
		return baseDir;
	}

	public void startGame(String clientRequest) throws NoSuchMethodException, IllegalAccessException, InvocationTargetException,
			InstantiationException, ClassNotFoundException {

		Object obj = createGameRunStarter();
		if (clientRequest == null || clientRequest.trim().isEmpty()) {
			Method m = obj.getClass().getMethod("startFullGame", new Class[0]);
			m.invoke(obj);
		} else {
			Method m = obj.getClass().getMethod("startCheckRun", String.class);
			m.invoke(obj, clientRequest);
		}
	}

	public String getVersion() throws IllegalAccessException, IllegalArgumentException, InvocationTargetException, NoSuchMethodException,
			SecurityException, InstantiationException, ClassNotFoundException {
		Object obj = createGameRunStarter();
		Method m = obj.getClass().getMethod("getVersion", new Class[0]);
		return (String) m.invoke(obj);
	}

	protected Object createGameRunStarter() throws InstantiationException, IllegalAccessException, ClassNotFoundException {
		JclObjectFactory factory = JclObjectFactory.getInstance();
		return factory.create(jcl, "de.oglimmer.cyc.GameRunStarter");
	}

	protected void initClassLoader() {
		jcl = new JarClassLoader();

		jcl.add(baseDir + currentDir);
	}

	private void findLatestDir() {
		File f = new File(baseDir);
		int maxNum = -1;
		String dirName = "";
		for (File ff : f.listFiles(new _FilenameFilter())) {
			if (Integer.parseInt(ff.getName().substring(3)) > maxNum) {
				maxNum = Integer.parseInt(ff.getName().substring(3));
				dirName = ff.getName();
			}
		}
		if (maxNum == -1) {
			throw new RuntimeException("No game-engine found at " + baseDir);
		}
		if (currentDir == null || !currentDir.equals(dirName)) {
			log.debug("Switching to new engine directory:" + dirName);
			currentDir = dirName;
			initClassLoader();
		}
	}

	public void stop() {
		running = false;
		dirScannerThread.interrupt();
	}

	class _FilenameFilter implements FilenameFilter {
		@Override
		public boolean accept(File dir, String name) {
			return name.startsWith("cyc");
		}
	}

	class DirectoryScanner implements Runnable {
		@Override
		public void run() {
			running = true;
			while (running) {
				try {
					findLatestDir();
					TimeUnit.SECONDS.sleep(5);
				} catch (InterruptedException e) {
					// ignore
				} catch (Exception e) {
					log.error("DirectoryScanner failed", e);
				}
			}
		}

	}

}
