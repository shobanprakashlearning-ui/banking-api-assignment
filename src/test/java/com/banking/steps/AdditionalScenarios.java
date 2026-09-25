package com.banking.steps;
import com.banking.models.AccountPayload;

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
import com.banking.models.AccountPayload;
import com.banking.models.BankInformation;
import com.banking.models.BranchAddress;
import com.banking.models.ContactDetails;

import java.math.BigDecimal;
import java.time.Instant;
import java.util.Random;
import java.util.concurrent.ThreadLocalRandom;



public class AdditionalScenarios {
    
    private Response response; 
   
    private String customerNumber;
    private int accountNumber = new Random().nextInt(9000000) + 1000000;    
    private TestContext testContext;
    private AccountPayload accountPayload;    

    // Cucumber automatically injects the shared TestContext here
    public AdditionalScenarios(TestContext testContext) {
        this.testContext = testContext;
    }
    
    
    
    @Given ("I have a non-existent customer number")
	public void i_have_a_non_existent_customer_number() {
    	
    	 int randomString = new Random().nextInt(1000000, 10000000);
    	 customerNumber = String.valueOf(randomString); 
    	
    }

    
    @And("I retrieve the customer details using the non-Existent customer number and verify Error")
    public void i_retrieve_the_customer_details_using_the_nonExistent_customer_number_and_verify_Error() {    	
    	
    	  response = RestAssured.given()                 
                  .when()
                  .get("/customers/"+customerNumber);
          
          response.then().log().all(); // Logs the full server response body and headers
       
        assertEquals(response.getStatusCode(), 404, "Expected customer Retrival to return error");
       
    }
    
  
    public Response addAccount() {
    	
        String existingCustomerNumber = testContext.getCustomerNumber();

        // 1. Build Branch Address
        BranchAddress branchAddress = new BranchAddress();
        branchAddress.setAddress1("456 Bank Plazareturn ");
        branchAddress.setCity("San Francisco");
        branchAddress.setState("CA");
        branchAddress.setZip("94104");
        branchAddress.setCountry("USA");

        // 2. Build Bank Information
        BankInformation bankInfo = new BankInformation();
        bankInfo.setBranchName("Downtown Branch");
        bankInfo.setBranchCode(1234);
        bankInfo.setRoutingNumber(123456789);
        bankInfo.setBranchAddress(branchAddress);

        // 3. Build Account Payload
        accountPayload = new AccountPayload();
        accountPayload.setAccountNumber(accountNumber);
        accountPayload.setBankInformation(bankInfo);
        accountPayload.setAccountStatus("ACTIVE");
        accountPayload.setAccountType("CHECKING");
        // Adding an explicit opening balance to ensure sufficient source funds for future transfers
        accountPayload.setAccountBalance(1000.00); 
        // Using Instant.now().toString() generates the required ISO-8601 timestamp format
        accountPayload.setAccountCreated(Instant.now().toString());

        // 4. Send the POST request to add the account
        // Note: dynamicCustomerPayload is assumed to be accessible from your Customer steps
        response = RestAssured.given()
                .log().ifValidationFails()
                .contentType(ContentType.JSON)                
                .body(accountPayload)
                .when()
                .post("/accounts/add/"+existingCustomerNumber);
                
        response.then().log().ifValidationFails();         
       
        return response;// Return the account number for potential use in other steps
    }
    
    @And ("I add a account to the existing customer")
	public void i_add_a_account_to_the_existing_customer() {
    	
		response=addAccount();		
		assertEquals(response.getStatusCode(), 201, "Expected Account Addition to return 201");
    }
    
    
    @Then("I attempt to add another account using the exact same account number and verify error")
	public void i_attempt_to_add_another_account_using_the_exact_same_account_number_and_verify_error(){
    	
    	response=addAccount();	   
		assertEquals(response.getStatusCode(), 400, "Expected duplicate Account Addition to return 400");
    }
    
   
    }
    
