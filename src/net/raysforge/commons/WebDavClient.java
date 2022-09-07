package net.raysforge.commons;

import java.io.IOException;
import java.io.InputStream;
import java.net.Authenticator;
import java.net.PasswordAuthentication;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.time.Duration;

public class WebDavClient {
	
	private final String baseURL;
	private final HttpClient httpClient;

    //String userpass = username + ":" + password;
    //String basicAuth = "Basic " + Base64.getEncoder().encodeToString(userpass.getBytes());
    //c.setRequestProperty ("Authorization", basicAuth);          

	public WebDavClient(String baseURL, String username, String password) {
		this.baseURL = baseURL;
        httpClient = HttpClient.newBuilder()
                .authenticator(new Authenticator() {
                    @Override
                    protected PasswordAuthentication getPasswordAuthentication() {
                        return new PasswordAuthentication(username, password.toCharArray());
                    }
                })
                //.version(HttpClient.Version.HTTP_1_1)
                .connectTimeout(Duration.ofSeconds(10))
                .build();
	}
	
    private HttpResponse<String> request1(String method, String urlPath) throws IOException {
        HttpRequest request = HttpRequest.newBuilder().uri(URI.create(baseURL + urlPath)).method(method, HttpRequest.BodyPublishers.ofString("")).build();
        //.header("Content-Type", "text/xml")
        try {
            return httpClient.send(request, HttpResponse.BodyHandlers.ofString());
        } catch (IOException | InterruptedException e) {
            throw new IOException(e);
        }
    }

    protected String request2(String method, String urlPath) throws IOException {
        HttpResponse<String> response = request1(method,urlPath);
        int sc = response.statusCode();
        if( sc >= 200 && sc < 300 ) {
            return response.body();
        } else {
            throw new IOException("HTTP ERROR: " + sc);
        }
	}

    public boolean exists(String urlPath) throws IOException {
		System.out.println("checking: " + urlPath);
		HttpResponse<String> response = request1("HEAD", urlPath);
		return response.statusCode() != 404;
	}
	
    public boolean createFolder(String urlPath) throws Exception {
        System.out.println("createFolder: " + urlPath);
        
        if(exists(urlPath)) {
            return false;
        }
        
        HttpResponse<String> response = request1("MKCOL", urlPath);
        
        int sc = response.statusCode();
        if( sc != 201 )  {
            System.out.println(response.body());
            throw new IOException("HTTP ERROR: " + sc);
        } else {
            return true;
        }
    }

	public boolean delete(String urlPath) throws IOException {
		System.out.println("deleting: " + urlPath);
		HttpResponse<String> response = request1("DELETE", urlPath);
        int sc = response.statusCode();
		return sc == 204;
	}

    public boolean upload(InputStream is, String urlPath) throws IOException {
        System.out.println("uploading: " + urlPath);
        
        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create(baseURL + urlPath))
                .headers("Content-Type", "text/plain;charset=UTF-8")
                .PUT(HttpRequest.BodyPublishers.ofInputStream(()->is))
                .build();
        
        try {
            HttpResponse<String> response = this.httpClient.send(request, HttpResponse.BodyHandlers.ofString());
            int sc = response.statusCode();
            if( sc >= 200 && sc < 300 ) {
                return sc == 201;
            } else {
                throw new IOException("HTTP ERROR: " + sc);
            }
        } catch (IOException | InterruptedException e) {
            throw new IOException(e);
        }

    }

    public boolean upload(String localFile, String urlPath) throws IOException {
		System.out.println("uploading: " + urlPath);
		
		HttpRequest request = HttpRequest.newBuilder()
		        .uri(URI.create(baseURL + urlPath))
		        .headers("Content-Type", "text/plain;charset=UTF-8")
		        .PUT(HttpRequest.BodyPublishers.ofFile(Paths.get(localFile)))
		        .build();
		
        try {
            HttpResponse<String> response = this.httpClient.send(request, HttpResponse.BodyHandlers.ofString());
            int sc = response.statusCode();
            if( sc >= 200 && sc < 300 ) {
                return sc == 201;
            } else {
                throw new IOException("HTTP ERROR: " + sc);
            }
        } catch (IOException | InterruptedException e) {
            throw new IOException(e);
        }

	}

	public void download(String urlPath, String localFile) throws IOException  {
		System.out.println("downloading: " + urlPath);

		HttpRequest request = HttpRequest.newBuilder().uri(URI.create(baseURL + urlPath)).build();

        try {
            this.httpClient.send(request, HttpResponse.BodyHandlers.ofFile(Path.of(localFile)));
        } catch (IOException | InterruptedException e) {
            throw new IOException(e);
        }
	}

	public InputStream getDownloadStream(String urlPath) throws IOException {
		System.out.println("downloading: " + urlPath);

        HttpRequest request = HttpRequest.newBuilder().uri(URI.create(baseURL + urlPath)).build();

        try {
            HttpResponse<InputStream> response = this.httpClient.send(request, HttpResponse.BodyHandlers.ofInputStream());
            int sc = response.statusCode();
            if( sc >= 200 && sc < 300 ) {
                return response.body();
            } else {
                throw new IOException("HTTP ERROR: " + sc);
            }
        } catch (IOException | InterruptedException e) {
            throw new IOException(e);
        }
	}

	public boolean deleteIfItExists(String urlPath) throws IOException {
		if( exists(urlPath)) {
			return delete(urlPath);
		} else {
		    return false;
		}
		
	}

}
