package com.banking.steps;

import com.banking.utils.ConfigReader;
import com.banking.utils.TestContext;

import io.cucumber.java.After;
import io.cucumber.java.Before;
import io.restassured.RestAssured;
import io.restassured.response.Response;

public class Hooks {

	private TestContext testContext;

    // PicoContainer automatically injects the same TestContext instance used in your Step Defs
    public Hooks(TestContext testContext) {
        this.testContext = testContext;
    }
    
    @Before
    public void setup() {
        // Set the base URL for all Rest-Assured requests
        RestAssured.baseURI = ConfigReader.getProperty("base.url");
    }

    @After
    public void teardown() {
    	// Check if a customer number was generated and stored during this scenario
        // Assuming you use a Long (object) or primitive long where default is 0
        if (testContext.getCustomerNumber() != null) {
            System.out.println("--- TEARDOWN INITITATED ---");
            System.out.println("Cleaning up test data for Customer Number: " + testContext.getCustomerNumber());

            // Execute the actual API call to delete the customer (which also deletes linked accounts)
            Response response = RestAssured.given()
                    .pathParam("customerNumber", testContext.getCustomerNumber())
                    .when()
                    .delete("/customers/{customerNumber}");

            // Log the result of the cleanup for debugging
            if (response.statusCode() == 200 || response.statusCode() == 204) {
                System.out.println("Cleanup successful. Customer deleted.");
            } else {
                System.err.println("Cleanup failed! Status Code: " + response.statusCode());
                System.err.println("Response: " + response.asString());
            }
        } else {
            System.out.println("No customer was created in this scenario. Skipping cleanup.");
        }
    }
 }