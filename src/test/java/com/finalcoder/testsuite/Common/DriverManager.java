package com.finalcoder.testsuite.Common;

import io.github.bonigarcia.wdm.WebDriverManager;
import io.github.bonigarcia.wdm.config.DriverManagerType;
import lombok.SneakyThrows;
import lombok.extern.log4j.Log4j2;
import org.apache.commons.lang3.NotImplementedException;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeOptions;
import org.openqa.selenium.edge.EdgeOptions;

import java.io.File;
import java.net.URISyntaxException;
import java.util.List;

@Log4j2
public final class DriverManager {
    private final TestConfiguration configuration;
    private WebDriver driver;

    public static DriverManagerType SelectedManagerType;
    public static WebDriverManager SelectedWebDriverManager;

    public DriverManager(TestConfiguration configuration) {
        this.configuration = configuration;
    }

    public void start() {
        getDriver();
    }

    public void stop() {
        if (driver != null) {
            SelectedWebDriverManager.quit();
        }
    }

    public WebDriver getDriver() {
        if (driver != null) return driver;

        driver = switch (this.configuration.webDriver().getEnvironment()) {
            case LOCAL -> getLocalDriver();
            case REMOTE -> getRemoteDriver();
            default -> throw new NotImplementedException();
        };
        return driver;
    }

    private WebDriver getRemoteDriver() {
        throw new NotImplementedException();
    }

    @SneakyThrows
    private WebDriver getLocalDriver() {
        SelectedManagerType = this.configuration.webDriver().getBrowserType();
        return switch (SelectedManagerType) {
            case CHROME -> {
                var chromeOptions = new ChromeOptions();
                if (configuration.webDriver().getStartMaximized()) {
                    chromeOptions.addArguments("--start-maximized");
                }
                if (configuration.webDriver().getIsBrowserHeadless()) {
                    chromeOptions.addArguments(List.of(
                            "--headless=chrome",
                            "--disable-gpu",
                            "--window-size=1920,1080"));
                }
                if (configuration.webDriver().getInjectExtension()) {
                    var url = ClassLoader.getSystemResource("extensions/SeleniumAutomation.crx");
                    if (url != null) {
                        try {
                            chromeOptions.addExtensions(new File(url.toURI()));
                        } catch (URISyntaxException e) {
                            log.error("Error while adding extension", e);
                        }
                    }
                }
                SelectedWebDriverManager = WebDriverManager.chromedriver();
                yield SelectedWebDriverManager.capabilities(chromeOptions).create();
            }
            case EDGE -> {
                var edgeOptions = new EdgeOptions();
                if (configuration.webDriver().getStartMaximized()) {
                    edgeOptions.addArguments("--start-maximized");
                }
                if (configuration.webDriver().getIsBrowserHeadless()) {
                    edgeOptions.addArguments(List.of(
                            "--headless=chrome",
                            "--disable-gpu",
                            "--window-size=1920,1080"));
                }
                if (configuration.webDriver().getInjectExtension()) {
                    var url = ClassLoader.getSystemResource("extensions/SeleniumAutomation.crx");
                    if (url != null) {
                        try {
                            edgeOptions.addExtensions(new File(url.toURI()));
                        } catch (URISyntaxException e) {
                            log.error("Error while adding extension", e);
                        }
                    }
                }
                SelectedWebDriverManager = WebDriverManager.edgedriver();
                yield SelectedWebDriverManager.capabilities(edgeOptions).create();
            }
            default -> throw new NotImplementedException();
        };
    }
}
