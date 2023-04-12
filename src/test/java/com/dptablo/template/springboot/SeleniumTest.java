package com.dptablo.template.springboot;

import com.dptablo.template.springboot.test.support.SeleniumTestSupportExtension;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.condition.EnabledIfSystemProperty;
import org.junit.jupiter.api.extension.ExtendWith;
import org.openqa.selenium.chrome.ChromeOptions;
import org.openqa.selenium.remote.RemoteWebDriver;
import org.springframework.test.context.ActiveProfiles;
import org.testcontainers.junit.jupiter.Testcontainers;

import java.net.MalformedURLException;
import java.net.URL;

import static org.assertj.core.api.Assertions.*;

@ActiveProfiles("tc")
@Testcontainers
@ExtendWith(SeleniumTestSupportExtension.class)
public class SeleniumTest {
    /**
     * selenium/standalone-chrome를 이용하여 remote selenium를 사용한 테스트 예제 입니다.
     * 이 테스트 케이스는 OS/ARCH가 linux/amd64인 경우에만 통과합니다.
     *
     * @throws MalformedURLException
     */
    @Test
    @EnabledIfSystemProperty(named = "os.arch", matches = ".*amd64.*") // 시스템 프로퍼티인 os.arch가 amd64를 포함할 때만 실행
    void test() throws MalformedURLException {
        var chromeOptions = new ChromeOptions();
        chromeOptions.addArguments("--headless");
        chromeOptions.addArguments("--no-sandbox");
        chromeOptions.addArguments("--single-process");
        chromeOptions.addArguments("--disable-dev-shm-usage");
        chromeOptions.addArguments("--log-level=3");
        chromeOptions.addArguments("--window-size=1920x1080");
        chromeOptions.addArguments("--disable-gpu");
        chromeOptions.addArguments("--disable-infobars");
        chromeOptions.addArguments("--disable-extensions");

        var seleniumUrl = System.getProperty("dptablo.selenium.url");
        assertThat(seleniumUrl).isNotNull();

        var remoteWebDriver = new RemoteWebDriver(new URL(seleniumUrl), chromeOptions);
        assertThat(remoteWebDriver).isNotNull();
    }
}
