package com.finalcoder.testsuite.Utils;

import com.finalcoder.testsuite.Common.DriverManager;
import com.finalcoder.testsuite.Common.TestConfiguration;
import io.cucumber.plugin.ConcurrentEventListener;
import io.cucumber.plugin.event.*;
import lombok.extern.log4j.Log4j2;
import org.apache.commons.lang3.time.DurationFormatUtils;
import org.openqa.selenium.JavascriptException;
import org.openqa.selenium.JavascriptExecutor;

@Log4j2
public final class StepLogger implements ConcurrentEventListener {
  private static final TestConfiguration configuration = new TestConfiguration();
  public EventHandler<TestStepStarted> stepStartedEventHandler = this::logTestStarted;
  public EventHandler<TestStepFinished> stepFinishedEventHandler = this::logTestFinished;

  @Override
  public void setEventPublisher(EventPublisher eventPublisher) {
    eventPublisher.registerHandlerFor(TestStepStarted.class, stepStartedEventHandler);
    eventPublisher.registerHandlerFor(TestStepFinished.class, stepFinishedEventHandler);
  }

  private void logTestStarted(TestStepStarted testStepStarted) {
    if (testStepStarted.getTestStep() != null
        && testStepStarted.getTestStep() instanceof PickleStepTestStep testStep) {
      var stepText = testStep.getStep().getText();
      log.debug("Running Step - {}", stepText);
      if (DriverManager.SelectedWebDriverManager == null) {
        return;
      }
      var driver = DriverManager.SelectedWebDriverManager.getWebDriver();
      if (driver != null) {
        try {
          if (driver instanceof JavascriptExecutor javascriptExecutor) {
            javascriptExecutor.executeScript(
                String.format(
                    "chrome.runtime.sendMessage('%s',{type:'step-started', message:'%s'});",
                    configuration.webDriver().getExtensionId(), stepText));
          }
        } catch (JavascriptException e) {
          // ignore
        }
      }
    }
  }

  private void logTestFinished(TestStepFinished testStepFinished) {
    if (testStepFinished.getTestStep() != null
        && testStepFinished.getTestStep() instanceof PickleStepTestStep testStep) {
      var logText = "{} took {} seconds to run. Result: {}";
      if (testStepFinished.getResult().getStatus() == Status.PASSED)
        log.trace(
            logText,
            testStep.getStep().getText(),
            DurationFormatUtils.formatDuration(
                testStepFinished.getResult().getDuration().toMillis(), "ss.SSS"),
            testStepFinished.getResult().getStatus().toString());
      else
        log.error(
            logText,
            testStep.getStep().getText(),
            DurationFormatUtils.formatDuration(
                testStepFinished.getResult().getDuration().toMillis(), "ss.SSS"),
            testStepFinished.getResult().getStatus().toString());
    }
  }
}
