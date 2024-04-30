package com.finalcoder.testsuite.Common;

import java.util.List;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;
import org.openqa.selenium.By;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.WebDriverException;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.ExpectedCondition;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public final class CommonConditions {
  public static ExpectedCondition<Boolean> pageLoads() {
    return driver -> {
      try {
        // when this returns "complete"
        // it means the page is loaded
        final String javaScript = "return document.readyState";
        var javascriptExecutor = (JavascriptExecutor) driver;
        if (javascriptExecutor == null) throw new WebDriverException("JavaScript executor is null");
        Object value = javascriptExecutor.executeScript(javaScript);

        if (!(value instanceof String result)) return null;

        if (result.isBlank() || result.isEmpty()) return false;

        return result.equalsIgnoreCase("complete");
      } catch (WebDriverException e) {
        return null;
      }
    };
  }

  public static ExpectedCondition<List<WebElement>> numberOfElementsWithInToBeMoreThan(
      final WebElement element, final By locator, final Integer number) {
    return webDriver -> {
      List<WebElement> elements = element.findElements(locator);
      int currentNumber = elements.size();
      return currentNumber > number ? elements : null;
    };
  }
}
