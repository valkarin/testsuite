package com.finalcoder.testsuite;

import com.finalcoder.testsuite.Common.TestConfiguration;
import com.finalcoder.testsuite.Utils.AllureUtils;
import com.finalcoder.testsuite.Utils.InstanceFactory;
import io.cucumber.testng.AbstractTestNGCucumberTests;
import io.cucumber.testng.CucumberOptions;
import lombok.extern.log4j.Log4j2;
import org.testng.annotations.AfterSuite;
import org.testng.annotations.BeforeSuite;

@Log4j2
@CucumberOptions(
    features = {"src/test/resources/features"},
    glue = "com.finalcoder.testsuite.StepDefinitions",
    plugin = {
      "org.jetbrains.plugins.cucumber.java.run.CucumberJvm5SMFormatter",
      "io.qameta.allure.cucumber7jvm.AllureCucumber7Jvm",
      "com.finalcoder.testsuite.Utils.StepLogger"
    },
    objectFactory = InstanceFactory.class)
public final class CucumberRunner extends AbstractTestNGCucumberTests {

  @BeforeSuite(alwaysRun = true)
  private void beforeAll() {
    var configuration = new TestConfiguration();
    log.info(configuration.getAppTitle());
  }

  @AfterSuite(alwaysRun = true)
  public void generateAllureReport() {
    AllureUtils.GenerateSingleFileReport();
  }
}
