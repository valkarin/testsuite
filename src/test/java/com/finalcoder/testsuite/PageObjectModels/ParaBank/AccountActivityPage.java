package com.finalcoder.testsuite.PageObjectModels.ParaBank;

import com.finalcoder.testsuite.PageObjectModels.BasePage;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;

public final class AccountActivityPage extends BasePage {
  @FindBy(css = "td[id='balance']")
  public WebElement tdBalance;

  public double getBalance() {
    return Double.parseDouble(
        commonMethods
            .withElement(tdBalance)
            .waitUntilVisible()
            .highlight()
            .getText()
            .replace("$", ""));
  }
}
