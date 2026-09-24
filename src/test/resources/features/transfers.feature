Feature: Account Transfers

  Background:
    Given I have synthetic customer data
    When I create a new customer
    Then Add two accounts to an customer having explicit opening balances

  Scenario: Successful transfer between accounts
    Then I get the balance in source and destination accounts before transfer
    When I transfer "50.50" from the source account to the destination account
    Then verify the balance in both the source and destination accounts is updated correctly
    And verify corresponding debit and credit records are created

  Scenario: Test Get account details API
    Then I get the account details

  Scenario Outline: Rejected transfers
    When I get the balance in source and destination accounts before transfer
    When I attempt to transfer "<amount>" from the source account to the destination account and verify Rejection
    And the account balances remain unchanged
    And no successful transaction entries are created

    Examples:
      | amount | description     |
      | -10.00 | Negative amount |

  Scenario Outline: Rejected transfers
    When I get the balance in source and destination accounts before transfer
    When I attempt to transfer "<amount>" from the source account to the destination account and verify Rejection
    And the account balances remain unchanged
    And no successful transaction entries are created

    Examples:
      | amount  | description                 |
      | 9999.99 | Exceeding available balance |
