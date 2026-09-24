package com.banking.steps;
import com.banking.models.AccountPayload;
import com.banking.models.TransferPayload;
import com.banking.models.BankInformation;
import com.banking.models.BranchAddress;
import com.banking.models.Transaction;
import com.banking.utils.TestContext;

import io.cucumber.java.en.And;
import io.cucumber.java.en.Given;
import io.cucumber.java.en.Then;
import io.cucumber.java.en.When;
import io.restassured.RestAssured;
import io.restassured.http.ContentType;
import io.restassured.response.Response;

import static org.testng.Assert.assertEquals;

import java.time.Instant;
import java.util.List;
import java.util.Random;

public class TransferSteps {
    
	
	private TestContext testContext;
	private Response response;   
    private AccountPayload accountPayload;    
    Random random = new Random();
    private String accountNumber1;
    private String accountNumber2;
	double sourceBalanceBeforeTransfer;
	double destinationBalanceBeforeTransfer;   
	double transferAmount;
    
    
    public TransferSteps(TestContext testContext) {
        this.testContext = testContext;
    }
    
    
    @When("Add two accounts to an customer having explicit opening balances")    
    public void add_two_accounts_to_an_existing_customer() {
    	
		// Add first account
    	accountNumber1=i_add_a_new_account_to_the_existing_customer();     	
    	testContext.setAccountNumber1(accountNumber1);    
		
		// Add second account
    	accountNumber2=i_add_a_new_account_to_the_existing_customer();    	
    	testContext.setAccountNumber2(accountNumber2);
    	
		
	}
    
    
    @When("I add a new account to the existing customer")
    public String i_add_a_new_account_to_the_existing_customer() {
    	int accountNumber = random.nextInt(9000000) + 1000000;
        String existingCustomerNumber = testContext.getCustomerNumber();

        // 1. Build Branch Address
        BranchAddress branchAddress = new BranchAddress();
        branchAddress.setAddress1("456 Bank Plaza");
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
        
       
        assertEquals(response.getStatusCode(), 201, "Expected Account Addition to return 201");
        return String.valueOf(accountNumber); // Return the account number for potential use in other steps
    }
    
    
    
    @Then("I get the account balance")
    public double i_get_the_account_balance(String accountNumber) {
		
		// Get balance for an account
		response = RestAssured.given()
				.log().ifValidationFails()
				.when()
				.get("/accounts/"+accountNumber);
				
		response.then().log().ifValidationFails();	
		double accountBalance = response.jsonPath().getDouble("accountBalance");		
		
		return accountBalance;
	}
    
    @Then("I get the balance in source and destination accounts before transfer")
    public void i_get_the_balance_in_source_and_destination_accounts_before_transfer() {
		sourceBalanceBeforeTransfer = i_get_the_account_balance(accountNumber1);
		destinationBalanceBeforeTransfer = i_get_the_account_balance(accountNumber2);
		
		System.out.println("Source Account Balance Before Transfer: " + sourceBalanceBeforeTransfer);
		System.out.println("Destination Account Balance Before Transfer: " + destinationBalanceBeforeTransfer);
		
	}
    
    @Then("verify the balance in both the source and destination accounts is updated correctly")
    public void verify_the_balance_in_both_the_source_and_destination_accounts_is_updated_correctly() {
    	
		double sourceBalanceAfterTransfer = i_get_the_account_balance(accountNumber1);
		double destinationBalanceAfterTransfer = i_get_the_account_balance(accountNumber2);
		
		assertEquals(sourceBalanceAfterTransfer, sourceBalanceBeforeTransfer - transferAmount, "Source account balance did not update correctly after transfer.");
		assertEquals(destinationBalanceAfterTransfer, destinationBalanceBeforeTransfer + transferAmount, "Destination account balance did not update correctly after transfer.");
    			
    }
    
    
    @Then("I get the account details")
    public double getAccountDetails() {
		
		// Get balance for an account
		response = RestAssured.given()
				.log().ifValidationFails()
				.when()
				.get("/accounts/"+accountNumber1);
				
		response.then().log().ifValidationFails();
		assertEquals(response.getStatusCode(), 200, "Expected Account Details retrieval to return 200");		
		double accountBalance = response.jsonPath().getDouble("accountBalance");	
	
		
		return accountBalance;
	}
    
