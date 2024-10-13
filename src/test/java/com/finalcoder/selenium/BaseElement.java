package com.finalcoder.selenium;

import org.openqa.selenium.WebElement;
import org.openqa.selenium.interactions.Actions;
import org.openqa.selenium.support.ui.ExpectedConditions;

/**
 * This abstract class holds methods or fields that will be shared by all elements.
 *
 * @param <T> T is the class extending BaseElement
 */
public abstract class BaseElement<T extends BaseElement<T>> {
  private final CommonMethods commonMethods;
  protected final WebElementHolder webElementHolder;

  BaseElement(CommonMethods commonMethods, WebElementHolder webElementHolder) {
    this.commonMethods = commonMethods;
    this.webElementHolder = webElementHolder;
  }

  @SuppressWarnings("unchecked")
  public T highlight() {
    commonMethods.javascriptExecutor.executeScript("console.log(\"Element Highlighted.\")");
    var attribute = webElementHolder.getWrappedElement().getAttribute("style");
    attribute = attribute + " background: yellow !important; border: 3px solid red !important;";
    commonMethods.javascriptExecutor.executeScript(
        "arguments[0].setAttribute('style', arguments[1])",
        webElementHolder.getWrappedElement(),
        attribute);
    return (T) this;
  }

  @SuppressWarnings("unchecked")
  public T waitUntilVisible() {
    if (webElementHolder.isElementFound())
      commonMethods.webDriverWait.until(
          ExpectedConditions.visibilityOf(webElementHolder.getWrappedElement()));
    else
      commonMethods.webDriverWait.until(
          ExpectedConditions.presenceOfElementLocated(webElementHolder.locator));
    return (T) this;
  }

  public T scrollToElement() {
    new Actions(commonMethods.driver).build().perform();
    var location = webElementHolder.getWrappedElement().getLocation();
    commonMethods.javascriptExecutor.executeScript(
        String.format("window.scrollTo(%d,%d);", location.x, location.y));
      return (T) this;
  }

  public T scrollIntoView() {
    var element = webElementHolder.getWrappedElement();
    commonMethods.javascriptExecutor
            .executeScript("arguments[0].scrollIntoView({behavior: 'instant'});", element);
    return (T) this;
  }

  public WebElement getElement() {
    return webElementHolder.getWrappedElement();
  }

  public String getText() {
    return this.webElementHolder.getWrappedElement().getText();
  }
}
