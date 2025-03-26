package com.nesine.tests.hooks;

import com.nesine.framework.pages.HomePage;
import com.nesine.framework.utils.DriverManager;
import io.cucumber.java.After;
import io.cucumber.java.Before;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.openqa.selenium.Cookie;
import org.openqa.selenium.OutputType;
import org.openqa.selenium.TakesScreenshot;
import io.cucumber.java.Scenario;

public class Hooks {

    private static final Logger logger = LogManager.getLogger(Hooks.class);

    @Before
    public void setUp(Scenario scenario) {
        logger.info("Setting up scenario: {}", scenario.getName());
        DriverManager.getDriver();

        String cookieName = "cc_cookie";
        String cookieValue = "{\"level\":[\"necessary\",\"ads\"],\"revision\":0,\"data\":null,\"rfc_cookie\":false}";

        Cookie customCookie = new Cookie.Builder(cookieName, cookieValue)
                .domain("www.nesine.com")
                .path("/")
                .build();

        DriverManager.getDriver().manage().addCookie(customCookie);

    }

    public void beforeScenario(){
    HomePage homepage = new HomePage(DriverManager.getDriver());
    homepage.closeAnnouncementModalIfPresent();
    }

    @After
    public void tearDown(Scenario scenario) {
        if (scenario.isFailed()) {
            logger.error("Scenario '{}' FAILED", scenario.getName());
            final byte[] screenshot = ((TakesScreenshot) DriverManager.getDriver()).getScreenshotAs(OutputType.BYTES);
            scenario.attach(screenshot, "image/png", scenario.getName());
        }else {
            logger.info("Scenario '{}' PASSED", scenario.getName());
        }
        DriverManager.quitDriver();
    }
}