package com.finalcoder.selenium;

import com.finalcoder.testsuite.Common.CommonConditions;
import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.Select;

import java.util.List;

public final class FluentSelect extends BaseElement<FluentSelect> {
  private final CommonMethods commonMethods;
  private Select selectElement;

  FluentSelect(
      CommonMethods commonMethods, WebElementHolder webElementHolder) {
    super(commonMethods, webElementHolder);
    this.commonMethods = commonMethods;
  }

  private Select getSelectElement() {
    if (selectElement == null) {
      selectElement = new Select(webElementHolder.getWrappedElement());
    }
    return selectElement;
  }

  public FluentSelect waitUntilItHasOptions() {
    this.waitUntilItHasMoreOptionsThan(0);
    return this;
  }

  public FluentSelect waitUntilItHasMoreOptionsThan(Integer number) {
    commonMethods.webDriverWait.until(
        CommonConditions.numberOfElementsWithInToBeMoreThan(
            webElementHolder.getWrappedElement(), By.xpath(".//option"), number));
    return this;
  }

  public boolean isMultiSelect() {
    return this.getSelectElement().isMultiple();
  }

  public FluentSelect selectByIndex(int index) {
    this.getSelectElement().selectByIndex(index);
    return this;
  }

  public FluentSelect selectByValue(String value) {
    this.getSelectElement().selectByValue(value);
    return this;
  }

  public FluentSelect selectByVisibleText(String text) {
    this.getSelectElement().selectByVisibleText(text);
    return this;
  }

  public WebElement getSelectedFirstElement() {
    return this.getSelectElement().getFirstSelectedOption();
  }

  public List<WebElement> getAllSelectedElements() {
    return this.getSelectElement().getAllSelectedOptions();
  }

  public List<WebElement> getOptions() {
    return this.getSelectElement().getOptions();
  }

  public FluentSelect deselectByIndex(int index) {
    this.getSelectElement().deselectByIndex(index);
    return this;
  }

  public FluentSelect deselectByValue(String value) {
    this.getSelectElement().deselectByValue(value);
    return this;
  }

  public FluentSelect deselectByVisibleText(String text) {
    this.getSelectElement().deselectByVisibleText(text);
    return this;
  }

  public FluentSelect deselectAll() {
    this.getSelectElement().deselectAll();
    return this;
  }
}
