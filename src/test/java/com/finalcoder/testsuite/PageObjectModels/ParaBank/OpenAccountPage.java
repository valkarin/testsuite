package com.finalcoder.testsuite.PageObjectModels.ParaBank;

import com.finalcoder.testsuite.PageObjectModels.BasePage;
import org.assertj.core.api.AssertionsForClassTypes;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;

// page_url = https://parabank.parasoft.com/parabank/openaccount.htm
public final class OpenAccountPage extends BasePage {
  @FindBy(xpath = "//select[@id='type']")
  public WebElement selectAccountType;

  @FindBy(xpath = "//select[@id='fromAccountId']")
  public WebElement selectFromAccount;

  @FindBy(xpath = "//input[@value='Open New Account']")
  public WebElement openNewAccountButton;

  @FindBy(xpath = "//a[@id='newAccountId']")
  public WebElement newAccountLink;

  public void selectAccountType(String accountType) {
    commonMethods
        .withSelect(selectAccountType)
        .waitUntilVisible()
        .highlight()
        .waitUntilItHasMoreOptionsThan(1)
        .selectByVisibleText(accountType);
  }

  public void selectAccount(String accountNumber) {
    commonMethods
        .withSelect(selectFromAccount)
        .waitUntilVisible()
        .highlight()
        .waitUntilItHasOptions()
        .selectByValue(accountNumber);
  }

  public void clickOpenNewAccount() {
    this.click(openNewAccountButton);
    commonMethods.withPage().waitUntilPageLoaded();
  }

  public void assertNewAccountOpened() {
    var isDisplayed =
        commonMethods.withElement(newAccountLink).waitUntilVisible().getElement().isDisplayed();
    AssertionsForClassTypes.assertThat(isDisplayed).isTrue();
  }

  public String getNewAccountNumber() {
    return commonMethods.withElement(newAccountLink).waitUntilVisible().highlight().getText();
  }

  public void clickNewAccountLink() {
    this.click(newAccountLink);
    commonMethods.withPage().waitUntilPageLoaded();
  }
}
