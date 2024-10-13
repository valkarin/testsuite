package com.finalcoder.testsuite.PageObjectModels;

import static org.assertj.core.api.Assertions.assertThat;

import com.finalcoder.selenium.CommonMethods;
import com.finalcoder.testsuite.Common.DriverManager;
import org.assertj.core.api.AbstractStringAssert;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.PageFactory;

public abstract class BasePage {
  protected final WebDriver driver;
  protected final CommonMethods commonMethods;

  public BasePage(WebDriver driver) {
    this.driver = driver;
    this.commonMethods = CommonMethods.withDriver(driver);
    PageFactory.initElements(driver, this);
  }

  public BasePage() {
    this(DriverManager.SelectedWebDriverManager.getWebDriver());
  }

  public void fillInput(WebElement element, String value) {
    this.commonMethods
        .withTextField(element)
        .waitUntilVisible()
        .highlight()
        .clear()
        .sendKeys(value);
  }

  public void click(WebElement element) {
    this.commonMethods.withButton(element).waitUntilClickable().highlight().click();
  }

  public AbstractStringAssert<?> assertWebElementText(WebElement element) {
    var elementText =
        CommonMethods.withDefaultDriver()
            .withElement(element)
            .waitUntilVisible()
            .getElement()
            .getText();
    return assertThat(elementText);
  }
}
