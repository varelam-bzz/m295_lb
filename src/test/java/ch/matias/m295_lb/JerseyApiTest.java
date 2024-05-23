package ch.matias.m295_lb;

import org.apache.commons.codec.binary.Base64;
import org.apache.http.HttpResponse;
import org.apache.http.HttpStatus;
import org.apache.http.client.methods.*;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.HttpClientBuilder;
import org.junit.Test;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.runner.RunWith;
import org.junit.runners.JUnit4;
import org.springframework.http.HttpHeaders;

import java.io.IOException;
import java.nio.charset.StandardCharsets;

import static org.junit.jupiter.api.Assertions.assertEquals;

@RunWith(JUnit4.class)
public class JerseyApiTest {
	private static final String SERVICE_URL
			= "http://localhost:8080/artifact/resources/games";

	private static final String USERNAME = "admin";
	private static final String PASSWORD = "1234";

	private void addAuthorizationHeader(HttpUriRequest request) {
		String auth = STR."\{USERNAME}:\{PASSWORD}";
		byte[] encodedAuth = Base64.encodeBase64(auth.getBytes(StandardCharsets.ISO_8859_1));
		String authHeader = STR."Basic \{new String(encodedAuth)}";
		request.setHeader(HttpHeaders.AUTHORIZATION, authHeader);
	}

	public void makeAssert(HttpUriRequest request, int status) throws IOException {
		HttpResponse httpResponse = HttpClientBuilder
				.create()
				.build()
				.execute(request);

		assertEquals(
				status,
				httpResponse.getStatusLine().getStatusCode()
		);
	}

	public void makePost(HttpPost request, String json, int status) throws IOException {
		addAuthorizationHeader(request);
		StringEntity entity = new StringEntity(json);
		request.setEntity(entity);
		request.setHeader("Content-Type", "application/json");

		makeAssert(request, status);
	}

	public void makePut(HttpPut request, String json, int status) throws IOException {
		addAuthorizationHeader(request);
		StringEntity entity = new StringEntity(json);
		request.setEntity(entity);
		request.setHeader("Content-Type", "application/json");

		makeAssert(request, status);
	}

	private void initializeTestData() throws IOException {
		HttpPost request = new HttpPost(SERVICE_URL);
		String json = "{\"name\":\"Sekiro\",\"releaseDate\":\"2019-03-22\",\"price\":60,\"purchases\":-1000,\"released\":true,\"publisher\":{\"name\":\"FromSoftware\"}}";
		makePost(request, json, HttpStatus.SC_OK);

		json = "{\"name\":\"ExistingGame\",\"releaseDate\":\"2019-03-22\",\"price\":60,\"purchases\":-1000,\"released\":true,\"publisher\":{\"name\":\"FromSoftware\"}}";
		makePost(request, json, HttpStatus.SC_OK);
	}

	private void cleanUpTestData() throws IOException {
		HttpUriRequest request = new HttpDelete(SERVICE_URL);
		addAuthorizationHeader(request);
		HttpClientBuilder
				.create()
				.build()
				.execute(request);
	}

	@BeforeEach
	public void setUp() throws IOException {
		cleanUpTestData();
		initializeTestData();
	}

	@AfterEach
	public void tearDown() throws IOException {
		cleanUpTestData();
	}

	@Test
	public void ping_thenOk() throws IOException {
		HttpUriRequest request = new HttpGet(STR."\{SERVICE_URL}/ping");
		HttpResponse httpResponse = HttpClientBuilder
				.create()
				.build()
				.execute(request);

		assertEquals(
				HttpStatus.SC_OK,
				httpResponse.getStatusLine().getStatusCode()
		);
	}

	@Test
	public void getAllGames_thenOk() throws IOException {
		HttpUriRequest request = new HttpGet(SERVICE_URL);
		HttpResponse httpResponse = HttpClientBuilder
				.create()
				.build()
				.execute(request);

		assertEquals(
				HttpStatus.SC_OK,
				httpResponse.getStatusLine().getStatusCode()
		);
	}

	@Test
	public void getGameById_thenOk() throws IOException {
		HttpUriRequest request = new HttpGet(STR."\{SERVICE_URL}/byId/1");
		HttpResponse httpResponse = HttpClientBuilder
				.create()
				.build()
				.execute(request);

		assertEquals(
				HttpStatus.SC_OK,
				httpResponse.getStatusLine().getStatusCode()
		);
	}

	@Test
	public void getGameById_thenNotFound() throws IOException {
		HttpUriRequest request = new HttpGet(STR."\{SERVICE_URL}/byId/9999");
		HttpResponse httpResponse = HttpClientBuilder
				.create()
				.build()
				.execute(request);

		assertEquals(
				HttpStatus.SC_NOT_FOUND,
				httpResponse.getStatusLine().getStatusCode()
		);
	}

