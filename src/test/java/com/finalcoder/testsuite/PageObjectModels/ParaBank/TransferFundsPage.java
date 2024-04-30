package com.finalcoder.testsuite.PageObjectModels.ParaBank;

import com.finalcoder.testsuite.PageObjectModels.BasePage;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;

// page_url = https://parabank.parasoft.com/parabank/transfer.htm
public final class TransferFundsPage extends BasePage {
  @FindBy(css = "input[id='amount']")
  public WebElement inputAmount;

  @FindBy(css = "select[id='fromAccountId']")
  public WebElement selectFromAccount;

  @FindBy(css = "select[id='toAccountId']")
  public WebElement selectToAccount;

  @FindBy(css = "input[value='Transfer']")
  public WebElement transferButton;

  public void clickTransferButton() {
    this.click(transferButton);
    commonMethods.withPage().waitUntilPageLoaded();
  }

  public void enterAmount(Double amount) {
    this.fillInput(inputAmount, amount.toString());
  }

  public void selectFromAccount(String fromAccount) {
    commonMethods
        .withSelect(selectFromAccount)
        .waitUntilVisible()
        .highlight()
        .waitUntilItHasOptions()
        .selectByVisibleText(fromAccount);
  }

  public void selectToAccount(String toAccount) {
    commonMethods
        .withSelect(selectToAccount)
        .waitUntilVisible()
        .highlight()
        .waitUntilItHasOptions()
        .selectByVisibleText(toAccount);
  }
}
