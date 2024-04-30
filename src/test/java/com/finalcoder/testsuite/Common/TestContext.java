package com.finalcoder.testsuite.Common;

import lombok.Getter;
import lombok.extern.log4j.Log4j2;

/**
 * Use this class to share State between Scenarios.
 * Keep in mind, State can make your steps more tightly coupled and harder to reuse.
 */
@Getter
@Log4j2
public final class TestContext {
    private final GenericMap dataStorage;

    public TestContext() {
        dataStorage = new GenericMap();
    }

}
