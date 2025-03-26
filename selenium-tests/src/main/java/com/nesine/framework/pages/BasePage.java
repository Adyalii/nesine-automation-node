package com.nesine.framework.pages;

import java.time.Duration;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.support.ui.WebDriverWait;
import com.nesine.framework.utils.CommonMethods;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;


public abstract class BasePage {

    protected WebDriver driver;
    protected WebDriverWait wait;
    protected CommonMethods commonMethods;

    protected final Logger logger = LogManager.getLogger(getClass());

    public BasePage(WebDriver driver) {
        this.driver = driver;
        this.wait = new WebDriverWait(driver, Duration.ofSeconds(15));
        this.commonMethods = new CommonMethods(driver);
    }

    protected void click(By locator) {
        logger.info("Clicking element: {}", locator);
        commonMethods.click(locator);
    }

    protected void sendKeys(By locator, String text) {
        logger.info("Sending keys '{}' to element: {}", text, locator);
        commonMethods.sendKeys(locator, text);
    }

    protected String getText(By locator) {
        logger.info("Getting text from element: {}", locator);
        return commonMethods.getText(locator);
    }

    protected boolean isElementDisplayed(By locator) {
        logger.debug("Checking visibility of element: {}", locator);
        try {
            return commonMethods.waitForVisibility(locator).isDisplayed();
        } catch (Exception e) {
            System.out.println(locator + " is not displayed");
            return false;
        }
    }

    protected void waitForInvisibility(By locator) {
        commonMethods.waitForInvisibility(locator);
    }

    protected void navigateTo(String url) {
        logger.info("Navigating to URL: {}", url);
        driver.get(url);
    }

    protected boolean waitForURLToBe(String url){
        logger.info("Waiting for URL to be: {}", url);
        return commonMethods.waitForURLToBe(url);
    }
}