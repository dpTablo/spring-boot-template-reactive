package com.dptablo.template.springboot.test.support;

import com.dptablo.template.springboot.test.support.settings.TestContainersSeleniumStandalonChromeSettings;
import org.junit.jupiter.api.extension.*;
import org.openqa.selenium.chrome.ChromeOptions;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.test.context.TestPropertySource;
import org.testcontainers.containers.BrowserWebDriverContainer;

@EnableConfigurationProperties
@TestPropertySource(locations = "classpath:application-tc.yml")
public class SeleniumTestSupportExtension implements
        BeforeAllCallback, AfterAllCallback, BeforeEachCallback, AfterEachCallback {

    private final BrowserWebDriverContainer<?> browserWebDriverContainer;

    public SeleniumTestSupportExtension() {
        browserWebDriverContainer = new BrowserWebDriverContainer<>(
                    TestContainersSeleniumStandalonChromeSettings.SELENIUM_STANDALON_CHROME_IMAGE_TAG)
                .withCapabilities(new ChromeOptions());
    }

    @Override
    public void beforeAll(ExtensionContext context) {
        browserWebDriverContainer.start();
        System.setProperty("dptablo.selenium.url", browserWebDriverContainer.getSeleniumAddress().toString());
    }

    @Override
    public void afterAll(ExtensionContext context) {
        if (browserWebDriverContainer != null && browserWebDriverContainer.isRunning()) {
            browserWebDriverContainer.stop();
        }
    }

    @Override
    public void afterEach(ExtensionContext context) {
    }

    @Override
    public void beforeEach(ExtensionContext context) {
    }
}
