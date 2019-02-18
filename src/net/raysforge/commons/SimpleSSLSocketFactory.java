package net.raysforge.commons;

import java.io.IOException;
import java.net.InetAddress;
import java.net.Socket;
import java.security.KeyManagementException;
import java.security.NoSuchAlgorithmException;
import java.util.logging.Logger;

import javax.net.SocketFactory;
import javax.net.ssl.HostnameVerifier;
import javax.net.ssl.HttpsURLConnection;
import javax.net.ssl.SSLContext;
import javax.net.ssl.SSLSession;
import javax.net.ssl.SSLSocketFactory;
import javax.net.ssl.TrustManager;

public class SimpleSSLSocketFactory extends SSLSocketFactory {

	final static Logger logger = Logger.getLogger(SimpleSSLSocketFactory.class.getName());

	public static void enableUnofficiallySignedSSLCertificates(boolean enableHostnameMissmatch) {
		TrustManager[] trustAllCerts = new TrustManager[1];
		trustAllCerts[0] = new SimpleTrustManager();
		SSLContext sc = null;
		try {
			sc = SSLContext.getInstance("SSL");
			sc.init(null, trustAllCerts, new java.security.SecureRandom());
			HttpsURLConnection.setDefaultSSLSocketFactory(sc.getSocketFactory());
		} catch (NoSuchAlgorithmException e) {
			logger.severe("ERROR " + e);
		} catch (KeyManagementException e) {
			logger.severe("ERROR " + e);
		}

		if (enableHostnameMissmatch) {
			HostnameVerifier hv = new HostnameVerifier() {
				public boolean verify(String urlHostName, SSLSession session) {
					logger.warning("Warning: URL Host: " + urlHostName + " vs. session.getPeerHost: " + session.getPeerHost());
					return true;
				}
			};
			HttpsURLConnection.setDefaultHostnameVerifier(hv);
		}
		logger.fine("Simple SSL trust manager installed...");
	}

	private SSLSocketFactory factory;

	public SimpleSSLSocketFactory() {
		try {
			SSLContext sslcontext = SSLContext.getInstance("TLS");
			sslcontext.init(null, // No KeyManager required
					new TrustManager[] { new SimpleTrustManager() }, new java.security.SecureRandom());

			this.factory = sslcontext.getSocketFactory();

		} catch (Exception ex) {
			logger.info("Error in ssl factory: " + ex);
			// MNLoggerImpl.getRootLogger().error(ex.getMessage(), ex);
		}
	}

	public static synchronized SocketFactory getDefault() {
		return new SimpleSSLSocketFactory();
	}

	@Override
	public Socket createSocket(Socket socket, String s, int i, boolean flag) throws IOException {
		return this.factory.createSocket(socket, s, i, flag);
	}

	@Override
	public Socket createSocket(InetAddress inaddr, int i, InetAddress inaddr1, int j) throws IOException {
		return this.factory.createSocket(inaddr, i, inaddr1, j);
	}

	@Override
	public Socket createSocket(InetAddress inaddr, int i) throws IOException {
		return this.factory.createSocket(inaddr, i);
	}

	@Override
	public Socket createSocket(String s, int i, InetAddress inaddr, int j) throws IOException {
		return this.factory.createSocket(s, i, inaddr, j);
	}

	@Override
	public Socket createSocket(String s, int i) throws IOException {
		return this.factory.createSocket(s, i);
	}

	@Override
	public String[] getDefaultCipherSuites() {
		return this.factory.getSupportedCipherSuites();
	}

	@Override
	public String[] getSupportedCipherSuites() {
		return this.factory.getSupportedCipherSuites();
	}

}
