package com.frameworkBuilder.statuses;

import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;
import com.frameworkBuilder.common.RestUtilities;
import com.frameworkBuilder.constants.EndPoints;
import com.frameworkBuilder.constants.Path;
import static io.restassured.RestAssured.*;
import static org.hamcrest.Matchers.*;
import io.restassured.response.Response;
import io.restassured.specification.RequestSpecification;
import io.restassured.specification.ResponseSpecification;

public class UserTimelineTest {

	RequestSpecification requestSpec;
	ResponseSpecification responseSpec;
	Response response;

	@BeforeClass
	public void setUp() {
		requestSpec = RestUtilities.getRequestSpecification();
		requestSpec.queryParam("screen_name", "RestfulTest");
		requestSpec.basePath(Path.STASUSES);
		responseSpec = RestUtilities.getResponseSpecification();
	}
	
	@Test
	public void readTweetsTest() {
		
		given()
			.spec(RestUtilities.createQueryParam(requestSpec, "count", "1"))
		.when()
			.get(EndPoints.STATUSES_USER_TIMELINE)
		.then()
			.log().all()
			.spec(responseSpec)
			.body("user.name", hasItem("RESTful Test"));	
	}
}
