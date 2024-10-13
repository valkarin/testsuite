package com.finalcoder.selenium;

import com.finalcoder.testsuite.Common.CommonConditions;
import com.google.common.collect.Maps;
import org.openqa.selenium.InvalidArgumentException;
import org.openqa.selenium.OutputType;
import org.openqa.selenium.TakesScreenshot;
import org.openqa.selenium.chromium.ChromiumDriver;
import org.openqa.selenium.firefox.FirefoxDriver;
import org.openqa.selenium.support.ui.ExpectedConditions;

import java.util.Base64;
import java.util.Map;

public class FluentPage {
  private final CommonMethods commonMethods;

  FluentPage(CommonMethods commonMethods) {
    this.commonMethods = commonMethods;
  }

  public void waitUntilPageLoaded() {
    commonMethods.webDriverWait.until(CommonConditions.pageLoads());
  }

  public void waitUntilTitleIs(String title) {
    commonMethods.webDriverWait.until(ExpectedConditions.titleIs(title));
  }

  public void waitUntilTitleContains(String title) {
    commonMethods.webDriverWait.until(ExpectedConditions.titleContains(title));
  }

  /**
   * @param fullscreen Whether to take a fullscreen screenshot of the page
   * @return screenshot as a byte array
   */
  public byte[] takeScreenshot(boolean fullscreen) {
    if (commonMethods.driver instanceof ChromiumDriver chromiumDriver) {
      if (fullscreen) {
        Map<String, Object> screenshotConfig = Maps.newHashMap();
        screenshotConfig.put("captureBeyondViewport", true);
        screenshotConfig.put("fromSurface", true);
        Map<String, Object> base64PngResult =
            chromiumDriver.executeCdpCommand("Page.captureScreenshot", screenshotConfig);
        return Base64.getDecoder().decode(((String) base64PngResult.get("data")).getBytes());
      }
    } else if (commonMethods.driver instanceof FirefoxDriver firefoxDriver) {
      if (fullscreen) {
        return firefoxDriver.getFullPageScreenshotAs(OutputType.BYTES);
      }
    }
    if (fullscreen) {
      throw new InvalidArgumentException("Cannot take a fullscreen screenshot with this driver");
    }
    return ((TakesScreenshot) commonMethods.driver).getScreenshotAs(OutputType.BYTES);
  }
}
