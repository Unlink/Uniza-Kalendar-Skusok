/*
 *  Copyright (c) 2015 Michal Ďuračík
 */
package cachedHttpClient;

import com.unlink.common.HttpClient.BasicNameValuePair;
import com.unlink.common.HttpClient.IHttpClient;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStreamWriter;
import java.net.HttpURLConnection;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.List;
import java.util.Scanner;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author Unlink
 */
public class HttpClient implements IHttpClient {

	private final File aCacheDir;
	private final IHttpClient aFallback;

	public HttpClient(File paCacheDir, IHttpClient paFallback) {
		if (!paCacheDir.exists()) {
			paCacheDir.mkdirs();
		}
		this.aCacheDir = paCacheDir;
		this.aFallback = paFallback;
	}

	@Override
	public String get(String paUrl) throws IOException {
		return get(paUrl, "UTF-8");
	}

	@Override
	public String get(String paUrl, String paEnconding) throws IOException {
		File f = getFile(paUrl);
		if (aFallback != null) {
			String response = aFallback.get(paUrl, paEnconding);
			try (OutputStreamWriter osw = new OutputStreamWriter(new FileOutputStream(f), paEnconding)) {
				osw.append(response);
			}
			return response;
		}
		else if (f.exists()) {
			return readStream(new FileInputStream(f), paEnconding);
		}
		else {
			throw new CacheMissException("Nebola nájdená url '" + paUrl + "' a fallback nebol definovaný");
		}
	}

	@Override
	public String post(String paUrl, List<BasicNameValuePair> paParams) throws IOException {
		return post(paUrl, paParams, "UTF-8");
	}

	@Override
	public String post(String paUrl, List<BasicNameValuePair> paParams, String paEnconding) throws IOException {
		File f = getFile(paUrl);
		if (aFallback != null) {
			String response = aFallback.post(paUrl, paParams, paEnconding);
			try (OutputStreamWriter osw = new OutputStreamWriter(new FileOutputStream(f), paEnconding)) {
				osw.append(response);
			}
			return response;
		}
		else if (f.exists()) {
			return readStream(new FileInputStream(f), paEnconding);
		}
		else {
			throw new CacheMissException("Nebola nájdená url '" + paUrl + "' a fallback nebol definovaný");
		}
	}

	private String readStream(InputStream is, String paEnconding) throws IOException {
		Scanner s = new Scanner(is, paEnconding).useDelimiter("\\A");
		return s.hasNext() ? s.next() : "";
	}

	private File getFile(String paUrl) {
		return new File(aCacheDir, md5(paUrl) + ".html");
	}

	private String md5(String paStr) {
		try {
			MessageDigest md = MessageDigest.getInstance("MD5");
			md.update(paStr.getBytes());
			byte[] digest = md.digest();
			StringBuilder sb = new StringBuilder();
			for (byte b : digest) {
				sb.append(String.format("%02x", b & 0xff));
			}

			return sb.toString();
		}
		catch (NoSuchAlgorithmException ex) {
			return null;
		}
	}

}
