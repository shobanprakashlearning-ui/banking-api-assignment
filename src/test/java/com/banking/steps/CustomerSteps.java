package com.banking.steps;

import io.cucumber.java.en.Given;
import io.cucumber.java.en.When;
import io.cucumber.java.en.Then;
import io.cucumber.java.en.And;
import io.restassured.RestAssured;
import io.restassured.response.Response;
import io.restassured.http.ContentType;
import static org.testng.Assert.*;
import com.banking.models.Customer;
import com.banking.models.CustomerAddress;
import com.banking.utils.TestContext;
import com.banking.models.ContactDetails;

import java.util.Random;
import java.util.concurrent.ThreadLocalRandom;



public class CustomerSteps {
    
    private Response response; 
   
    // Change your class variable type
    private Customer dynamicCustomerPayload;
    
    private int randomString = new Random().nextInt(1000000, 10000000);
    String customerNumber = String.valueOf(randomString);  
    
    private TestContext testContext;

    // Cucumber automatically injects the shared TestContext here
    public CustomerSteps(TestContext testContext) {
        this.testContext = testContext;
    }
    

    @Given("I have synthetic customer data")
    public void i_have_synthetic_customer_data() {
        // Create independent synthetic data using Jackson or raw strings
    	String uniqueEmail = "testuser" + System.currentTimeMillis() + "@example.com";
    	
   
    	ContactDetails contact = new ContactDetails();
        contact.setEmailId(uniqueEmail);
        contact.setHomePhone("555-0100");
        contact.setWorkPhone("555-0101");

        // 2. Build Address
        CustomerAddress address = new CustomerAddress();
        address.setAddress1("123 Test St");
        address.setAddress2("Apt " + new Random().nextInt(100)); // Dynamic apartment number
        address.setCity("San Francisco");
        address.setState("CA");
        address.setZip("94105");
        address.setCountry("USA");

        // 3. Build Main Customer Object
        dynamicCustomerPayload = new Customer();
        dynamicCustomerPayload.setFirstName("John");
        dynamicCustomerPayload.setLastName("Doe");
        dynamicCustomerPayload.setMiddleName("W");
        dynamicCustomerPayload.setCustomerNumber(customerNumber); // Dynamic customer number
        dynamicCustomerPayload.setStatus("ACTIVE");
        dynamicCustomerPayload.setCustomerAddress(address);
        dynamicCustomerPayload.setContactDetails(contact);
    }

    @When("I create a new customer")
    public void i_create_a_new_customer() {
        response = RestAssured.given()
                .contentType(ContentType.JSON)
                .body(dynamicCustomerPayload)
                .when()
                .post("/customers/add");
        
        response.then().log().ifValidationFails(); 
        assertEquals(response.getStatusCode(), 201, "Expected customer Retrival to return 201");
        
        System.out.println("Customer created with customerNumber: " + customerNumber);
        
        // Store the customer number in the shared context for later use
        testContext.setCustomerNumber(customerNumber); 
                
    }
    

    @And("I can retrieve the customer details")
    public void I_can_retrieve_the_customer_details() {    	
    	
    	  response = RestAssured.given()                 
                  .when()
                  .get("/customers/"+customerNumber);
          
          response.then().log().ifValidationFails(); // Logs the full server response body and headers
       
        assertEquals(response.getStatusCode(), 200, "Expected customer Retrival to return 200");
    }
    
    
    @When ("I update the customer's information")
    public void i_update_the_customer_information() {
    	
    	String newPhoneNumber = "555-" + (new Random().nextInt(9000) + 1000);        
        dynamicCustomerPayload.getContactDetails().setHomePhone(newPhoneNumber);
		

		response = RestAssured.given()
				.contentType(ContentType.JSON)
				.body(dynamicCustomerPayload)
				.when()
				.put("/customers/"+customerNumber);
		
		response.then().log().ifValidationFails();// Logs the full server response body and headers
		
		assertEquals(response.getStatusCode(), 200, "Expected customer Retrival to return 200");
		String responseBody = response.asString().trim();
	    assertEquals(responseBody, "Success: Customer updated.", "The response message did not match the expected success string.");
    
    
    }
    
    @And("retrieving the customer shows the updated information")
    public void retrieving_the_customer_shows_the_updated_information() {
    	
    	  response = RestAssured.given()                 
                  .when()
                  .get("/customers/"+customerNumber);
          
          response.then().log().ifValidationFails(); // Logs the full server response body and headers
       
        assertEquals(response.getStatusCode(), 200, "Expected customer Retrival to return 200");
        
        String actualHomePhone = response.jsonPath().getString("contactDetails.homePhone");
        
        // 4. Get the expected homePhone we set in the previous step
        String expectedHomePhone = dynamicCustomerPayload.getContactDetails().getHomePhone();

        // 5. Assert that the retrieved phone number matches the updated one
        assertEquals(actualHomePhone, expectedHomePhone, "The home phone number in the response did not match the updated value.");
    }
    
    
    @When ("I delete the customer")
	public void i_delete_the_customer() {
    	
    	  response = RestAssured.given()                 
                  .when()
                  .delete("/customers/"+customerNumber);
          
          response.then().log().ifValidationFails(); // Logs the full server response body and headers
          
        assertEquals(response.getStatusCode(), 200, "Expected customer Deletion API to return 200");
  		String responseBody = response.asString().trim();
  	    assertEquals(responseBody, "Success: Customer deleted.", "The response message did not match the expected Deletion Success string.");
    }
    
    @And("retrieving the customer should show that it no longer exists")
    public void retrieving_the_customer_shows_that_it_no_longer_exists() {    	
		
		response = RestAssured.given()
				.when()
				.get("/customers/"+customerNumber);
		  
		  response.then().log().ifValidationFails(); // Logs the full server response body and headers
	   
		assertEquals(response.getStatusCode(), 404, "Expected customer Retrival to return 404 after deletion");
		
		String responseBody = response.asString().trim();
  	    assertEquals(responseBody, "Customer Number "+customerNumber+" not found.", "The response message did not match the expected Not Found string.");
    	
    }
    }
    
