package com.finalcoder.selenium;

import com.finalcoder.testsuite.Common.DriverManager;
import com.finalcoder.testsuite.Utils.Exceptions.CommonMethodsException;

import java.time.Duration;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Objects;
import java.util.concurrent.atomic.AtomicBoolean;

import lombok.SneakyThrows;
import lombok.extern.log4j.Log4j2;
import org.openqa.selenium.By;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chromium.ChromiumDriver;
import org.openqa.selenium.devtools.Command;
import org.openqa.selenium.devtools.Event;
import org.openqa.selenium.devtools.v85.page.model.DownloadProgress;
import org.openqa.selenium.support.ui.ExpectedCondition;
import org.openqa.selenium.support.ui.Sleeper;
import org.openqa.selenium.support.ui.WebDriverWait;

@Log4j2
@SuppressWarnings({"UnusedReturnValue", "unused"})
public final class CommonMethods {
  final WebDriver driver;
  final JavascriptExecutor javascriptExecutor;
  WebDriverWait webDriverWait;
  static final Duration WAIT_TIMEOUT = Duration.ofSeconds(30);
  static final Sleeper sleeper = Sleeper.SYSTEM_SLEEPER;

  private CommonMethods(WebDriver webDriver) {
    this.driver = webDriver;
    this.javascriptExecutor = (JavascriptExecutor) driver;
    this.webDriverWait = new WebDriverWait(driver, WAIT_TIMEOUT);
  }

  private CommonMethods() {
    driver = DriverManager.SelectedWebDriverManager.getWebDriver();
    javascriptExecutor = (JavascriptExecutor) driver;
    this.webDriverWait = new WebDriverWait(driver, WAIT_TIMEOUT);
  }

  public static CommonMethods withDefaultDriver() {
    return new CommonMethods();
  }

  public static CommonMethods withDriver(WebDriver driver) {
    return new CommonMethods(driver);
  }

  /**
   * This method retries a lambda function giving exception repeatedly until the time runs out or
   * the method stops giving exception. When the time runs out, this method will rethrow the latest
   * exception.
   *
   * <p>Example Usage:
   *
   * <pre>
   *      CommonMethods.RetryUntil(()-> {
   *          driver.findElement(By.xpath("//UnknownElement"));
   *      }, Duration.ofSeconds(5));
   * </pre>
   *
   * @param runnable Lambda function that will be run repeatedly
   * @param timeout For how long the lambda function needs to be retried.
   */
  @SneakyThrows
  public static void RetryUntil(RunnableVoid runnable, Duration timeout) {
    var again = true;
    var start = System.currentTimeMillis();
    var end = start + timeout.toMillis();
    Throwable ex = null;
    while (again && System.currentTimeMillis() < end) {
      ex = null;
      try {
        runnable.run();
        again = false;
      } catch (Throwable e) {
        ex = e;
      }

      try {
        sleeper.sleep(Duration.ofMillis(300));
      } catch (InterruptedException var6) {
        Thread.currentThread().interrupt();
        throw new CommonMethodsException(var6);
      }
    }
    if (ex != null) {
      throw ex;
    }
  }

  /**
   * This method retries a lambda function giving exception repeatedly until the time runs out or
   * the method stops giving exception. When the time runs out, this method will rethrow the latest
   * exception.
   *
   * <p>Example Usage:
   *
   * <pre>
   *      String text = CommonMethods.RetryUntil(() ->
   *          driver.findElement(By.xpath("//UnknownElement")).getText(),
   *          Duration.ofSeconds(5));
   * </pre>
   *
   * @param runnable Lambda function that will be run repeatedly
   * @param timeout For how long the lambda function needs to be retried.
   * @param <T> Return type of the lambda function
   * @return Result of the lambda function
   */
  @SneakyThrows
  public static <T> T RetryUntil(Runnable<T> runnable, Duration timeout) {
    var again = true;
    var start = System.currentTimeMillis();
    var end = start + timeout.toMillis();
    Throwable ex = null;
    T returnValue = null;
    while (again && System.currentTimeMillis() < end) {
      ex = null;
      try {
        returnValue = runnable.run();
        again = false;
      } catch (Throwable e) {
        ex = e;
      }

      try {
        sleeper.sleep(Duration.ofMillis(300));
      } catch (InterruptedException var6) {
        Thread.currentThread().interrupt();
        throw new CommonMethodsException(var6);
      }
    }
    if (returnValue != null) return returnValue;
    if (ex != null) {
      throw ex;
    }
    return null;
  }

  /**
   * This method overrides the default timeout.
   *
   * @param duration Duration of the new timeout
   */
  public CommonMethods withDuration(Duration duration) {
    webDriverWait = (WebDriverWait) webDriverWait.withTimeout(duration);
    return this;
  }

  public void resetDuration() {
    webDriverWait = (WebDriverWait) webDriverWait.withTimeout(WAIT_TIMEOUT);
  }

  public FluentButton withButton(WebElement element) {
    return new FluentButton(this, new WebElementHolder(this, element));
  }

  public FluentButton withButton(By locator) {
    return new FluentButton(this, new WebElementHolder(this, locator));
  }

  public FluentLink withLink(WebElement element) {
    return new FluentLink(this, new WebElementHolder(this, element));
  }

  public FluentLink withLink(By locator) {
    return new FluentLink(this, new WebElementHolder(this, locator));
  }

  public FluentTextField withTextField(WebElement element) {
    return new FluentTextField(this, new WebElementHolder(this, element));
  }

  public FluentTextField withTextField(By locator) {
    return new FluentTextField(this, new WebElementHolder(this, locator));
  }

  public FluentElement withElement(WebElement element) {
    return new FluentElement(this, new WebElementHolder(this, element));
  }

  public FluentElement withElement(By locator) {
    return new FluentElement(this, new WebElementHolder(this, locator));
  }

  public FluentSelect withSelect(WebElement element) {
    return new FluentSelect(this, new WebElementHolder(this, element));
  }

  public FluentSelect withSelect(By locator) {
    return new FluentSelect(this, new WebElementHolder(this, locator));
  }

  public FluentPage withPage() {
    return new FluentPage(this);
  }

  public void navigate(String url) {
    if (driver instanceof ChromiumDriver chromiumDriver) {
      try {
        var devtools = chromiumDriver.getDevTools();
        devtools.createSessionIfThereIsNotOne();
        chromiumDriver.get(url);
        var params = new LinkedHashMap<String, Object>();
        params.put("behavior", "allowAndName");
        params.put("downloadPath", "");
        params.put("eventsEnabled", true);
        AtomicBoolean completed = new AtomicBoolean(false);
        devtools.send(new Command<>("Browser.setDownloadBehavior", Map.copyOf(params)));
        devtools.addListener(
            new Event<DownloadProgress>("Browser.downloadProgress", input -> input.read(DownloadProgress.class)),
            e -> completed.set(Objects.equals(e.getState().toString(), "completed")));
        webDriverWait.until(_d -> completed);
      } catch (Exception e) {
        log.error("There was an exception while navigating", e);
      }
    } else {
      driver.navigate().to(url);
    }
  }

  public void waitUntil(ExpectedCondition<?> condition) {
    webDriverWait.until(condition);
  }

  /** Runnable that doesn't return value */
  @FunctionalInterface
  public interface RunnableVoid {
    void run() throws Throwable;
  }

  /**
   * Runnable that returns value
   *
   * @param <T> Return type of Runnable
   */
  @FunctionalInterface
  public interface Runnable<T> {
    T run() throws Throwable;
  }
}
