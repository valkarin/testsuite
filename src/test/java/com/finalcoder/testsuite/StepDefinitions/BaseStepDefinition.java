package com.finalcoder.testsuite.StepDefinitions;

import com.finalcoder.testsuite.Common.CommonMethods;
import io.qameta.allure.Allure;

public abstract class BaseStepDefinition {

  protected void takeScreenshot(String name, boolean fullscreen) {
    Allure.addByteAttachmentAsync(
        name,
        "image/png",
        ".png",
        () -> CommonMethods.withDefaultDriver().withPage().takeScreenshot(fullscreen));
  }

  protected void takeScreenshot(boolean fullscreen) {
    Allure.addByteAttachmentAsync(
        "Screenshot",
        "image/png",
        ".png",
        () -> CommonMethods.withDefaultDriver().withPage().takeScreenshot(fullscreen));
  }
}
