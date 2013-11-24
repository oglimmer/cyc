package de.oglimmer.cyc.engineContainer;

import java.io.File;
import java.io.FilenameFilter;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.concurrent.TimeUnit;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.xeustechnologies.jcl.JarClassLoader;
import org.xeustechnologies.jcl.JclObjectFactory;

public class EngineLoader {

	private static Logger log = LoggerFactory.getLogger(EngineLoader.class);

	private String currentDir = null;
	private String baseDir;
	private Thread dirScannerThread;
	private boolean running;
	private JarClassLoader jcl;

	public EngineLoader() {
		baseDir = System.getProperty("cyc.home", ".");
		if (!baseDir.endsWith("/")) {
			baseDir += "/";
		}
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

	public void startGame(String clientRequest) throws NoSuchMethodException, IllegalAccessException,
			InvocationTargetException {

		JclObjectFactory factory = JclObjectFactory.getInstance();

		Object obj = factory.create(jcl, "de.oglimmer.cyc.GameRunStarter");
		if (clientRequest == null || clientRequest.trim().isEmpty()) {
			Method m = obj.getClass().getMethod("startFullGame", new Class[0]);
			m.invoke(obj);
		} else {
			Method m = obj.getClass().getMethod("startCheckRun", String.class);
			m.invoke(obj, clientRequest);
		}
	}

	public String getVersion() throws IllegalAccessException, IllegalArgumentException, InvocationTargetException,
			NoSuchMethodException, SecurityException {
		JclObjectFactory factory = JclObjectFactory.getInstance();

		Object obj = factory.create(jcl, "de.oglimmer.cyc.GameRunStarter");
		Method m = obj.getClass().getMethod("getVersion", new Class[0]);
		return (String) m.invoke(obj);
	}

	private JarClassLoader initClassLoader() {
		JarClassLoader jcl = new JarClassLoader();

		jcl.add(baseDir + currentDir);
		return jcl;
	}

	private void findLatestDir() {
		File f = new File(baseDir);
		int maxNum = -1;
		String dirName = "";
		for (File ff : f.listFiles(new FilenameFilter() {
			@Override
			public boolean accept(File dir, String name) {
				return name.startsWith("cyc");
			}
		})) {
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
			jcl = initClassLoader();
		}
	}

	public void stop() {
		running = false;
		dirScannerThread.interrupt();
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
