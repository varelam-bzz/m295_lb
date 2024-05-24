package ch.matias.m295_lb;

import org.apache.commons.codec.binary.Base64;
import org.apache.http.HttpResponse;
import org.apache.http.HttpStatus;
import org.apache.http.client.methods.*;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.HttpClientBuilder;
import org.junit.jupiter.api.MethodOrderer;
import org.junit.jupiter.api.Order;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestMethodOrder;
import org.springframework.http.HttpHeaders;

import java.io.IOException;
import java.nio.charset.StandardCharsets;

import static org.junit.jupiter.api.Assertions.assertEquals;

@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
public class JerseyApiTest {
	private static final String SERVICE_URL
			= "http://localhost:8080/artifact/resources/games";

	private static final String USERNAME_ADMIN = "admin";
	private static final String USERNAME_CLEANER = "cleaner";
	private static final String PASSWORD = "1234";

	private void addAuthorizationHeader(HttpUriRequest request, String username) {
		String auth = STR."\{username}:\{PASSWORD}";
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
		addAuthorizationHeader(request, USERNAME_ADMIN);
		StringEntity entity = new StringEntity(json);
		request.setEntity(entity);
		request.setHeader("Content-Type", "application/json");

		makeAssert(request, status);
	}

	public void makePut(HttpPut request, String json, int status) throws IOException {
		addAuthorizationHeader(request, USERNAME_ADMIN);
		StringEntity entity = new StringEntity(json);
		request.setEntity(entity);
		request.setHeader("Content-Type", "application/json");

		makeAssert(request, status);
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
	public void getGamesCount_thenOk() throws IOException {
		HttpUriRequest request = new HttpGet(STR."\{SERVICE_URL}/count");
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
	@Order(4)
	public void gameExistsById_thenOk() throws IOException {
		HttpUriRequest request = new HttpGet(STR."\{SERVICE_URL}/exists/1");
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
	public void gameExistsById_thenNotFound() throws IOException {
		HttpUriRequest request = new HttpGet(STR."\{SERVICE_URL}/exists/999");
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
	public void gameExistsByInvalidId_thenNotFound() throws IOException {
		HttpUriRequest request = new HttpGet(STR."\{SERVICE_URL}/exists/asdasd");
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
	@Order(3)
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
	public void getGameByInvalidId_thenNotFound() throws IOException {
		HttpUriRequest request = new HttpGet(STR."\{SERVICE_URL}/byId/asdasd");
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
	@Order(2)
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
	@Order(1)
	public void insertGame_thenOk() throws IOException {
		HttpPost request = new HttpPost(SERVICE_URL);
		String json = "{\"name\":\"Sekiro\",\"releaseDate\":\"2019-03-22\",\"price\":60,\"purchases\":1300000,\"released\":true,\"publisher\":{\"name\":\"FromSoftware\"}}";
		makePost(request, json, HttpStatus.SC_OK);
	}

	@Test
	public void insertGameWithMaxAllowedLength_thenOk() throws IOException {
		HttpPost request = new HttpPost(SERVICE_URL);
		String json = "{\"name\":\"8N#pQr2L!yVc6tFgS@Xe3hEwAsDfGzUxI*JkY9oP7l\",\"releaseDate\":\"2019-03-22\",\"price\":60,\"purchases\":1300000,\"released\":true,\"publisher\":{\"name\":\"FromSoftware\"}}";
		makePost(request, json, HttpStatus.SC_OK);
	}

	@Test
	public void insertGameWithId_thenOk() throws IOException {
		HttpPost request = new HttpPost(SERVICE_URL);
		String json = "{\"id\": 1,\"name\":\"TestTest\",\"releaseDate\":\"2019-03-22\",\"price\":60,\"purchases\":1300000,\"released\":true,\"publisher\":{\"name\":\"FromSoftware\"}}";
		makePost(request, json, HttpStatus.SC_OK);
	}

	@Test
	public void insertGame_thenConflict() throws IOException {
		HttpPost request = new HttpPost(SERVICE_URL);
		String json = "{\"name\":\"Sekiro\",\"releaseDate\":\"2019-03-22\",\"price\":60,\"purchases\":1300000,\"released\":true,\"publisher\":{\"name\":\"FromSoftware\"}}";
		makePost(request, json, HttpStatus.SC_CONFLICT);
	}

	@Test
	public void insertGameWithInvalidName_thenBadRequest() throws IOException {
		HttpPost request = new HttpPost(SERVICE_URL);
		String json = "{\"name\":\"ExistingGameExistingGameExistingGameExistingGameExistingGameExistingGameExistingGameExistingGameExistingGameExistingGameExistingGameExistingGameExistingGameExistingGameExistingGameExistingGameExistingGameExistingGameExistingGameExistingGameExistingGameExistingGameExistingGameExistingGameExistingGameExistingGameExistingGameExistingGameExistingGameExistingGameExistingGameExistingGameExistingGameExistingGameExistingGame\",\"releaseDate\":\"2019-03-22\",\"price\":60,\"purchases\":1300000,\"released\":true,\"publisher\":{\"name\":\"FromSoftware\"}}";
		makePost(request, json, HttpStatus.SC_BAD_REQUEST);
	}

	@Test
	public void insertGameWithInvalidPurchases_thenBadRequest() throws IOException {
		HttpPost request = new HttpPost(SERVICE_URL);
		String json = "{\"name\":\"ASDSD\",\"releaseDate\":\"2019-03-22\",\"price\":60,\"purchases\":-1300000,\"released\":true,\"publisher\":{\"name\":\"FromSoftware\"}}";
		makePost(request, json, HttpStatus.SC_BAD_REQUEST);
	}

	@Test
	public void insertGameWithInvalidPrice_thenBadRequest() throws IOException {
		HttpPost request = new HttpPost(SERVICE_URL);
		String json = "{\"name\":\"ASDSD\",\"releaseDate\":\"2019-03-22\",\"price\":360,\"purchases\":-1300000,\"released\":true,\"publisher\":{\"name\":\"FromSoftware\"}}";
		makePost(request, json, HttpStatus.SC_BAD_REQUEST);
	}

	@Test
	public void insertGameWithInvalidReleaseDate_thenBadRequest() throws IOException {
		HttpPost request = new HttpPost(SERVICE_URL);
		String json = "{\"name\":\"ASDSD\",\"releaseDate\":\"2019-03-2\",\"price\":360,\"purchases\":1300000,\"released\":true,\"publisher\":{\"name\":\"FromSoftware\"}}";
		makePost(request, json, HttpStatus.SC_BAD_REQUEST);
	}

	@Test
	public void insertGameWithInvalidRole_thenUnauthorized() throws IOException {
		HttpPost request = new HttpPost(SERVICE_URL);
		String json = "{\"name\":\"ASDSD\",\"releaseDate\":\"2019-03-22\",\"price\":60,\"purchases\":1300000,\"released\":true,\"publisher\":{\"name\":\"FromSoftware\"}}";
		addAuthorizationHeader(request, "");
		StringEntity entity = new StringEntity(json);
		request.setEntity(entity);
		request.setHeader("Content-Type", "application/json");

		makeAssert(request, HttpStatus.SC_UNAUTHORIZED);
	}

	@Test
	public void bulkInsertGame_thenOk() throws IOException {
		HttpPost request = new HttpPost(STR."\{SERVICE_URL}/bulk");
		String json = "[{\"name\":\"Game One\",\"releaseDate\":\"2020-03-22\",\"price\":60,\"purchases\":1300000,\"released\":true,\"publisher\":{\"name\":\"FromSoftware\"}},{\"name\":\"Elden Ring\",\"releaseDate\":\"2022-02-25\",\"price\":60,\"purchases\":2000000,\"released\":true,\"publisher\":{\"name\":\"FromSoftware\"}}]";
		makePost(request, json, HttpStatus.SC_OK);
	}

	@Test
	public void bulkInsertGameWithId_thenOk() throws IOException {
		HttpPost request = new HttpPost(STR."\{SERVICE_URL}/bulk");
		String json = "[{\"id\":1,\"name\":\"Game One\",\"releaseDate\":\"2020-03-22\",\"price\":60,\"purchases\":1300000,\"released\":true,\"publisher\":{\"id\":1,\"name\":\"FromSoftware\"}},{\"name\":\"Elden Ring\",\"releaseDate\":\"2022-02-25\",\"price\":60,\"purchases\":2000000,\"released\":true,\"publisher\":{\"name\":\"FromSoftware\"}}]";
		makePost(request, json, HttpStatus.SC_OK);
	}

	@Test
	public void bulkInsertGameWithInvalidPurchases_thenBadRequest() throws IOException {
		HttpPost request = new HttpPost(STR."\{SERVICE_URL}/bulk");
		String json = "[{\"name\":\"Game One\",\"releaseDate\":\"2020-03-22\",\"price\":60,\"purchases\":-1300000,\"released\":true,\"publisher\":{\"name\":\"FromSoftware\"}},{\"name\":\"Elden Ring\",\"releaseDate\":\"2022-02-25\",\"price\":60,\"purchases\":2000000,\"released\":true,\"publisher\":{\"name\":\"FromSoftware\"}}]";
		makePost(request, json, HttpStatus.SC_BAD_REQUEST);
	}

	@Test
	public void bulkInsertGameWithInvalidPrice_thenBadRequest() throws IOException {
		HttpPost request = new HttpPost(STR."\{SERVICE_URL}/bulk");
		String json = "[{\"name\":\"Game One\",\"releaseDate\":\"2020-03-22\",\"price\":360,\"purchases\":1300000,\"released\":true,\"publisher\":{\"name\":\"FromSoftware\"}},{\"name\":\"Elden Ring\",\"releaseDate\":\"2022-02-25\",\"price\":60,\"purchases\":2000000,\"released\":true,\"publisher\":{\"name\":\"FromSoftware\"}}]";
		makePost(request, json, HttpStatus.SC_BAD_REQUEST);
	}

	@Test
	public void bulkInsertGameWithInvalidReleaseDate_thenBadRequest() throws IOException {
		HttpPost request = new HttpPost(STR."\{SERVICE_URL}/bulk");
		String json = "[{\"name\":\"Game One\",\"releaseDate\":\"2020-03-2\",\"price\":60,\"purchases\":1300000,\"released\":true,\"publisher\":{\"name\":\"FromSoftware\"}},{\"name\":\"Elden Ring\",\"releaseDate\":\"2022-02-25\",\"price\":60,\"purchases\":2000000,\"released\":true,\"publisher\":{\"name\":\"FromSoftware\"}}]";
		makePost(request, json, HttpStatus.SC_BAD_REQUEST);
	}

	@Test
	public void bulkInsertGameWithInvalidRole_thenUnauthorized() throws IOException {
		HttpPost request = new HttpPost(SERVICE_URL);
		String json = "[{\"name\":\"Game One\",\"releaseDate\":\"2020-03-22\",\"price\":60,\"purchases\":1300000,\"released\":true,\"publisher\":{\"name\":\"FromSoftware\"}},{\"name\":\"Elden Ring\",\"releaseDate\":\"2022-02-25\",\"price\":60,\"purchases\":2000000,\"released\":true,\"publisher\":{\"name\":\"FromSoftware\"}}]";
		addAuthorizationHeader(request, "");
		StringEntity entity = new StringEntity(json);
		request.setEntity(entity);
		request.setHeader("Content-Type", "application/json");

		makeAssert(request, HttpStatus.SC_UNAUTHORIZED);
	}


	@Test
	public void updateGame_thenOk() throws IOException {
		HttpPut request = new HttpPut(SERVICE_URL);
		String json = "{\"name\":\"Sekiro\",\"releaseDate\":\"2019-03-22\",\"price\":60,\"purchases\":290000,\"released\":true,\"publisher\":{\"name\":\"FromSoftware\"}}";
		makePut(request, json, HttpStatus.SC_OK);
	}

	@Test
	public void updateGame_thenNotFound() throws IOException {
		HttpPut request = new HttpPut(SERVICE_URL);
		String json = "{\"name\":\"NonExistentGame\",\"releaseDate\":\"2019-03-22\",\"price\":60,\"purchases\":130000,\"released\":true,\"publisher\":{\"name\":\"NonExistentPublisher\"}}";
		makePut(request, json, HttpStatus.SC_NOT_FOUND);
	}

	@Test
	public void updateGameWithInvalidPurchases_thenBadRequest() throws IOException {
		HttpPut request = new HttpPut(SERVICE_URL);
		String json = "{\"name\":\"Sekiro\",\"releaseDate\":\"2019-03-22\",\"price\":60,\"purchases\":-130000,\"released\":true,\"publisher\":{\"name\":\"NonExistentPublisher\"}}";
		makePut(request, json, HttpStatus.SC_BAD_REQUEST);
	}

	@Test
	public void updateGameWithInvalidPrice_thenBadRequest() throws IOException {
		HttpPut request = new HttpPut(SERVICE_URL);
		String json = "{\"name\":\"NonExistentGame\",\"releaseDate\":\"2019-03-22\",\"price\":360,\"purchases\":130000,\"released\":true,\"publisher\":{\"name\":\"NonExistentPublisher\"}}";
		makePut(request, json, HttpStatus.SC_BAD_REQUEST);
	}

	@Test
	public void updateGameWithInvalidRole_thenUnauthorized() throws IOException {
		HttpPost request = new HttpPost(SERVICE_URL);
		String json = "{\"name\":\"Sekiro\",\"releaseDate\":\"2019-03-22\",\"price\":60,\"purchases\":1300000,\"released\":true,\"publisher\":{\"name\":\"FromSoftware\"}}";
		addAuthorizationHeader(request, "");
		StringEntity entity = new StringEntity(json);
		request.setEntity(entity);
		request.setHeader("Content-Type", "application/json");

		makeAssert(request, HttpStatus.SC_UNAUTHORIZED);
	}

	@Test
	public void deleteGame_thenOk() throws IOException {
		HttpUriRequest request = new HttpDelete(STR."\{SERVICE_URL}/1");
		addAuthorizationHeader(request, USERNAME_CLEANER);
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
		addAuthorizationHeader(request, USERNAME_CLEANER);
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
	public void deleteGameInvalidId_thenNotFound() throws IOException {
		HttpUriRequest request = new HttpDelete(STR."\{SERVICE_URL}/asdas");
		addAuthorizationHeader(request, USERNAME_CLEANER);
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
		addAuthorizationHeader(request, USERNAME_CLEANER);
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
	public void deleteGameWithInvalidRole_thenUnauthorized() throws IOException {
		HttpUriRequest request = new HttpDelete(STR."\{SERVICE_URL}/1");
		addAuthorizationHeader(request, "");
		HttpResponse httpResponse = HttpClientBuilder
				.create()
				.build()
				.execute(request);

		assertEquals(
				HttpStatus.SC_UNAUTHORIZED,
				httpResponse.getStatusLine().getStatusCode()
		);
	}

	@Test
	public void deleteAllGamesWithInvalidRole_thenUnauthorized() throws IOException {
		HttpUriRequest request = new HttpDelete(SERVICE_URL);
		addAuthorizationHeader(request, "");
		HttpResponse httpResponse = HttpClientBuilder
				.create()
				.build()
				.execute(request);

		assertEquals(
				HttpStatus.SC_UNAUTHORIZED,
				httpResponse.getStatusLine().getStatusCode()
		);
	}
}
