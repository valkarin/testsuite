package com.finalcoder.testsuite.Common;

import com.finalcoder.testsuite.Utils.ConfigFileReader;
import io.github.bonigarcia.wdm.config.DriverManagerType;
import java.util.Objects;
import java.util.stream.Stream;
import lombok.NoArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.apache.commons.configuration2.ImmutableConfiguration;
import org.apache.commons.lang3.StringUtils;

@SuppressWarnings("unused")
@Log4j2
@NoArgsConstructor
public final class TestConfiguration {

  private static class TestProperties {
    public static final String APPLICATION_TITLE = "application.title";
  }

  private static class DriverProperties {
    public static final String ENVIRONMENT = "webdriver.environment";
    public static final String BROWSER_TYPE = "webdriver.browser.type";
    public static final String BROWSER_START_MAXIMIZED = "webdriver.browser.startMaximized";
    public static final String IS_BROWSER_HEADLESS = "webdriver.browser.isHeadless";
    public static final String INJECT_EXTENSION = "webdriver.browser.injectExtension";
    public static final String EXTENSION_ID = "webdriver.extension.id";
  }

  private final ImmutableConfiguration configuration =
      ConfigFileReader.withFile("test.properties").getConfig();

  public WebDriverProperties webDriver() {
    return new WebDriverProperties(this.configuration);
  }

  public String getAppTitle() {
    return configuration.getString(TestProperties.APPLICATION_TITLE);
  }

  public String getUserHomeDirectory() {
    return configuration.getString("user.home");
  }

  public static class WebDriverProperties {
    private final ImmutableConfiguration testConfiguration;

    private WebDriverProperties(ImmutableConfiguration configuration) {
      testConfiguration = configuration;
    }

    public EnvironmentType getEnvironment() {
      var configValue = testConfiguration.getString(DriverProperties.ENVIRONMENT);
      var environmentValue = System.getenv(DriverProperties.ENVIRONMENT);
      var systemValue = System.getProperty(DriverProperties.ENVIRONMENT);
      var value =
          Stream.of(environmentValue, systemValue, configValue)
              .filter(Objects::nonNull)
              .filter(StringUtils::isBlank)
              .findFirst()
              .orElse(EnvironmentType.LOCAL.toString());
      return EnvironmentType.valueOf(value.toUpperCase());
    }

    public DriverManagerType getBrowserType() {
      var configValue = testConfiguration.getString(DriverProperties.BROWSER_TYPE);
      var environmentValue = System.getenv(DriverProperties.BROWSER_TYPE);
      var systemValue = System.getProperty(DriverProperties.BROWSER_TYPE);
      var value =
          Stream.of(environmentValue, systemValue, configValue)
              .filter(Objects::nonNull)
              .filter(StringUtils::isBlank)
              .findFirst()
              .orElse(DriverManagerType.EDGE.toString());
      return DriverManagerType.valueOf(value.toUpperCase());
    }

    public boolean getStartMaximized() {
      var configValue = testConfiguration.getString(DriverProperties.BROWSER_START_MAXIMIZED);
      var environmentValue = System.getenv(DriverProperties.BROWSER_START_MAXIMIZED);
      var systemValue = System.getProperty(DriverProperties.BROWSER_START_MAXIMIZED);
      var value =
          Stream.of(environmentValue, systemValue, configValue)
              .filter(Objects::nonNull)
              .filter(StringUtils::isBlank)
              .findFirst()
              .orElse(Boolean.TRUE.toString());
      return Boolean.parseBoolean(value.toUpperCase());
    }

    public boolean getIsBrowserHeadless() {
      var configValue = testConfiguration.getString(DriverProperties.IS_BROWSER_HEADLESS);
      var environmentValue = System.getenv(DriverProperties.IS_BROWSER_HEADLESS);
      var systemValue = System.getProperty(DriverProperties.IS_BROWSER_HEADLESS);
      var value =
          Stream.of(environmentValue, systemValue, configValue)
              .filter(Objects::nonNull)
              .filter(StringUtils::isBlank)
              .findFirst()
              .orElse(Boolean.FALSE.toString());
      return Boolean.parseBoolean(value.toUpperCase());
    }

    public boolean getInjectExtension() {
      var configValue = testConfiguration.getString(DriverProperties.INJECT_EXTENSION);
      var environmentValue = System.getenv(DriverProperties.INJECT_EXTENSION);
      var systemValue = System.getProperty(DriverProperties.INJECT_EXTENSION);
      var value =
          Stream.of(environmentValue, systemValue, configValue)
              .filter(Objects::nonNull)
              .filter(StringUtils::isBlank)
              .findFirst()
              .orElse(Boolean.TRUE.toString());
      return Boolean.parseBoolean(value.toUpperCase());
    }

    public String getExtensionId() {
      return testConfiguration.getString(DriverProperties.EXTENSION_ID);
    }
  }
}
