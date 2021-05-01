package net.raysforge.commons;

import java.io.ByteArrayOutputStream;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.Authenticator;
import java.net.HttpURLConnection;
import java.net.PasswordAuthentication;
import java.net.URL;
import java.util.Base64;

public class WebDavClient extends Authenticator {
	
	private final String baseURL;
	private final String username;
	private final String password;
	private boolean isAuthenticated = false;

	public WebDavClient(String baseURL, String username, String password) {
		this.baseURL = baseURL;
		this.username = username;
		this.password = password;
		Authenticator.setDefault(this);
	}
	
	public PasswordAuthentication getPasswordAuthentication() {
		isAuthenticated = true;
		return new PasswordAuthentication(username, password.toCharArray());
	}

	public boolean exists(String urlPath) throws IOException {
		System.out.println("checking: " + urlPath);

		URL u = new URL(baseURL + urlPath);
		HttpURLConnection c = (HttpURLConnection) u.openConnection();
		c.setRequestMethod("HEAD");

		int responseCode = c.getResponseCode();
		return responseCode != 404;

	}

	public boolean delete(String urlPath) throws IOException {
		System.out.println("deleting: " + urlPath);
		URL u = new URL(baseURL + urlPath);
		HttpURLConnection c = (HttpURLConnection) u.openConnection();
		c.setRequestMethod("DELETE");

		int responseCode = c.getResponseCode();

		ByteArrayOutputStream baos = new ByteArrayOutputStream();
		Copy.streamAndClose(c.getInputStream(), baos);

		//System.out.println(baos.toString());
		return responseCode == 204;
	}

	public HttpURLConnection getUploadConnection(String urlPath) throws IOException {
		System.out.println("uploading: " + urlPath);

		URL u = new URL(baseURL + urlPath);
		HttpURLConnection c = (HttpURLConnection) u.openConnection();
		ensureAuthentication(c);
		c.setChunkedStreamingMode(65536);
		c.setDoInput(true);
		c.setDoOutput(true);

		c.setRequestMethod("PUT");
		return c;
	}
	
	public void assertResponseCode(HttpURLConnection c, int code) throws IOException
	{
		int responseCode = c.getResponseCode();

		InputStream is = c.getInputStream(); // eat response data.
		ByteArrayOutputStream baos = new ByteArrayOutputStream();
		Copy.streamAndClose(is, baos);

		//System.out.println(baos.toString());
		if( responseCode != code)
			throw new RuntimeException("responseCode != code: " + responseCode +" != "+code);
	}
	
	public boolean upload(String localFile, String urlPath) throws IOException {
		//System.out.println("uploading: " + urlPath);
		
		URL u = new URL(baseURL + urlPath);
		HttpURLConnection c = (HttpURLConnection) u.openConnection();
		ensureAuthentication(c);
		c.setChunkedStreamingMode(65536);
		c.setDoInput(true);
		c.setDoOutput(true);

		c.setRequestMethod("PUT");

		OutputStream os = c.getOutputStream();
		FileInputStream fis = new FileInputStream(localFile);
		Copy.streamAndClose(fis, os);

		int responseCode = c.getResponseCode();

		InputStream is = c.getInputStream();
		ByteArrayOutputStream baos = new ByteArrayOutputStream();
		Copy.streamAndClose(is, baos);

		//System.out.println(baos.toString());
		return responseCode == 201;
	}

	private void ensureAuthentication(HttpURLConnection c) {
		if( !isAuthenticated)
		{
			String userpass = username + ":" + password;
			String basicAuth = "Basic " + Base64.getEncoder().encodeToString(userpass.getBytes());
			c.setRequestProperty ("Authorization", basicAuth);			
		}
	}

	public void download(String urlPath, String localFile) throws IOException {
		System.out.println("downloading: " + urlPath);

		URL u = new URL(baseURL + urlPath);
		HttpURLConnection c = (HttpURLConnection) u.openConnection();
		InputStream is = c.getInputStream();

		FileOutputStream fos = new FileOutputStream(localFile);
		Copy.streamAndClose(is, fos);
	}

	public InputStream getDownloadStream(String urlPath) throws IOException {
		System.out.println("downloading: " + urlPath);

		URL u = new URL(baseURL + urlPath);
		HttpURLConnection c = (HttpURLConnection) u.openConnection();
		return c.getInputStream();
	}

	public void deleteIfItExists(String urlPath) throws IOException {
		if( exists(urlPath))
			delete(urlPath);
		
	}

}
