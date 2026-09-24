Feature: Customer Lifecycle Management

  Scenario: Complete customer lifecycle from creation to deletion
    Given I have synthetic customer data
    When I create a new customer
    Then the customer creation is successful
    And I can retrieve the customer details
    When I update the customer's information
    And retrieving the customer shows the updated information
    When I delete the customer
    And retrieving the customer shows that it no longer exists