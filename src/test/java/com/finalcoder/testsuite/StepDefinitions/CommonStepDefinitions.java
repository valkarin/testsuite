package com.finalcoder.testsuite.StepDefinitions;

import com.finalcoder.testsuite.Common.CommonMethods;
import io.cucumber.java.en.And;
import io.cucumber.java.en.Given;

public final class CommonStepDefinitions extends BaseStepDefinition {

  @Given("User navigates to {string}")
  public void userNavigatesTo(String website) {
    CommonMethods.withDefaultDriver().navigate(website);
  }

  @And("Take a full-page screenshot")
  public void userTakesFullPageScreenshot() {
    this.takeScreenshot(true);
  }

  @And("Take a screenshot")
  public void userTakesScreenshot() {
    this.takeScreenshot(false);
  }
}
