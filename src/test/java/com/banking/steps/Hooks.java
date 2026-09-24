package com.banking.steps;

import com.banking.utils.ConfigReader;
import io.cucumber.java.After;
import io.cucumber.java.Before;
import io.restassured.RestAssured;

public class Hooks {

    @Before
    public void setup() {
        // Set the base URL for all Rest-Assured requests
        RestAssured.baseURI = ConfigReader.getProperty("base.url");
    }

    @After
    public void teardown() {
        // Implement logic to delete the test customer created during the scenario.
        // The assignment mandates cleaning up records even after failures.
        // Example: RestAssured.given().delete("/customers/" + testCustomerNumber);
    }
}