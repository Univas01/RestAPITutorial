package twitter;

import org.testng.Assert;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;
import io.restassured.RestAssured;
import io.restassured.path.json.JsonPath;
import io.restassured.response.Response;
import static io.restassured.RestAssured.*;

public class PostStatusesTest {
	
	String consumerKey = "2nJxMyZherdxMb5toAfCGcORd";
	String consumerSecret = "jvGVLiXrGQW4EvVWrIWvyAJZbAAZbZ4TB2I8LodLwSOSA6SlUC";
	String accessToken = "998043340636647425-D1jtqGUC6AvVzzO3wmAhjTiKMcn7olq";
	String accessSecret = "5xXymi1PcaITrKfoU6wHzslCwPlbm9C76wSbq9be9GuXv";
	Response response;

	@BeforeMethod
	public void setUp() {
		
		RestAssured.baseURI = "https://api.twitter.com";
		RestAssured.basePath = "/1.1/statuses";
	}
	
	@Test
	public void postRequest(){
		
		response = 
		given()
			.auth()
			.oauth(consumerKey, consumerSecret, accessToken, accessSecret)
			.queryParam("status", "My First Tweet")
		.when()
			.post("/update.json");
		
		String responseBody = response.getBody().prettyPrint();
		JsonPath jsonPath = new JsonPath(responseBody);
		
		Assert.assertEquals(response.statusCode(), 200);
		Assert.assertEquals(jsonPath.get("text"), "My First Tweet");
		System.out.println("Headers:- "+response.getHeaders());

	}

}
