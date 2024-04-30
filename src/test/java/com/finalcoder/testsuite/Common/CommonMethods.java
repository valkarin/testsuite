package com.finalcoder.testsuite.Common;

import com.finalcoder.testsuite.Utils.Exceptions.CommonMethodsException;
import com.google.common.collect.Maps;
import java.time.Duration;
import java.util.*;
import java.util.concurrent.atomic.AtomicBoolean;
import lombok.Getter;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.openqa.selenium.*;
import org.openqa.selenium.chromium.ChromiumDriver;
import org.openqa.selenium.devtools.HasDevTools;
import org.openqa.selenium.devtools.v122.browser.Browser;
import org.openqa.selenium.firefox.FirefoxDriver;
import org.openqa.selenium.support.ui.*;

@Slf4j
@SuppressWarnings({"UnusedReturnValue", "unused"})
public final class CommonMethods {
  private final WebDriver driver;
  private final JavascriptExecutor javascriptExecutor;
  private WebDriverWait webDriverWait;
  private static final Duration WAIT_TIMEOUT = Duration.ofSeconds(30);
  private static final Sleeper sleeper = Sleeper.SYSTEM_SLEEPER;

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
    return new FluentButton(new WebElementHolder(element));
  }

  public FluentButton withButton(By locator) {
    return new FluentButton(new WebElementHolder(locator));
  }

  public FluentLink withLink(WebElement element) {
    return new FluentLink(new WebElementHolder(element));
  }

  public FluentLink withLink(By locator) {
    return new FluentLink(new WebElementHolder(locator));
  }

  public FluentTextField withTextField(WebElement element) {
    return new FluentTextField(new WebElementHolder(element));
  }

  public FluentTextField withTextField(By locator) {
    return new FluentTextField(new WebElementHolder(locator));
  }

  public FluentElement withElement(WebElement element) {
    return new FluentElement(new WebElementHolder(element));
  }

  public FluentElement withElement(By locator) {
    return new FluentElement(new WebElementHolder(locator));
  }

  public FluentSelect withSelect(WebElement element) {
    return new FluentSelect(new WebElementHolder(element));
  }

  public FluentSelect withSelect(By locator) {
    return new FluentSelect(new WebElementHolder(locator));
  }

  public FluentPage withPage() {
    return new FluentPage();
  }

  public void navigate(String url) {
    driver.get(url);
    if (!(driver instanceof HasDevTools devToolDriver)) return;
    var devtools = devToolDriver.getDevTools();
    devtools.createSession();
    devtools.send(
        Browser.setDownloadBehavior(
            Browser.SetDownloadBehaviorBehavior.ALLOWANDNAME,
            Optional.empty(),
            Optional.of(""),
            Optional.of(true)));
    AtomicBoolean completed = new AtomicBoolean(false);
    devtools.addListener(
        Browser.downloadProgress(),
        e -> completed.set(Objects.equals(e.getState().toString(), "completed")));
    webDriverWait.until(_d -> completed);
    devtools.disconnectSession();
  }

  public void waitUntil(ExpectedCondition<?> condition) {
    webDriverWait.until(condition);
  }

  protected final class WebElementHolder implements WrapsElement {
    private WebElement foundElement;
    @Getter private final By locator;

    private WebElementHolder(WebElement foundElement, By locator) {
      // this.parent = parent;
      this.foundElement = foundElement;
      this.locator = locator;
    }

    private WebElementHolder(WebElement foundElement) {
      this(foundElement, null);
    }

    private WebElementHolder(By locator) {
      this(null, locator);
    }

    public boolean isElementFound() {
      return foundElement != null;
    }

    private void findElement() {
      if (this.locator == null) return;
      foundElement = driver.findElement(locator);
    }

    @Override
    public WebElement getWrappedElement() {
      if (foundElement == null) findElement();
      return foundElement;
    }
  }

  /**
   * This abstract class holds methods or fields that will be shared by all elements.
   *
   * @param <T> T is the class extending BaseElement
   */
  protected abstract class BaseElement<T extends BaseElement<T>> {
    protected final WebElementHolder webElementHolder;

    public BaseElement(WebElementHolder webElementHolder) {
      this.webElementHolder = webElementHolder;
    }

    @SuppressWarnings("unchecked")
    public T highlight() {
      javascriptExecutor.executeScript("console.log(\"Element Highlighted.\")");
      var attribute = webElementHolder.getWrappedElement().getAttribute("style");
      attribute = attribute + " background: yellow !important; border: 3px solid red !important;";
      javascriptExecutor.executeScript(
          "arguments[0].setAttribute('style', arguments[1])",
          webElementHolder.getWrappedElement(),
          attribute);
      return (T) this;
    }

    @SuppressWarnings("unchecked")
    public T waitUntilVisible() {
      if (webElementHolder.isElementFound())
        webDriverWait.until(ExpectedConditions.visibilityOf(webElementHolder.getWrappedElement()));
      else
        webDriverWait.until(ExpectedConditions.presenceOfElementLocated(webElementHolder.locator));
      return (T) this;
    }

    public WebElement getElement() {
      return webElementHolder.getWrappedElement();
    }

    public String getText() {
      return this.webElementHolder.getWrappedElement().getText();
    }
  }

  protected abstract class BaseClickableElement<T extends BaseClickableElement<T>>
      extends BaseElement<T> {
    public BaseClickableElement(WebElementHolder webElementHolder) {
      super(webElementHolder);
    }

    @SuppressWarnings("unchecked")
    public T waitUntilClickable() {
      webDriverWait.until(
          ExpectedConditions.elementToBeClickable(webElementHolder.getWrappedElement()));
      return (T) this;
    }

    public void click() {
      webElementHolder.getWrappedElement().click();
    }
  }

  public final class FluentElement extends BaseElement<FluentElement> {
    private FluentElement(WebElementHolder webElementHolder) {
      super(webElementHolder);
    }
  }

  public final class FluentButton extends BaseClickableElement<FluentButton> {
    private FluentButton(WebElementHolder webElementHolder) {
      super(webElementHolder);
    }
  }

  public final class FluentLink extends BaseClickableElement<FluentLink> {
    private FluentLink(WebElementHolder webElementHolder) {
      super(webElementHolder);
    }
  }

  public final class FluentTextField extends BaseElement<FluentTextField> {
    private FluentTextField(WebElementHolder webElementHolder) {
      super(webElementHolder);
    }

    public void sendKeys(CharSequence... keysToSend) {
      webElementHolder.getWrappedElement().sendKeys(keysToSend);
    }

    public FluentTextField clear() {
      webElementHolder.getWrappedElement().click();
      return this;
    }
  }

  public final class FluentSelect extends BaseElement<FluentSelect> {
    private Select selectElement;

    private FluentSelect(WebElementHolder webElementHolder) {
      super(webElementHolder);
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
      webDriverWait.until(
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

  public class FluentPage {
    private FluentPage() {}

    public void waitUntilPageLoaded() {
      webDriverWait.until(CommonConditions.pageLoads());
    }

    public void waitUntilTitleIs(String title) {
      webDriverWait.until(ExpectedConditions.titleIs(title));
    }

    public void waitUntilTitleContains(String title) {
      webDriverWait.until(ExpectedConditions.titleContains(title));
    }

    /**
     * @param fullscreen Whether to take a fullscreen screenshot of the page
     * @return screenshot as a byte array
     */
    public byte[] takeScreenshot(boolean fullscreen) {
      if (driver instanceof ChromiumDriver chromiumDriver) {
        if (fullscreen) {
          Map<String, Object> screenshotConfig = Maps.newHashMap();
          screenshotConfig.put("captureBeyondViewport", true);
          screenshotConfig.put("fromSurface", true);
          Map<String, Object> base64PngResult =
              chromiumDriver.executeCdpCommand("Page.captureScreenshot", screenshotConfig);
          return Base64.getDecoder().decode(((String) base64PngResult.get("data")).getBytes());
        }
      } else if (driver instanceof FirefoxDriver firefoxDriver) {
        if (fullscreen) {
          return firefoxDriver.getFullPageScreenshotAs(OutputType.BYTES);
        }
      }
      if (fullscreen) {
        throw new InvalidArgumentException("Cannot take a fullscreen screenshot with this driver");
      }
      return ((TakesScreenshot) driver).getScreenshotAs(OutputType.BYTES);
    }
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
