package com.finalcoder.testsuite.StepDefinitions.ParaBank;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;

import com.finalcoder.selenium.CommonMethods;
import com.finalcoder.testsuite.Common.Constants;
import com.finalcoder.testsuite.Common.Models.Credentials;
import com.finalcoder.testsuite.Common.ScenarioContext;
import com.finalcoder.testsuite.Common.TestContext;
import com.finalcoder.testsuite.PageObjectModels.ParaBank.*;
import com.finalcoder.testsuite.StepDefinitions.BaseStepDefinition;
import io.cucumber.java.en.And;
import io.cucumber.java.en.Then;
import io.cucumber.java.en.When;
import io.qameta.allure.Allure;
import java.time.Duration;
import java.util.List;
import java.util.Map;
import lombok.extern.log4j.Log4j2;

@Log4j2
public final class ParaBankStepDefinitions extends BaseStepDefinition {

  private final HomePage homePage;
  private final RegistrationPage registrationPage;
  private final OverviewPage overviewPage;
  private final OpenAccountPage openAccountPage;
  private final AccountActivityPage accountActivityPage;
  private final TransferFundsPage transferFundsPage;
  private final TestContext testContext;
  private final ScenarioContext scenarioContext;

  public ParaBankStepDefinitions(
      HomePage homePage,
      RegistrationPage registrationPage,
      OverviewPage overviewPage,
      OpenAccountPage openAccountPage,
      AccountActivityPage accountActivityPage,
      TransferFundsPage transferFundsPage,
      TestContext testContext,
      ScenarioContext scenarioContext) {
    this.homePage = homePage;
    this.registrationPage = registrationPage;
    this.overviewPage = overviewPage;
    this.openAccountPage = openAccountPage;
    this.accountActivityPage = accountActivityPage;
    this.transferFundsPage = transferFundsPage;
    this.testContext = testContext;
    this.scenarioContext = scenarioContext;
  }

  @When("User clicks on the Register link under Customer Login panel")
  public void userClicksOnTheRegisterLinkUnderCustomerLoginPanel() {
    homePage.clickLinkRegister();
  }

  @Then("User is on the registration page")
  public void userIsOnTheRegistrationPage() {
    registrationPage.assertUserOnRegistrationPage();
  }

  @When("User enter the following personal details:")
  public void userEnterTheFollowingPersonalDetails(List<Map<String, String>> personalDetails) {
    for (Map<String, String> personalDetail : personalDetails) {
      String firstname = personalDetail.get("Firstname");
      Allure.step(
          "Enter Firstname as " + firstname,
          () -> registrationPage.fillInput(registrationPage.inputCustomerFirstName, firstname));
      String lastname = personalDetail.get("Lastname");
      Allure.step(
          "Enter Lastname as " + lastname,
          () -> registrationPage.fillInput(registrationPage.inputCustomerLastName, lastname));
      String address = personalDetail.get("Address");
      Allure.step(
          "Enter Address as " + address,
          () -> registrationPage.fillInput(registrationPage.inputCustomerAddressStreet, address));
      String city = personalDetail.get("City");
      Allure.step(
          "Enter City as " + city,
          () -> registrationPage.fillInput(registrationPage.inputCustomerAddressCity, city));
      String state = personalDetail.get("State");
      Allure.step(
          "Enter State as " + state,
          () -> registrationPage.fillInput(registrationPage.inputCustomerAddressState, state));
      String zipcode = personalDetail.get("Zipcode");
      Allure.step(
          "Enter Zipcode as " + zipcode,
          () -> registrationPage.fillInput(registrationPage.inputCustomerAddressZipCode, zipcode));
      String phone = personalDetail.get("Phone#");
      Allure.step(
          "Enter Phone as " + phone,
          () -> registrationPage.fillInput(registrationPage.inputCustomerPhoneNumber, phone));
      String ssn = personalDetail.get("SSN");
      Allure.step(
          "Enter SSN as " + ssn,
          () -> registrationPage.fillInput(registrationPage.inputCustomerSsn, ssn));
    }
  }

  @Then("User enter the following account details:")
  public void userEnterTheFollowingAccountDetails(List<Map<String, String>> accountDetails) {
    for (Map<String, String> accountDetail : accountDetails) {
      String username = accountDetail.get("Username");
      Allure.step(
          "Enter Username as " + username,
          () -> registrationPage.fillInput(registrationPage.inputCustomerUsername, username));
      String password = accountDetail.get("Password");
      Allure.step(
          "Enter Password as " + password,
          () -> registrationPage.fillInput(registrationPage.inputCustomerPassword, password));
      Allure.step(
          "Enter Confirm Password as " + password,
          () -> registrationPage.fillInput(registrationPage.inputRepeatedPassword, password));
      this.testContext
          .getDataStorage()
          .put(Constants.ParaBank.CREDENTIALS_KEY, new Credentials(username, password));
    }
  }

  @And("User clicks to Register button")
  public void userClicksToRegisterButton() {
    registrationPage.clickRegisterButton();
  }

  @Then("Check if registration successful")
  public void checkIfRegistrationSuccessful() {
    takeScreenshot(true);
    registrationPage.assertSuccessMessage();
  }

  @Then("Check if registration failed")
  public void checkIfRegistrationFailed() {
    takeScreenshot(true);
    registrationPage.assertFailedMessage();
  }

  @Then("User enters saved credentials")
  public void userEntersSavedCredentials() {
    var credentials =
        this.testContext
            .getDataStorage()
            .get(Constants.ParaBank.CREDENTIALS_KEY, Credentials.class);
    assertThat(credentials).isNotNull();
    assert credentials != null;
    Allure.addAttachment("Credentials", "text/plain", credentials.toString());
    homePage.enterUsername(credentials.username());
    homePage.enterPassword(credentials.password());
  }

