package com.finalcoder.core;

import org.apache.commons.io.IOUtils;

import java.io.IOException;
import java.net.URL;

import static com.finalcoder.core.Lazy.lazyEvaluated;
import static java.lang.Thread.currentThread;
import static java.nio.charset.StandardCharsets.UTF_8;
import static java.util.Objects.requireNonNull;

/**
 * Read file content from classpath
 *
 * The point is in lazy loading: the content is loaded only on the first usage and only once.
 */
// Copied from https://github.com/selenide/selenide/blob/main/src/main/java/com/codeborne/selenide/impl/FileContent.java
public class FileContent {
    private final String filePath;
    private final Lazy<String> content = lazyEvaluated(this::readContent);

    public FileContent(String filePath) {
        this.filePath = filePath;
    }

    public String content() {
        return content.get();
    }

    private String readContent() {
        try {
            URL sizzleJs = requireNonNull(currentThread().getContextClassLoader().getResource(filePath));
            return IOUtils.toString(sizzleJs, UTF_8);
        }
        catch (IOException e) {
            throw new IllegalArgumentException("Cannot load " + filePath + " from classpath", e);
        }
    }
}
