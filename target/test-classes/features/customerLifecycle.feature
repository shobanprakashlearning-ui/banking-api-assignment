Feature: Customer Lifecycle Management

  Scenario: Complete customer lifecycle from creation to deletion
    Given I have synthetic customer data
    When I create a new customer
    And I can retrieve the customer details
    When I update the customer's information
    And retrieving the customer shows the updated information
    When I delete the customer
    Then retrieving the customer should show that it no longer exists
    
    Scenario: Complete customer lifecycle from creation to detetion with account
    Given I have synthetic customer data
    When I create a new customer
    And I can retrieve the customer details
    Then Add an account to an customer having explicit opening balances  
    When I delete the customer
    Then retrieving the deleted account should show that it no longer exists