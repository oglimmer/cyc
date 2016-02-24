package de.oglimmer.cyc.web.ext;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.security.InvalidAlgorithmParameterException;
import java.security.InvalidKeyException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.Arrays;
import java.util.Base64;
import java.util.concurrent.TimeUnit;

import javax.crypto.BadPaddingException;
import javax.crypto.Cipher;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.NoSuchPaddingException;
import javax.crypto.spec.IvParameterSpec;
import javax.crypto.spec.SecretKeySpec;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.github.cage.Cage;
import com.github.cage.GCage;

import de.oglimmer.cyc.web.WebContainerProperties;
import lombok.Getter;
import lombok.SneakyThrows;

@WebServlet(urlPatterns = "/captcha")
public class CaptchaServlet extends HttpServlet {

	private static final long serialVersionUID = 1L;
	@Getter
	private static final Service service = new Service(WebContainerProperties.INSTANCE.getCaptchaKeyPhrase(),
			WebContainerProperties.INSTANCE.getCaptchaInitVector());
	private static final Cage cage = new GCage();

	public static String generateToken() {
		String token = cage.getTokenGenerator().next();
		return token;
	}

	@Override
	protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
		String token = req.getParameter("captchaToken");
		if (token == null) {
			resp.sendError(HttpServletResponse.SC_NOT_FOUND, "Captcha not found.");
			return;
		}
		try {
			TimeUnit.SECONDS.sleep(1);
		} catch (InterruptedException e) {
		}
		setResponseHeaders(resp);
		cage.draw(service.decrypt(token), resp.getOutputStream());
	}

	protected void setResponseHeaders(HttpServletResponse resp) {
		resp.setContentType("image/" + cage.getFormat());
		resp.setHeader("Cache-Control", "no-cache, no-store");
		resp.setHeader("Pragma", "no-cache");
		long time = System.currentTimeMillis();
		resp.setDateHeader("Last-Modified", time);
		resp.setDateHeader("Date", time);
		resp.setDateHeader("Expires", time);
	}

	public static class Service {

		private Cipher deCipher;
		private Cipher enCipher;
		private SecretKeySpec key;
		private IvParameterSpec ivSpec;

		@SneakyThrows(value = UnsupportedEncodingException.class)
		public Service(String keyPhrase, String initVector) {
			try {
				ivSpec = new IvParameterSpec(hashTo128bit(initVector));
				key = new SecretKeySpec(hashTo128bit(keyPhrase), "AES");
				deCipher = Cipher.getInstance("AES/CBC/PKCS5PADDING");
				enCipher = Cipher.getInstance("AES/CBC/PKCS5PADDING");
			} catch (NoSuchAlgorithmException e) {
				e.printStackTrace();
			} catch (NoSuchPaddingException e) {
				e.printStackTrace();
			}
		}

		private byte[] hashTo128bit(String str) throws UnsupportedEncodingException, NoSuchAlgorithmException {
			byte[] key = (str).getBytes("UTF-8");
			MessageDigest sha = MessageDigest.getInstance("SHA-1");
			key = sha.digest(key);
			key = Arrays.copyOf(key, 16); // use only first 128 bit
			return key;
		}

		@SneakyThrows(value = { InvalidKeyException.class, InvalidAlgorithmParameterException.class,
				IllegalBlockSizeException.class, BadPaddingException.class })
		public String encrypt(String obj) {
			byte[] input = obj.getBytes();
			enCipher.init(Cipher.ENCRYPT_MODE, key, ivSpec);
			return enc(enCipher.doFinal(input));
		}

		@SneakyThrows(value = { InvalidKeyException.class, InvalidAlgorithmParameterException.class,
				IllegalBlockSizeException.class, BadPaddingException.class })
		public String decrypt(String encrypted) {
			deCipher.init(Cipher.DECRYPT_MODE, key, ivSpec);
			return new String(deCipher.doFinal(dec(encrypted)));
		}

		private String enc(byte[] b) {
			return Base64.getEncoder().encodeToString(b);
		}

		private byte[] dec(String str) {
			return Base64.getDecoder().decode(str);
		}

	}

}