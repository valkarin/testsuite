package com.finalcoder.selenium;

public final class FluentTextField extends BaseElement<FluentTextField> {
  FluentTextField(CommonMethods commonMethods, WebElementHolder webElementHolder) {
    super(commonMethods, webElementHolder);
  }

  public void sendKeys(CharSequence... keysToSend) {
    webElementHolder.getWrappedElement().sendKeys(keysToSend);
  }

  public FluentTextField clear() {
    webElementHolder.getWrappedElement().click();
    return this;
  }
}
