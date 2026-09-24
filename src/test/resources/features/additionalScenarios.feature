Feature: Additonal Scenarios

	Scenario: Attempt to retrieve an unknown customer reference
    Given I have a non-existent customer number
    Then I retrieve the customer details using the non-Existent customer number and verify Error   
    
    
    Scenario: Attempt to create an account with a duplicate account number
    Given I have synthetic customer data
    When I create a new customer
    And I add a account to the existing customer
    Then I attempt to add another account using the exact same account number and verify error