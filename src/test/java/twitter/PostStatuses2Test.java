package twitter;

import org.testng.Assert;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;

import io.restassured.RestAssured;
import io.restassured.path.json.JsonPath;
import io.restassured.response.Response;
import static io.restassured.RestAssured.*;
import static org.hamcrest.Matchers.*;
import org.apache.log4j.Logger;

public class PostStatuses2Test {
	
	Logger log = Logger.getLogger(PostStatuses2Test.class.getClass());

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
		
		log.info("*****Inside postRequest Method*****");
		log.debug("*****Inside postRequest Method*****");
		
		response = 
		given()
			.log().all()
			.auth().oauth(consumerKey, consumerSecret, accessToken, accessSecret)
			.queryParam("status", "My First Tweet")
		.when()
			.post("/update.json")
		.then()
			.body("user.name", equalTo("RESTful Test"))
			.extract().response();
		
		log.info(response.getBody().prettyPrint());
		
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
		
		log.info("*****Inside showTweet Method*****");
		log.debug("*****Inside showTweet Method*****");
		
		response = 
		given()
			.auth()
			.oauth(consumerKey, consumerSecret, accessToken, accessSecret)
			.queryParam("id", tweetId)
		.when()
			.get("/show.json")
		.then()
			.statusCode(200)
			.extract().response();
		
		log.info(response.getBody().prettyPrint());

	}

	@Test (enabled=true, dependsOnMethods={"showTweet"})
	public void deleteTweet(){
		
		log.info("*****Inside deleteTweet Method*****");
		log.debug("*****Inside deleteTweet Method*****");

		response = 
		given()
			.auth()
			.oauth(consumerKey, consumerSecret, accessToken, accessSecret)
			.pathParam("id", tweetId)
		.when()
			.post("/destroy/{id}.json");
		response.getBody().prettyPrint();
		Assert.assertEquals(response.statusCode(), 200);
		
		log.info(response.getBody().prettyPrint());

		}
	}
