package com.finalcoder.testsuite.PageObjectModels.ParaBank;

import com.finalcoder.selenium.CommonMethods;
import com.finalcoder.testsuite.PageObjectModels.BasePage;
import java.util.Collections;
import java.util.Map;
import java.util.stream.Collectors;
import org.apache.commons.lang3.StringUtils;
import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;

// page_url = https://parabank.parasoft.com/parabank/overview.htm
public final class OverviewPage extends BasePage {
  @FindBy(xpath = "//h1[@class='title']")
  public WebElement h1AccountsOverview;

  @FindBy(xpath = "//table[@id='accountTable']")
  public WebElement accountsTable;

  @FindBy(xpath = "//a[contains(@href, 'openaccount')]")
  public WebElement linkOpenNewAccount;

  public void assertLoginSuccessful() {
    this.assertWebElementText(h1AccountsOverview).containsIgnoringCase("Accounts Overview");
  }

  public String getHighestAmountAccountNumber() {
    var accountsTable =
        CommonMethods.withDefaultDriver()
            .withElement(this.accountsTable)
            .waitUntilVisible()
            .highlight()
            .getElement();

    var accountsTableBody = accountsTable.findElement(By.tagName("tbody"));

    // Account // Balance* // Available Amount
    Map<String, Double> accounts =
        accountsTableBody.findElements(By.xpath(".//tr")).stream()
            .filter(element -> !StringUtils.isBlank(element.getAttribute("class")))
            .collect(
                Collectors.toUnmodifiableMap(
                    element -> element.findElements(By.tagName("td")).get(0).getText(),
                    element ->
                        Double.parseDouble(
                            element
                                .findElements(By.tagName("td"))
                                .get(2)
                                .getText()
                                .replace("$", ""))));
    return Collections.max(accounts.entrySet(), Map.Entry.comparingByValue()).getKey();
  }

  public void clickOpenNewAccount() {
    this.click(linkOpenNewAccount);
    commonMethods.withPage().waitUntilPageLoaded();
  }

  public void clickAccount(String accountNumber) {
    commonMethods
        .withLink(By.xpath(String.format("//a[@href='activity.htm?id=%s']", accountNumber)))
        .waitUntilVisible()
        .highlight()
        .click();
    commonMethods.withPage().waitUntilPageLoaded();
  }
}