	@Test
	public void getGameByName_thenOk() throws IOException {
		HttpUriRequest request = new HttpGet(STR."\{SERVICE_URL}/byName/Sekiro");
		HttpResponse httpResponse = HttpClientBuilder
				.create()
				.build()
				.execute(request);

		assertEquals(
				HttpStatus.SC_OK,
				httpResponse.getStatusLine().getStatusCode()
		);
	}

	@Test
	public void getGameByName_thenNotFound() throws IOException {
		HttpUriRequest request = new HttpGet(STR."\{SERVICE_URL}/byName/NonExistentGame");
		HttpResponse httpResponse = HttpClientBuilder
				.create()
				.build()
				.execute(request);

		assertEquals(
				HttpStatus.SC_NOT_FOUND,
				httpResponse.getStatusLine().getStatusCode()
		);
	}

	@Test
	public void getGameByReleaseDate_thenOk() throws IOException {
		HttpUriRequest request = new HttpGet(STR."\{SERVICE_URL}/byReleaseDate/2019-03-22");
		HttpResponse httpResponse = HttpClientBuilder
				.create()
				.build()
				.execute(request);

		assertEquals(
				HttpStatus.SC_OK,
				httpResponse.getStatusLine().getStatusCode()
		);
	}

	@Test
	public void getGameByReleaseDate_thenBadRequest() throws IOException {
		HttpUriRequest request = new HttpGet(STR."\{SERVICE_URL}/byReleaseDate/invalid-date");
		HttpResponse httpResponse = HttpClientBuilder
				.create()
				.build()
				.execute(request);

		assertEquals(
				HttpStatus.SC_BAD_REQUEST,
				httpResponse.getStatusLine().getStatusCode()
		);
	}

	@Test
	public void insertGame_thenOk() throws IOException {
		HttpPost request = new HttpPost(SERVICE_URL);
		String json = "{\"name\":\"Sekiro\",\"releaseDate\":\"2019-03-22\",\"price\":60,\"purchases\":1300000,\"released\":true,\"publisher\":{\"name\":\"FromSoftware\"}}";
		makePost(request, json, HttpStatus.SC_OK);
	}

	@Test
	public void insertGame_thenConflict() throws IOException {
		HttpPost request = new HttpPost(SERVICE_URL);
		String json = "{\"name\":\"ExistingGame\",\"releaseDate\":\"2019-03-22\",\"price\":60,\"purchases\":1300000,\"released\":true,\"publisher\":{\"name\":\"FromSoftware\"}}";
		makePost(request, json, HttpStatus.SC_CONFLICT);
	}

	@Test
	public void insertGame_thenBadRequest() throws IOException {
		HttpPost request = new HttpPost(SERVICE_URL);
		String json = "{\"badkey\":\"BadGame\"}";
		makePost(request, json, HttpStatus.SC_BAD_REQUEST);
	}

	@Test
	public void updateGame_thenOk() throws IOException {
		HttpPut request = new HttpPut(SERVICE_URL);
		String json = "{\"id\":1,\"name\":\"Sekiro\",\"releaseDate\":\"2019-03-22\",\"price\":60,\"purchases\":290000,\"released\":true,\"publisher\":{\"name\":\"FromSoftware\"}}";
		makePut(request, json, HttpStatus.SC_OK);
	}

	@Test
	public void updateGame_thenNotFound() throws IOException {
		HttpPut request = new HttpPut(SERVICE_URL);
		String json = "{\"id\":9999,\"name\":\"NonExistentGame\",\"releaseDate\":\"2019-03-22\",\"price\":60,\"purchases\":130000,\"released\":true,\"publisher\":{\"name\":\"NonExistentPublisher\"}}";
		makePut(request, json, HttpStatus.SC_NOT_FOUND);
	}

	@Test
	public void deleteGame_thenOk() throws IOException {
		HttpUriRequest request = new HttpDelete(STR."\{SERVICE_URL}/1");
		addAuthorizationHeader(request);
		HttpResponse httpResponse = HttpClientBuilder
				.create()
				.build()
				.execute(request);

		assertEquals(
				HttpStatus.SC_OK,
				httpResponse.getStatusLine().getStatusCode()
		);
	}

	@Test
	public void deleteGame_thenNotFound() throws IOException {
		HttpUriRequest request = new HttpDelete(STR."\{SERVICE_URL}/9999");
		addAuthorizationHeader(request);
		HttpResponse httpResponse = HttpClientBuilder
				.create()
				.build()
				.execute(request);

		assertEquals(
				HttpStatus.SC_NOT_FOUND,
				httpResponse.getStatusLine().getStatusCode()
		);
	}

	@Test
	public void deleteAllGames_thenOk() throws IOException {
		HttpUriRequest request = new HttpDelete(SERVICE_URL);
		addAuthorizationHeader(request);
		HttpResponse httpResponse = HttpClientBuilder
				.create()
				.build()
				.execute(request);

		assertEquals(
				HttpStatus.SC_OK,
				httpResponse.getStatusLine().getStatusCode()
		);
	}
}
