package ch.matias.m295_lb;

import org.apache.commons.codec.binary.Base64;
import org.apache.http.client.methods.HttpUriRequest;
import org.springframework.http.HttpHeaders;

import java.nio.charset.StandardCharsets;

class JerseyApiTest {
	private static final String SERVICE_URL
			= "http://localhost:8080/artifact/resources";

	private static final String USERNAME = "admin";
	private static final String PASSWORD = "1234";

	private void addAuthorizationHeader(HttpUriRequest request) {
		String auth = STR."\{USERNAME}:\{PASSWORD}";
		byte[] encodedAuth = Base64.encodeBase64(auth.getBytes(StandardCharsets.ISO_8859_1));
		String authHeader = STR."Basic \{new String(encodedAuth)}";
		request.setHeader(HttpHeaders.AUTHORIZATION, authHeader);
	}


}
