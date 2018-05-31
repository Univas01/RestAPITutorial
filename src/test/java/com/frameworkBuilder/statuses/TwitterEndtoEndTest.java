package com.frameworkBuilder.statuses;

import org.testng.Assert;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;
import com.frameworkBuilder.common.RestUtilities;
import com.frameworkBuilder.constants.EndPoints;
import com.frameworkBuilder.constants.Path;
import io.restassured.path.json.JsonPath;
import io.restassured.response.Response;
import io.restassured.specification.RequestSpecification;
import io.restassured.specification.ResponseSpecification;
import static io.restassured.RestAssured.*;
import static org.hamcrest.Matchers.*;

public class TwitterEndtoEndTest {
	
	RequestSpecification requestSpec;
	ResponseSpecification responseSpec;
	Response response;
	String tweetId;
	String tweetText;
	
	@BeforeMethod
	public void setUp() {
		requestSpec = RestUtilities.getRequestSpecification();
		requestSpec.basePath(Path.STASUSES);
		responseSpec = RestUtilities.getResponseSpecification();
	}
	
	@Test
	public void postTweet() {
		response = 
		given()
			.spec(RestUtilities.createQueryParam(requestSpec, "status", "My Tenth tweet this week"))
		.when()
			.post(EndPoints.STATUSES_TWEET_POST)
		.then()
			.spec(responseSpec)
			.body("user.screen_name", equalTo("RestfulTest"))
			.extract().response();
		JsonPath postTweetJsonPath = RestUtilities.getJsonPath(response);
		tweetId = postTweetJsonPath.get("id_str");
		tweetText = postTweetJsonPath.get("text");		
	}
	
	@Test (enabled = true, dependsOnMethods = {"postTweet"})
	public void showTweet() {
		response = 
		given()
			.spec(RestUtilities.createQueryParam(requestSpec, "id", tweetId))
		.when()
			.get(EndPoints.STATUSES_TWEET_READ_SINGLE)
		.then()
			.spec(responseSpec)
			//.log().all()
			.extract().response();	
		String showTweetResponseBody = response.getBody().asString();
		JsonPath showTweetJsonPath = new JsonPath(showTweetResponseBody);
		Assert.assertEquals(showTweetJsonPath.get("text"), tweetText);
	}
	
	@Test (enabled = true, dependsOnMethods = {"showTweet"})
	public void deleteTweet() {
		given()
			.spec(RestUtilities.createPathParam(requestSpec, "id", tweetId))
		.when()
			.post(EndPoints.STATUSES_TWEET_DESTROY)
		.then()
			.spec(responseSpec);
	}
}
