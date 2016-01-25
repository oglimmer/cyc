package de.oglimmer.cyc.engineContainer;

import java.io.File;
import java.io.FilenameFilter;
import java.lang.reflect.InvocationTargetException;
import java.security.Policy;
import java.util.concurrent.TimeUnit;

import org.xeustechnologies.jcl.JarClassLoader;
import org.xeustechnologies.jcl.JclObjectFactory;

import de.oglimmer.cyc.engine.IGameRunStarter;
import lombok.extern.slf4j.Slf4j;

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
		log.debug("Using cyc.home={}", baseDir);
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

	public void startGame(String clientRequest) {

		IGameRunStarter obj = createGameRunStarter();
		if (clientRequest == null || clientRequest.trim().isEmpty()) {
			obj.startFullGame();
		} else if (clientRequest.toUpperCase().startsWith("TEST")) { 
			obj.startTestRun(clientRequest.substring(4));
		} else {
			obj.startCheckRun(clientRequest);
		}
	}

	public String getVersion() throws InvocationTargetException {
		IGameRunStarter obj = createGameRunStarter();
		return obj.getVersion();
	}

	protected IGameRunStarter createGameRunStarter() {
		JclObjectFactory factory = JclObjectFactory.getInstance();
		return (IGameRunStarter) factory.create(jcl, "de.oglimmer.cyc.GameRunStarter");
	}

	protected void initClassLoader() {
		jcl = new JarClassLoader();
		// Needed for proper garbage collection of the old JarClassLoader inside sun.security.provider.PolicyFile
		Policy.getPolicy().refresh();

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
			throw new NoEngineException(baseDir);
		}
		if (currentDir == null || !currentDir.equals(dirName)) {
			log.debug("Switching to new engine directory:" + dirName);
			currentDir = dirName;
			initClassLoader();
		}
	}

	public void stop() {
		running = false;
		if (dirScannerThread != null) {
			dirScannerThread.interrupt();
		}
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
					// ignore if interrupted
				} catch (NoEngineException e) {
					try {
						TimeUnit.MINUTES.sleep(1);
					} catch (InterruptedException e1) {
						// ignore if interrupted
					}
				} catch (Exception e) {
					log.error("DirectoryScanner failed", e);
				}
			}
		}

	}

}
