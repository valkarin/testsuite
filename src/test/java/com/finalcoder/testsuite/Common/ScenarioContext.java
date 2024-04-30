package com.finalcoder.testsuite.Common;

import lombok.Getter;
import lombok.extern.log4j.Log4j2;

/** Use this class to share State between Steps. */
@Getter
@Log4j2
public final class ScenarioContext {
  private final GenericMap dataStorage;

  public ScenarioContext() {
    dataStorage = new GenericMap();
  }
}
