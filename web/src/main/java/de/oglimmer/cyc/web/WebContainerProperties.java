package de.oglimmer.cyc.web;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.nio.file.FileSystems;
import java.nio.file.Path;
import java.nio.file.StandardWatchEventKinds;
import java.nio.file.WatchEvent;
import java.nio.file.WatchKey;
import java.nio.file.WatchService;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Properties;

import org.apache.commons.lang.RandomStringUtils;

import lombok.extern.slf4j.Slf4j;

@Slf4j
public enum WebContainerProperties {
	INSTANCE;

	private static final String CYC_PROPERTIES = "cyc.properties";

	private Properties prop = new Properties();
	private boolean running = true;
	private Thread propertyFileWatcherThread;
	private PropertyFileWatcher propertyFileWatcher;
	private List<Runnable> reloadables = new ArrayList<>();
	private String sourceLocation;

	private WebContainerProperties() {
		init();
	}

	private void init() {
		sourceLocation = System.getProperty(CYC_PROPERTIES);
		if (sourceLocation != null) {
			try {
				try (InputStream fis = new FileInputStream(sourceLocation)) {
					prop.load(fis);
				}
				if (propertyFileWatcherThread == null) {
					propertyFileWatcher = new PropertyFileWatcher();
					propertyFileWatcherThread = new Thread(propertyFileWatcher);
					propertyFileWatcherThread.setName("PropertyFileWatcherThread");
					propertyFileWatcherThread.start();
				}
			} catch (IOException e) {
				log.error("Failed to load properties file " + sourceLocation, e);
			}
		}
	}

	public String getFbSecret() {
		return prop.getProperty("facebook.secret", "");
	}

	public String getFbAppId() {
		return prop.getProperty("facebook.appId", "");
	}

	public String getGoogleClientId() {
		return prop.getProperty("google.clientId", "");
	}

	public String getEnginePassword() {
		return prop.getProperty("engine.password");
	}

	public String getFullEngineHost() {
		// don't use localhost, as JDK8 doesn't resolve localhost properly
		return prop.getProperty("engine.host.full", "127.0.0.1");
	}

	public int getFullEnginePort() {
		/* port defined in de.oglimmer.cyc.GameServer.SERVER_PORT */
		return Integer.parseInt(prop.getProperty("engine.port.full", "9998"));
	}

	public String getTestEngineHost() {
		// don't use localhost, as JDK8 doesn't resolve localhost properly
		return prop.getProperty("engine.host.test", "127.0.0.1");
	}
	
	public int getTestEnginePort() {
		/* port defined in de.oglimmer.cyc.GameServer.SERVER_PORT */
		return Integer.parseInt(prop.getProperty("engine.port.test", "9998"));
	}

	public boolean isHttpsEnabled() {
		return Boolean.parseBoolean(prop.getProperty("web.https.required", "false"));
	}

	public String getHttpsDomain() {
		return prop.getProperty("web.https.domain", "codeyourrestaurant.com");
	}

	public String getSmtpUser() {
		return prop.getProperty("smtp.user", System.getProperty("cyc.smtp.user", ""));
	}

	public String getSmtpPassword() {
		return prop.getProperty("smtp.password", System.getProperty("cyc.smtp.password", ""));
	}

	public String getSmtpHost() {
		return prop.getProperty("smtp.host", System.getProperty("cyc.smtp.host", "localhost"));
	}

	public int getSmtpPort() {
		return Integer.parseInt(prop.getProperty("smtp.port", System.getProperty("cyc.smtp.port", "-1")));
	}

	public boolean getSmtpSSL() {
		return Boolean.parseBoolean(prop.getProperty("smtp.ssl", System.getProperty("cyc.smtp.ssl", "false")));
	}

	public String getSmtpFrom() {
		return prop.getProperty("smtp.from", System.getProperty("cyc.smtp.from",
				"\"CodeYourRestaurant Mail System\" <no-reply@codeyourrestaurant.com>"));
	}

	public Date getSystemDisabledDate() {
		try {
			SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm");
			return sdf.parse(prop.getProperty("cyc.date-disabled", ""));
		} catch (ParseException e) {
			Date never = new Date();
			never.setTime(4102444799000L);// 2099-12-31 23:59:59
			return never;
		}
	}

	public Date getSystemHaltDate() {
		try {
			SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm");
			return sdf.parse(prop.getProperty("cyc.date-halt", ""));
		} catch (ParseException e) {
			Date never = new Date();
			never.setTime(4102444799000L);// 2099-12-31 23:59:59
			return never;
		}
	}

	public String getSystemMessage() {
		return prop.getProperty("cyc.system-message", "");
	}

	public String getAddressPageOwner() {
		return prop.getProperty("cyc.address-page-owner", "");
	}

	public String getCaptchaKeyPhrase() {
		return prop.getProperty("cyc.captcha.keyphrase", RandomStringUtils.randomAlphanumeric(32));
	}

	public String getCaptchaInitVector() {
		return prop.getProperty("cyc.captcha.initvector", RandomStringUtils.randomAlphanumeric(32));
	}

	public boolean isCaptchaEnabled() {
		return Boolean.parseBoolean(prop.getProperty("cyc.captcha.enabled", "false"));
	}

	public boolean isEmailVerificationEnabled() {
		return Boolean.parseBoolean(prop.getProperty("cyc.emailVerification.enabled", "false"));
	}

	public String getGlobalAdminEmail() {
		return prop.getProperty("cyc.globalAdmin.email");
	}
	
	public void registerOnReload(Runnable toCall) {
		reloadables.add(toCall);
	}

	void reload() {
		init();
		reloadables.forEach(c -> c.run());
	}

	public void shutdown() {
		running = false;		
		propertyFileWatcher.shutdown();		
		propertyFileWatcherThread.interrupt();		
	}

	class PropertyFileWatcher implements Runnable {

		private WatchKey wk;
		
		public void shutdown() {
			if (wk != null) {
				wk.cancel();
			}
		}
		
		public void run() {
			File toWatch = new File(sourceLocation);
			log.info("PropertyFileWatcher started");
			try {
				final Path path = FileSystems.getDefault().getPath(toWatch.getParent());
				try (final WatchService watchService = FileSystems.getDefault().newWatchService()) {
					path.register(watchService, StandardWatchEventKinds.ENTRY_MODIFY);					
					while (running) {
						wk = watchService.take();
						if (wk.isValid()) {
							for (WatchEvent<?> event : wk.pollEvents()) {
								// we only register "ENTRY_MODIFY" so the context is always a Path.
								final Path changed = (Path) event.context();
								if (changed.endsWith(toWatch.getName())) {
									log.trace("{} changed => reload", toWatch.getAbsolutePath());
									reload();
								}
							}
							boolean valid = wk.reset();
							if (!valid) {
								log.warn("The PropertyFileWatcher's key has been unregistered.");
							}
						}			
					}
				}
			} catch (InterruptedException e) {
			} catch (Exception e) {
				log.error("PropertyFileWatcher failed", e);
			}
			log.info("PropertyFileWatcher ended");
		}
	}
}
