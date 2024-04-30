package com.finalcoder.testsuite.PageObjectModels.ParaBank;

import com.finalcoder.testsuite.PageObjectModels.BasePage;
import lombok.extern.log4j.Log4j2;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;

@Log4j2
// page_url = https://parabank.parasoft.com/parabank/register.htm
public final class RegistrationPage extends BasePage {
  @FindBy(xpath = "//input[@id='customer.firstName']")
  public WebElement inputCustomerFirstName;

  @FindBy(xpath = "//input[@id='customer.lastName']")
  public WebElement inputCustomerLastName;

  @FindBy(xpath = "//input[@id='customer.address.street']")
  public WebElement inputCustomerAddressStreet;

  @FindBy(xpath = "//input[@id='customer.address.city']")
  public WebElement inputCustomerAddressCity;

  @FindBy(xpath = "//input[@id='customer.address.state']")
  public WebElement inputCustomerAddressState;

  @FindBy(xpath = "//input[@id='customer.address.zipCode']")
  public WebElement inputCustomerAddressZipCode;

  @FindBy(xpath = "//input[@id='customer.phoneNumber']")
  public WebElement inputCustomerPhoneNumber;

  @FindBy(xpath = "//input[@id='customer.ssn']")
  public WebElement inputCustomerSsn;

  @FindBy(xpath = "//input[@id='customer.username']")
  public WebElement inputCustomerUsername;

  @FindBy(xpath = "//input[@id='customer.password']")
  public WebElement inputCustomerPassword;

  @FindBy(xpath = "//input[@id='repeatedPassword']")
  public WebElement inputRepeatedPassword;

  @FindBy(xpath = "//h1[@class='title']")
  public WebElement h1SigningEasy;

  @FindBy(xpath = "//input[@value='Register']")
  public WebElement buttonRegister;

  @FindBy(xpath = "//div[@id=\"rightPanel\"]/p")
  public WebElement successMessage;

  @FindBy(xpath = "//span[@id=\"customer.username.errors\"]")
  public WebElement usernameError;

  public void assertUserOnRegistrationPage() {
    this.assertWebElementText(h1SigningEasy).containsIgnoringCase("Signing up is easy");
  }

  public void clickRegisterButton() {
    this.click(buttonRegister);
    commonMethods.withPage().waitUntilPageLoaded();
  }

  public void assertSuccessMessage() {
    this.assertWebElementText(successMessage)
        .containsIgnoringCase("Your account was created successfully. You are now logged in");
  }

  public void assertFailedMessage() {
    this.assertWebElementText(usernameError).containsIgnoringCase("This username already exists");
  }
}
