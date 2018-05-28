package twitter;

import org.testng.Assert;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;
import io.restassured.RestAssured;
import io.restassured.authentication.AuthenticationScheme;
import io.restassured.builder.RequestSpecBuilder;
import io.restassured.builder.ResponseSpecBuilder;
import io.restassured.http.ContentType;
import io.restassured.path.json.JsonPath;
import io.restassured.response.Response;
import io.restassured.specification.RequestSpecification;
import io.restassured.specification.ResponseSpecification;
import static io.restassured.RestAssured.*;
import static org.hamcrest.Matchers.*;
import java.util.concurrent.TimeUnit;
import org.apache.log4j.Logger;

public class CompleteTwitterWorkflowTest {

	RequestSpecBuilder requestBuilder;
	static RequestSpecification requestSpec;
	ResponseSpecBuilder responseBuilder;
	static ResponseSpecification responseSpec;
	Response response;
	String tweetId;

	@BeforeMethod
	public void setUp() {

		String consumerKey = "2nJxMyZherdxMb5toAfCGcORd";
		String consumerSecret = "jvGVLiXrGQW4EvVWrIWvyAJZbAAZbZ4TB2I8LodLwSOSA6SlUC";
		String accessToken = "998043340636647425-D1jtqGUC6AvVzzO3wmAhjTiKMcn7olq";
		String accessSecret = "5xXymi1PcaITrKfoU6wHzslCwPlbm9C76wSbq9be9GuXv";
		AuthenticationScheme authScheme = RestAssured.oauth(consumerKey, consumerSecret, accessToken, accessSecret);

		requestBuilder = new RequestSpecBuilder();
		requestBuilder.setBaseUri("https://api.twitter.com");
		requestBuilder.setBasePath("/1.1/statuses");
		requestBuilder.setAuth(authScheme);
		requestBuilder.addQueryParam("status", "My first tweet this week");
		requestSpec = requestBuilder.build();

		responseBuilder = new ResponseSpecBuilder();
		responseBuilder.expectStatusCode(200);
		responseBuilder.expectResponseTime(lessThan(10L), TimeUnit.SECONDS);
		responseBuilder.expectContentType(ContentType.JSON);
		responseSpec = responseBuilder.build();

	}
	
	@Test
	public void firstTweet(){
		
		response = 
		given()
			.spec(requestSpec)
		.when()
			.post("/update.json")
		.then()
			.spec(responseSpec)
			.extract().response();
		
		String responseBody = response.getBody().prettyPrint();
		JsonPath jsonPath = new JsonPath(responseBody);
		tweetId = jsonPath.get("id_str");
	}
	
	@Test (enabled=true, dependsOnMethods={"firstTweet"})
	public void showTweet(){
		
		given()
			.spec(requestSpec)
			.queryParam("id", tweetId)
		.when()
			.get("/show.json")
		.then()
			.spec(responseSpec)
			.extract().response().prettyPrint();
	}
	
	@Test (enabled=true, dependsOnMethods={"showTweet"})
	public void deleteTweet(){
		
		response = 
		given()
			.spec(requestSpec)
			.pathParam("id", tweetId)
		.when()
			.post("/destroy/{id}.json")
		.then()
			.spec(responseSpec)
			.extract().response();
		response.getBody().prettyPrint();
		Assert.assertEquals(response.statusCode(), 200);
	}
		

}
