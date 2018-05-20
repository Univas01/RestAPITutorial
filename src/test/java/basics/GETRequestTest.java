package basics;

import org.testng.annotations.Test;
import org.testng.Assert;
import org.testng.annotations.BeforeMethod;
import static io.restassured.RestAssured.*;
import static org.hamcrest.Matchers.*;
import io.restassured.RestAssured;
import io.restassured.path.json.JsonPath;
import io.restassured.response.Response;

public class GETRequestTest {
	
	
	@BeforeMethod
	public void setUp(){
		// https://maps.googleapis.com/maps/api/distancematrix/json?origins=Vancouver+BC|Seattle&
		// destinations=San+Francisco|Victoria+BC&mode=bicycling&language=fr-FR&key=YOUR_API_KEY
		RestAssured.baseURI = "https://maps.googleapis.com";
		RestAssured.basePath = "/maps/api";
	}
	
	@Test
	public void getRequest(){

		given()
			.param("origins", "London")
			.param("destinations", "Manchester")
			.param("mode", "bicycling")
			.param("key", "AIzaSyDtKTQY3Tw7TE8EH_pgvxq_iGloCzfFFRY")
		.when()
			.get("/distancematrix/json")
		.then()
			.statusCode(200)
			.and()
			.body("rows[0].elements[0].distance.text", equalTo("342 km"));
	}
	
	@Test  (enabled = false)
	public void getRequestBody(){
		Response response = 
		given()
			.param("origins", "London")
			.param("destinations", "Manchester")
			.param("mode", "bicycling")
			.param("key", "AIzaSyDtKTQY3Tw7TE8EH_pgvxq_iGloCzfFFRY")
		.when()
			.get("/distancematrix/json");
	
		String resonseBody = response.getBody().prettyPrint();
		JsonPath jsonPath = new JsonPath(resonseBody);
		Assert.assertEquals(jsonPath.get("destination_addresses[0]"), "Manchester, UK");
		
		
	}
}
