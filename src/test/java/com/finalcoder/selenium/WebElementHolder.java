package com.finalcoder.selenium;

import lombok.Getter;
import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.WrapsElement;

public final class WebElementHolder implements WrapsElement {
  private final CommonMethods commonMethods;
  private WebElement foundElement;
  @Getter final By locator;

  WebElementHolder(CommonMethods commonMethods, WebElement foundElement, By locator) {
    this.commonMethods = commonMethods;
    this.foundElement = foundElement;
    this.locator = locator;
  }

  WebElementHolder(CommonMethods commonMethods, WebElement foundElement) {
    this(commonMethods, foundElement, null);
  }

  WebElementHolder(CommonMethods commonMethods, By locator) {
    this(commonMethods, null, locator);
  }

  public boolean isElementFound() {
    return foundElement != null;
  }

  private void findElement() {
    if (this.locator == null) return;
    foundElement = commonMethods.driver.findElement(locator);
  }

  @Override
  public WebElement getWrappedElement() {
    if (foundElement == null) findElement();
    return foundElement;
  }
}
