package com.finalcoder.testsuite.StepDefinitions;

import com.finalcoder.testsuite.Common.CommonMethods;
import com.finalcoder.testsuite.Common.DriverManager;
import com.finalcoder.testsuite.Common.TestConfiguration;
import io.cucumber.java.After;
import io.cucumber.java.Before;
import io.cucumber.java.Scenario;
import io.qameta.allure.Allure;
import lombok.extern.log4j.Log4j2;

@Log4j2
public final class Hooks extends BaseStepDefinition {
  private final DriverManager driverManager;
  private final TestConfiguration configuration;

  public Hooks(DriverManager webDriverManager, TestConfiguration configuration) {
    this.driverManager = webDriverManager;
    this.configuration = configuration;
  }

  @Before("@UI")
  public void beforeBrowserScenario() {
    log.info(
        "Starting {} browser in {} environment for UI test.",
        configuration.webDriver().getBrowserType(),
        configuration.webDriver().getEnvironment());
    driverManager.start();
  }

  @After("@UI")
  public void afterBrowserScenario(Scenario scenario) {
    if (scenario.isFailed()) {
      Allure.addByteAttachmentAsync(
          "Failure Screenshot",
          "image/png",
          () -> CommonMethods.withDefaultDriver().withPage().takeScreenshot(true));
      Allure.addAttachment("Failed at", driverManager.getDriver().getCurrentUrl());
    }
    driverManager.stop();
  }

  @Before(order = 0)
  public void beforeScenario(Scenario scenario) {
    log.info("Running Scenario: '{}'", scenario.getName());
  }

  @After(order = 0)
  public void afterScenario(Scenario scenario) {
    if (scenario.isFailed()) log.error("Scenario: '{}' is failed.", scenario.getName());
    else log.info("Scenario: '{}' is finished.", scenario.getName());
  }
}
