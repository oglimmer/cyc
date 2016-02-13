package de.oglimmer.cyc.web.util;

import java.io.IOException;
import java.io.InputStream;
import java.io.StringWriter;

import org.apache.commons.io.IOUtils;
import org.apache.commons.mail.Email;
import org.apache.commons.mail.EmailConstants;
import org.apache.commons.mail.EmailException;
import org.apache.commons.mail.SimpleEmail;

import de.oglimmer.cyc.web.WebContainerProperties;
import lombok.extern.slf4j.Slf4j;

@Slf4j
public enum EmailService {
	INSTANCE;

	public void sendNewAccount(String email, String id, String userName) {
		createAndSendMailFile(email, "Account creation for codeyourrestaurant.com", "/account-creation.txt", userName,
				id, WebContainerProperties.INSTANCE.getAddressPageOwner());
	}

	private void createAndSendMailFile(String email, String subject, String fileName, Object... params) {
		try (InputStream is = getClass().getResourceAsStream(fileName)) {
			StringWriter writer = new StringWriter();
			IOUtils.copy(is, writer, null);
			String msg = writer.toString();
			createAndSendMail(email, subject, String.format(msg, params));
		} catch (IOException e) {
			log.error("Failed to get resource from file", e);
		}
	}

	public void sendPasswordReset(String email, String newPass) {
		createAndSendMail(email, "New password for codeyourrestaurant.com",
				"Hello\n, your new password is : " + newPass + ". You should login now and change it.");
	}

	private void createAndSendMail(String email, String subject, String msg) {
		try {
			Email simpleEmail = new SimpleEmail();
			simpleEmail.setCharset(EmailConstants.UTF_8);
			simpleEmail.setHostName(WebContainerProperties.INSTANCE.getSmtpHost());
			if (WebContainerProperties.INSTANCE.getSmtpPort() > 0) {
				simpleEmail.setSmtpPort(WebContainerProperties.INSTANCE.getSmtpPort());
			}
			simpleEmail.setSSLOnConnect(WebContainerProperties.INSTANCE.getSmtpSSL());
			if (!WebContainerProperties.INSTANCE.getSmtpUser().isEmpty()) {
				simpleEmail.setAuthentication(WebContainerProperties.INSTANCE.getSmtpUser(),
						WebContainerProperties.INSTANCE.getSmtpPassword());
			}
			simpleEmail.setFrom(WebContainerProperties.INSTANCE.getSmtpFrom());
			simpleEmail.setSubject(subject);
			simpleEmail.setMsg(msg);
			simpleEmail.addTo(email);
			simpleEmail.send();
		} catch (EmailException e) {
			log.error("Failed to send password email", e);
		}
	}

}
