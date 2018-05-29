package com.frameworkBuilder.common;

import com.frameworkBuilder.constants.Auth;
import com.frameworkBuilder.constants.Path;
import static org.hamcrest.Matchers.*;
import java.util.Map;
import java.util.concurrent.TimeUnit;
import io.restassured.RestAssured;
import io.restassured.authentication.AuthenticationScheme;
import io.restassured.builder.RequestSpecBuilder;
import io.restassured.builder.ResponseSpecBuilder;
import io.restassured.http.ContentType;
import io.restassured.path.json.JsonPath;
import io.restassured.path.xml.XmlPath;
import io.restassured.response.Response;
import io.restassured.specification.RequestSpecification;
import io.restassured.specification.ResponseSpecification;
import static io.restassured.RestAssured.*;

public class RestUtilities {

	public static String ENDPOINT;
	public static RequestSpecBuilder REQUEST_BUILDER;
	public static RequestSpecification REQUEST_SPEC;
	public static ResponseSpecBuilder RESPONSE_BUILDER;
	public static ResponseSpecification RESPONSE_SPEC;

	public static void setEndPoint(String endPoint) {
		ENDPOINT = endPoint;
	}

	public static RequestSpecification getRequestSpecification() {
		AuthenticationScheme authScheme = RestAssured.oauth(Auth.CONSUMER_KEY, Auth.CONSUMER_SECRET, Auth.ACCESS_TOKEN,
				Auth.ACCESS_SECRET);
		REQUEST_BUILDER = new RequestSpecBuilder();
		REQUEST_BUILDER.setBaseUri(Path.BASE_URI);
		REQUEST_BUILDER.setAuth(authScheme);
		REQUEST_SPEC = REQUEST_BUILDER.build();
		return REQUEST_SPEC;
	}
	
	public static ResponseSpecification getResponseSpecification(){
		RESPONSE_BUILDER = new ResponseSpecBuilder();
		RESPONSE_BUILDER.expectContentType(ContentType.JSON);
		RESPONSE_BUILDER.expectStatusCode(200);
		RESPONSE_BUILDER.expectResponseTime(lessThan(5L), TimeUnit.SECONDS);
		RESPONSE_SPEC = RESPONSE_BUILDER.build();
		return RESPONSE_SPEC;
	}

	public static RequestSpecification createQueryParam(RequestSpecification rspec, String key, String value){
		return  rspec.queryParam(key, value);
	}
	
	public static RequestSpecification createQueryParams(RequestSpecification rspec, Map<String, String> queryMaps){
		return rspec.queryParams(queryMaps);
	}

	public static RequestSpecification createPathParam(RequestSpecification rspec, String key, String value){
		return rspec.pathParam(key, value);
	}

	public static RequestSpecification createPathParams(RequestSpecification rspec, Map<String, String> pathMaps){
		return rspec.pathParams(pathMaps);
	}

	public static Response getResponse(){
		return given().get(ENDPOINT);
	}
	
	public static Response getResponse(RequestSpecification recSpec, String type) {
		REQUEST_SPEC.spec(recSpec);
		Response response = null;
		if (type.equalsIgnoreCase("get")) {
			response = given().spec(REQUEST_SPEC).when().get(ENDPOINT);
		} else if (type.equalsIgnoreCase("post")) {
			response = given().spec(REQUEST_SPEC).when().post(ENDPOINT);
		} else if (type.equalsIgnoreCase("put")) {
			response = given().spec(REQUEST_SPEC).when().put(ENDPOINT);
		} else if (type.equalsIgnoreCase("delete")) {
			response = given().spec(REQUEST_SPEC).when().delete(ENDPOINT);
		} else {
			System.out.println("This type is not supported");
		}
		response.then().log().all();
		response.then().spec(RESPONSE_SPEC);
		return response;
	}
	
	public static JsonPath getJsonPath(Response res){
		String jsonPath = res.asString();
		return new JsonPath(jsonPath);
	}
	
	public static XmlPath getXmlPath(Response res){
		String xmlPath = res.asString();
		return new XmlPath(xmlPath);
	}
	
	public static void resetBasePath(){
		RestAssured.basePath = null;
	}
	
	public static void setContentType(ContentType type){
		given().contentType(type);
	}

}
