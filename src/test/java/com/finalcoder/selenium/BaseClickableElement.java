package com.finalcoder.selenium;

import org.openqa.selenium.support.ui.ExpectedConditions;

public abstract class BaseClickableElement<T extends BaseClickableElement<T>>
    extends BaseElement<T> {
  private final CommonMethods commonMethods;

  BaseClickableElement(
      CommonMethods commonMethods, WebElementHolder webElementHolder) {
    super(commonMethods, webElementHolder);
    this.commonMethods = commonMethods;
  }

  @SuppressWarnings("unchecked")
  public T waitUntilClickable() {
    commonMethods.webDriverWait.until(
        ExpectedConditions.elementToBeClickable(webElementHolder.getWrappedElement()));
    return (T) this;
  }

  public void click() {
    webElementHolder.getWrappedElement().click();
  }
}
