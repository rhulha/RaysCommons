package net.raysforge.commons;

import java.security.GeneralSecurityException;
import java.security.SecureRandom;
import java.security.cert.CertificateException;
import java.security.cert.X509Certificate;
import java.util.List;
import java.util.Map;

import javax.net.ssl.HostnameVerifier;
import javax.net.ssl.HttpsURLConnection;
import javax.net.ssl.SSLContext;
import javax.net.ssl.SSLSession;
import javax.net.ssl.SSLSocketFactory;
import javax.net.ssl.X509TrustManager;

public class HttpsUtils {
	
	public static SSLSocketFactory getAllTrustingSSLSocketFactory() throws GeneralSecurityException{
		SSLContext context = SSLContext.getInstance("TLS");
		context.init(null, new X509TrustManager[] { new X509TrustManager() {
			public void checkClientTrusted(X509Certificate[] chain, String authType) throws CertificateException {
			}

			public void checkServerTrusted(X509Certificate[] chain, String authType) throws CertificateException {
			}

			public X509Certificate[] getAcceptedIssuers() {
				return new X509Certificate[0];
			}
		} }, new SecureRandom());
		return context.getSocketFactory(); 
	}
	
	public static void trustEveryone() {
		try {
			HttpsURLConnection.setDefaultHostnameVerifier(new HostnameVerifier() {
				public boolean verify(String hostname, SSLSession session) {
					return true;
				}
			});
			HttpsURLConnection.setDefaultSSLSocketFactory(getAllTrustingSSLSocketFactory());
		} catch (GeneralSecurityException e) { // should never happen 
			e.printStackTrace();
		}
	}

	public static void printHeaderFirstField(Map<String, List<String>> headerFields) {
		for (String key : headerFields.keySet()) {
			System.out.println(key + " : " + headerFields.get(key).get(0));
		}
	}
}
