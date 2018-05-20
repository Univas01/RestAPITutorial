package basics;

import org.testng.annotations.Test;
import org.testng.Assert;
import org.testng.annotations.BeforeMethod;
import static io.restassured.RestAssured.*;
import static org.hamcrest.Matchers.*;
import io.restassured.RestAssured;
import io.restassured.http.ContentType;
import io.restassured.path.json.JsonPath;
import io.restassured.response.Response;

public class POSTRequestTest {
	
	Response response;
	String placeIdResponseBody;
	
	@BeforeMethod
	public void setUp(){
		
		RestAssured.baseURI = "https://maps.googleapis.com";
		RestAssured.basePath = "/maps/api";
	}
	
	@Test (enabled = false)
	public void getRequest(){

		given()
			.queryParam("key", "AIzaSyDtKTQY3Tw7TE8EH_pgvxq_iGloCzfFFRY")
			.body("{"
					+ "\"location\": {"
					+ "\"lat\": -33.8669710,"
					+ "\"lng\": 151.1958750},"
					+ "\"accuracy\": 50,"
					+ "\"name\": \"Google Shoes!\","
					+ "\"phone_number\": \"(02) 9374 4000\","
					+ "\"address\": \"48 Pirrama Road, Pyrmont, NSW 2009, Australia\","
					+ "\"types\": [\"shoe_store\"],"
					+ "\"website\": \"http://www.google.com.au/\","
					+ "\"language\": \"en-AU\""
					+"}")
		.when()
			.post("/place/add/json")
		.then()
			.statusCode(200).and()
			.contentType(ContentType.JSON).and()
			.body("scope", equalTo("APP")).and()
			.body("status", equalTo("OK"));
	}
	
	@Test (priority = 1, enabled = true)
	public void getRequestTwo(){

		response = 
		given()
			.queryParam("key", "AIzaSyDtKTQY3Tw7TE8EH_pgvxq_iGloCzfFFRY")
			.body("{"
					+ "\"location\": {"
					+ "\"lat\": -33.8669710,"
					+ "\"lng\": 151.1958750},"
					+ "\"accuracy\": 50,"
					+ "\"name\": \"Google Shoes!\","
					+ "\"phone_number\": \"(02) 9374 4000\","
					+ "\"address\": \"48 Pirrama Road, Pyrmont, NSW 2009, Australia\","
					+ "\"types\": [\"shoe_store\"],"
					+ "\"website\": \"http://www.google.com.au/\","
					+ "\"language\": \"en-AU\""
					+"}")
		.when()
			.post("/place/add/json");
		String responseBody = response.getBody().prettyPrint();
		JsonPath jsonPathBody = new JsonPath(responseBody);
		String id = jsonPathBody.get("id");
		placeIdResponseBody = jsonPathBody.get("place_id");
		System.out.println("Place Id = " + placeIdResponseBody);
	}
	
	@Test (priority = 2, enabled = false)
	public void deleteRequest() throws InterruptedException{
		
		response = 
		given()
			.queryParam("key", "AIzaSyDtKTQY3Tw7TE8EH_pgvxq_iGloCzfFFRY")
			.body("{"
					+ "\"place_id\": \"{{placeIdResponseBody}}\""
					+ "}")
		.when()
			.post("/place/delete/json");

		String deleteBody = response.getBody().prettyPrint();
		JsonPath jsonPathDeleteBody = new JsonPath(deleteBody);
		String placeIdDeleteBody = jsonPathDeleteBody.get("status");
		Assert.assertEquals(placeIdDeleteBody, "OK");

	}
	
}