	public void verifyTheTransactionHistory(String accountNum,String txType, double txAmount) {
    	
    	
    	response = RestAssured.given()
    			.log().ifValidationFails()
    			.when()
    			.get("accounts/transactions/"+accountNum);
    	
    	response.then().log().ifValidationFails();
    	
    	assertEquals(response.getStatusCode(), 200, "Expected transaction retrieval to return 200");
    	
    	List<Transaction> transactions = response.jsonPath().getList("$", Transaction.class);

    	// Access the first transaction safely
    	if (!transactions.isEmpty()) {
    	    Transaction firstTx = transactions.get(0);
    	    assertEquals(firstTx.getAccountNumber(), Long.parseLong(accountNum), "The account number did not match the expected value.");
    	    assertEquals(firstTx.getTxType(), txType, "The transaction type did not match the expected value.");
    	    assertEquals(firstTx.getTxAmount(), txAmount, "The transaction amount did not match the expected value.");
    	}
    	
    }
    
    
    @And("verify corresponding debit and credit records are created")
    	public void verify_corresponding_debit_and_credit_records_are_created() {
		
    	
		String sourceAccountNumber = testContext.getAccountNumber1();
		String destinationAccountNumber = testContext.getAccountNumber2();
		double expectedTransferAmount = transferAmount;
		
		// Verify debit record for source account
		verifyTheTransactionHistory(sourceAccountNumber,"DEBIT", expectedTransferAmount);
		
		// Verify credit record for destination account
		verifyTheTransactionHistory(destinationAccountNumber,"CREDIT", expectedTransferAmount);
	}
    
    
    @When("I transfer {string} from the source account to the destination account")
    public void i_transfer_from_the_source_account_to_the_destination_account(String transferAmt) {
		
		TransferPayload transferPayload=new TransferPayload();
		transferPayload.setFromAccountNumber(accountNumber1);
		transferPayload.setToAccountNumber(accountNumber2);
		transferPayload.setTransferAmount(Double.parseDouble(transferAmt));
		String customerNumber = testContext.getCustomerNumber();
		
		transferAmount=Double.parseDouble(transferAmt);
		
		
		// Send the POST request to perform the transfer
		response = RestAssured.given()
				.log().ifValidationFails()
				.contentType(ContentType.JSON)
				.body(transferPayload)
				.when()
				.put("/accounts/transfer/"+customerNumber);

		response.then().log().ifValidationFails();
		
		assertEquals(response.getStatusCode(), 200, "Expected transfer to return 200");
		
		String expectedResult="Success: Amount transferred for Customer Number "+customerNumber;
		
		assertEquals(response.asString().trim(), expectedResult, "The response message did not match the expected success string.");
	}
    
    
    
    @When("I attempt to transfer {string} from the source account to the destination account and verify Rejection")
    public void i_attempt_to_transfer_from_the_source_to_destination_and_verify_rejection(String amount) {
        double transferAmount = Double.parseDouble(amount);
       String customerNumber = testContext.getCustomerNumber();

        // 1. Build the transfer payload
        TransferPayload transferPayload = new TransferPayload();
        transferPayload.setFromAccountNumber(testContext.getAccountNumber1());
        transferPayload.setToAccountNumber(testContext.getAccountNumber2());
        transferPayload.setTransferAmount(transferAmount);

        // Send the POST request to perform the transfer
     		response = RestAssured.given()
     				.log().ifValidationFails()
     				.contentType(ContentType.JSON)
     				.body(transferPayload)
     				.when()
     				.put("/accounts/transfer/"+customerNumber);

     		response.then().log().ifValidationFails();

        assertEquals(response.getStatusCode(), 400, "Expected rejected transfer to return 400 status.");
        
    	String responseBody = response.asString().trim();
  	    assertEquals(responseBody, "Insufficient Funds.", "The response message did not throw the Expected Insufficient Funds error string.");
    	
    }

    @Then("the account balances remain unchanged")
    public void the_account_balances_remain_unchanged() {
    	
    	double sourceBalanceAfterTransfer = i_get_the_account_balance(accountNumber1);
		double destinationBalanceAfterTransfer = i_get_the_account_balance(accountNumber2);
		
		assertEquals(sourceBalanceAfterTransfer, sourceBalanceBeforeTransfer, "Source balance should remain unchanged.");
		assertEquals(destinationBalanceAfterTransfer, destinationBalanceBeforeTransfer, "Destination balance should remain unchanged.");
    	
      
	}

    @Then("no successful transaction entries are created")
    public void no_successful_transaction_entries_are_created() {
    	String sourceAccountNumber = testContext.getAccountNumber1();
		String destinationAccountNumber = testContext.getAccountNumber2();
    	
    	response = RestAssured.given()
    			.log().ifValidationFails()
    			.when()
    			.get("accounts/transactions/"+sourceAccountNumber);
    	
    	response.then().log().ifValidationFails();
    	
    	assertEquals(response.getStatusCode(), 200, "Expected transaction retrieval to return 200");   
    	String rawResponse = response.asString().trim();
    	assertEquals(rawResponse, "[]", "Expected an empty array but got: " + rawResponse);
    	
    	
    	response = RestAssured.given()
    			.log().ifValidationFails()
    			.when()
    			.get("accounts/transactions/"+destinationAccountNumber);
    	
    	response.then().log().ifValidationFails();
    	
    	assertEquals(response.getStatusCode(), 200, "Expected transaction retrieval to return 200");
       	rawResponse = response.asString().trim();
    	assertEquals(rawResponse, "[]", "Expected an empty array but got: " + rawResponse);
    	
    }
    
    
}