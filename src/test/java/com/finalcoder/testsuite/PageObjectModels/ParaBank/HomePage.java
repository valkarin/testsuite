package com.finalcoder.testsuite.PageObjectModels.ParaBank;

import com.finalcoder.testsuite.PageObjectModels.BasePage;
import lombok.extern.log4j.Log4j2;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;

// page_url = https://parabank.parasoft.com/parabank/index.htm
@Log4j2
public final class HomePage extends BasePage {
  @FindBy(xpath = "//a[contains(@href, 'register')]")
  public WebElement linkRegister;

  @FindBy(xpath = "//a[contains(@href, 'overview')]")
  public WebElement linkOverview;

  @FindBy(xpath = "//input[@name='username']")
  public WebElement inputUsername;

  @FindBy(xpath = "//input[@name='password']")
  public WebElement inputPassword;

  @FindBy(xpath = "//input[@value='Log In']")
  public WebElement buttonLogin;

  @FindBy(xpath = "//a[contains(@href, 'transfer')]")
  public WebElement linkTransferFunds;

  public void clickLinkRegister() {
    this.click(linkRegister);
    commonMethods.withPage().waitUntilPageLoaded();
  }

  public void enterUsername(String username) {
    this.fillInput(inputUsername, username);
  }

  public void enterPassword(String password) {
    this.fillInput(inputPassword, password);
  }

  public void clickButtonLogin() {
    this.click(buttonLogin);
    commonMethods.withPage().waitUntilPageLoaded();
  }

  public void clickOverview() {
    this.click(linkOverview);
    commonMethods.withPage().waitUntilPageLoaded();
  }

  public void clickTransferFunds() {
    this.click(linkTransferFunds);
    commonMethods.withPage().waitUntilPageLoaded();
  }
}
