package stepDefinition;



import org.junit.Assert;

import io.cucumber.java.en.Given;
import io.cucumber.java.en.Then;
import io.cucumber.java.en.When;
import io.restassured.RestAssured;
import io.restassured.path.json.JsonPath;
import io.restassured.response.Response;
import io.restassured.specification.RequestSpecification;

public class GenerateBearer {

	private static final String CLIENT_NAME = "Postman";
	private static final String CLIENT_EMAIL = "valent99@example.com";
	private static final String TOOL_ID = "4643";
	private static final String CUST_NAME = "John";
	private static final String BASE_URL = "https://simple-tool-rental-api.glitch.me";

	private static String token;
	private static Response response;



	@Given("I am an authorized user to create a Token")
	public void iAmAnAuthorizedUser() {

		RestAssured.baseURI = BASE_URL;
		RequestSpecification request = RestAssured.given();

		request.header("Content-Type", "application/json");
		response = request.body("{ \"clientName\":\"" + CLIENT_NAME + "\", \"clientEmail\":\"" + CLIENT_EMAIL + "\"}")
				.post("/api-clients");

		String jsonString = response.asString();
		token = JsonPath.from(jsonString).get("accessToken");
		System.out.println("token:"+token);

	}
	
	@When("Create an Order using Token")
	public void createOrder() {
		RestAssured.baseURI = BASE_URL;
		RequestSpecification request = RestAssured.given();
		request.header("Authorization", "Bearer " + token)
		.header("Content-Type", "application/json");

		response = request.body("{ \"toolId\":\"" + TOOL_ID + "\", \"customerName\":\"" + CUST_NAME + "\"}")
				.post("/orders");
		
	}

	@Then("Verify the Order Status Code and Response")
	public void verifyStatusCode() {
		String jsonString=response.asString();
		Assert.assertEquals(201, response.getStatusCode());
		
		Assert.assertEquals(true, JsonPath.from(jsonString).get("created"));

		System.out.println("created is:"+JsonPath.from(jsonString).get("created"));
		System.out.println("orderId is:"+JsonPath.from(jsonString).get("orderId"));
		Assert.assertNotNull(JsonPath.from(jsonString).get("orderId"));

	}

	

}