package net.raysforge.commons;

import javax.net.ssl.X509TrustManager;

import java.security.cert.X509Certificate;

public class SimpleTrustManager implements X509TrustManager {
	public void checkClientTrusted(X509Certificate[] cert, String authType) {
		return;
	}

	public void checkServerTrusted(X509Certificate[] cert, String authType) {
		return;
	}

	public X509Certificate[] getAcceptedIssuers() {
		return new X509Certificate[0];
	}
}