  @And("User click to Log in button")
  public void userClickToLogInButton() {
    homePage.clickButtonLogin();
  }

  @Then("Check if login successful")
  public void checkIfLoginSuccessful() {
    takeScreenshot(true);
    overviewPage.assertLoginSuccessful();
  }

  @When("User finds an account number that has the highest Available Amount")
  public void userFindsAnAccountNumberThatHasTheHighestAvailableAmount() {
    var accountNumber =
        CommonMethods.RetryUntil(
            overviewPage::getHighestAmountAccountNumber, Duration.ofSeconds(5));
    this.scenarioContext
        .getDataStorage()
        .put(Constants.ParaBank.HIGHEST_AMOUNT_ACCOUNT_KEY, accountNumber);
  }

  @Then("User clicks to Open New Account link")
  public void userClicksToOpenNewAccount() {
    overviewPage.clickOpenNewAccount();
  }

  @And("User chooses the account type as {string}")
  public void userChoosesTheAccountTypeAs(String accountType) {
    openAccountPage.selectAccountType(accountType);
  }

  @And("Chooses highest Available Amount account")
  public void choosesHighestAvailableAmountAccount() {
    var accountNumber =
        this.scenarioContext
            .getDataStorage()
            .getString(Constants.ParaBank.HIGHEST_AMOUNT_ACCOUNT_KEY);
    openAccountPage.selectAccount(accountNumber);
  }

  @And("Clicks Open New Account button")
  public void clicksOpenNewAccountButton() {
    openAccountPage.clickOpenNewAccount();
  }

  @Then("Check if it is successful")
  public void checkIfItIsSuccessful() {
    takeScreenshot(true);
    openAccountPage.assertNewAccountOpened();
  }

  @Then("Click on Accounts Overview link")
  public void clickOnOverviewLink() {
    homePage.clickOverview();
  }

  @And("Save new account number")
  public void saveNewAccountNumber() {
    scenarioContext
        .getDataStorage()
        .put(Constants.ParaBank.NEW_ACCOUNT_KEY, openAccountPage.getNewAccountNumber());
  }

  @And("Save current balance of the new account")
  public void saveCurrentBalanceOfTheNewAccount() {
    openAccountPage.clickNewAccountLink();
    CommonMethods.withDefaultDriver().withPage().waitUntilPageLoaded();
    var currentBalance =
        CommonMethods.RetryUntil(accountActivityPage::getBalance, Duration.ofSeconds(5));
    scenarioContext
        .getDataStorage()
        .put(Constants.ParaBank.NEW_ACCOUNT_BALANCE_KEY, currentBalance);
    this.takeScreenshot("New Account Balance", true);
  }

  @Then("User clicks to Transfer Funds link")
  public void userClicksToTransferFundsLink() {
    homePage.clickTransferFunds();
  }

  @And("Transfer ${double} from Highest Available Amount account to new account")
  public void transfer$FromHighestAvailableAmountToNewAccount(double amount) {
    scenarioContext.getDataStorage().put(Constants.ParaBank.TRANSFER_AMOUNT_KEY, amount);
    Allure.step(
        String.format("Enter transfer amount of $%s", amount),
        () -> transferFundsPage.enterAmount(amount));
    var highestAmountAccount =
        scenarioContext.getDataStorage().getString(Constants.ParaBank.HIGHEST_AMOUNT_ACCOUNT_KEY);
    assertThat(highestAmountAccount).isNotBlank().doesNotContainOnlyWhitespaces();
    var newAccount = scenarioContext.getDataStorage().getString(Constants.ParaBank.NEW_ACCOUNT_KEY);
    assertThat(newAccount).isNotBlank().doesNotContainOnlyWhitespaces();
    Allure.step(
        String.format("Select #%s to transfer from", highestAmountAccount),
        () ->
            CommonMethods.RetryUntil(
                () -> transferFundsPage.selectFromAccount(highestAmountAccount),
                Duration.ofSeconds(5)));
    Allure.step(
        String.format("Select #%s to transfer to", newAccount),
        () ->
            CommonMethods.RetryUntil(
                () -> transferFundsPage.selectToAccount(newAccount), Duration.ofSeconds(5)));
    Allure.step("Click transfer button", transferFundsPage::clickTransferButton);
  }

  @Then("Check if new account's balance changed")
  public void checkIfNewAccountSBalanceChanged() {
    homePage.clickOverview();
    var newAccountNumber =
        scenarioContext.getDataStorage().getString(Constants.ParaBank.NEW_ACCOUNT_KEY);
    Allure.step(
        String.format("Click on #%s account in overview page", newAccountNumber),
        () -> overviewPage.clickAccount(newAccountNumber));
    var initialBalance =
        scenarioContext.getDataStorage().getDouble(Constants.ParaBank.NEW_ACCOUNT_BALANCE_KEY);
    var amountAdded =
        scenarioContext.getDataStorage().getDouble(Constants.ParaBank.TRANSFER_AMOUNT_KEY);
    Allure.step(
        String.format("Compare initial balance of $%s with added $%s", initialBalance, amountAdded),
        () -> {
          var currentBalance =
              CommonMethods.RetryUntil(accountActivityPage::getBalance, Duration.ofSeconds(5));
          assertThat(initialBalance + amountAdded).isEqualTo(currentBalance);
        });
    this.takeScreenshot("Latest Account Balance", true);
  }
}
