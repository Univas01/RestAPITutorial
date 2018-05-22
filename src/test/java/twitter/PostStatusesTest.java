package twitter;

import org.testng.Assert;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;
import io.restassured.RestAssured;
import io.restassured.path.json.JsonPath;
import io.restassured.response.Response;
import static io.restassured.RestAssured.*;
import static org.hamcrest.Matchers.*;

public class PostStatusesTest {

	String consumerKey = "2nJxMyZherdxMb5toAfCGcORd";
	String consumerSecret = "jvGVLiXrGQW4EvVWrIWvyAJZbAAZbZ4TB2I8LodLwSOSA6SlUC";
	String accessToken = "998043340636647425-D1jtqGUC6AvVzzO3wmAhjTiKMcn7olq";
	String accessSecret = "5xXymi1PcaITrKfoU6wHzslCwPlbm9C76wSbq9be9GuXv";
	Response response;
	String tweetId;
	

	@BeforeMethod
	public void setUp() {

		RestAssured.baseURI = "https://api.twitter.com";
		RestAssured.basePath = "/1.1/statuses";
	}

	@Test
	public void postRequest(){

		response = 
		given()
			.log()
			.all()
			.auth()
			.oauth(consumerKey, consumerSecret, accessToken, accessSecret)
			.queryParam("status", "My First Tweet")
		.when()
			.post("/update.json")
		.then()
			.body("user.name", equalTo("RESTful Test"))
			.extract().response();
			
		String responseBody = response.getBody().prettyPrint();
		JsonPath jsonPath = new JsonPath(responseBody);
		tweetId = jsonPath.get("id_str");
		
		String a = response.path("text");
		Assert.assertTrue(a.contains("My First Tweet"));
		
		Assert.assertEquals(response.statusCode(), 200);
		Assert.assertEquals(jsonPath.get("text"), "My First Tweet");
	}
	
	@Test (enabled=true, dependsOnMethods={"postRequest"})
	public void showTweet(){
		
		response = 
		given()
			.auth()
			.oauth(consumerKey, consumerSecret, accessToken, accessSecret)
			.queryParam("id", tweetId)
		.when()
			.get("/show.json");

		response.getBody().prettyPrint();
		Assert.assertEquals(response.statusCode(), 200);
	}

	@Test (enabled=true, dependsOnMethods={"showTweet"})
	public void deleteTweet(){

		response = 
		given()
			.auth()
			.oauth(consumerKey, consumerSecret, accessToken, accessSecret)
			.pathParam("id", tweetId)
		.when()
			.post("/destroy/{id}.json");
		response.getBody().prettyPrint();
		Assert.assertEquals(response.statusCode(), 200);

		}
	}
