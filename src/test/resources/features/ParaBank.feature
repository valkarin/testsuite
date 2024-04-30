Feature: ParaBank

  @UI
  Scenario: Registration with valid inputs
    Given User navigates to "https://parabank.parasoft.com/parabank/index.htm"
    When User clicks on the Register link under Customer Login panel
    Then User is on the registration page
    When User enter the following personal details:
      | Firstname | Lastname | Address      | City     | State | Zipcode | Phone#       | SSN       |
      | Mike      | Doe      | 555 Broadway | New York | NY    | 10019   | 555-555-5555 | 123121234 |
    Then User enter the following account details:
      | Username | Password |
      | MikeDoe  | 123456   |
    And User clicks to Register button
    Then Check if registration successful

  @UI
  Scenario: Attempt to register with an username that is already registered
    Given User navigates to "https://parabank.parasoft.com/parabank/index.htm"
    When User clicks on the Register link under Customer Login panel
    Then User is on the registration page
    When User enter the following personal details:
      | Firstname | Lastname | Address      | City     | State | Zipcode | Phone#       | SSN       |
      | Mike      | Doe      | 555 Broadway | New York | NY    | 10019   | 555-555-5555 | 123121234 |
    Then User enter the following account details:
      | Username | Password |
      | MikeDoe  | 123456   |
    And User clicks to Register button
    Then Check if registration failed

  @UI
  Scenario: Attempt to login with saved username and password
    Given User navigates to "https://parabank.parasoft.com/parabank/index.htm"
    Then User enters saved credentials
    And User click to Log in button
    Then Check if login successful

  @UI
  Scenario Outline: Open a new account
    Given User navigates to "https://parabank.parasoft.com/parabank/index.htm"
    Then User enters saved credentials
    And User click to Log in button
    Then Click on Accounts Overview link
    When User finds an account number that has the highest Available Amount
    Then User clicks to Open New Account link
    And User chooses the account type as "<Account Type>"
    And Chooses highest Available Amount account
    And Clicks Open New Account button
    Then Check if it is successful

    Examples:
      | Account Type |
      | CHECKING     |
      | SAVINGS      |

  @UI
  Scenario Outline: Transfer money to newly created accounts
    Given User navigates to "https://parabank.parasoft.com/parabank/index.htm"
    Then User enters saved credentials
    And User click to Log in button
    Then Click on Accounts Overview link
    When User finds an account number that has the highest Available Amount
    Then User clicks to Open New Account link
    And User chooses the account type as "<Account Type>"
    And Chooses highest Available Amount account
    And Clicks Open New Account button
    And Save new account number
    And Save current balance of the new account
    Then User clicks to Transfer Funds link
    And Transfer $10.00 from Highest Available Amount account to new account
    Then Check if new account's balance changed

    Examples:
      | Account Type |
      | CHECKING     |
      | SAVINGS      |

