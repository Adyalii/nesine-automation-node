package com.nesine.framework.utils;

import org.openqa.selenium.*;
import org.openqa.selenium.interactions.Actions;
import org.openqa.selenium.support.ui.*;
import java.time.Duration;

public class CommonMethods {

    private WebDriver driver;
    private WebDriverWait wait;

    public CommonMethods(WebDriver driver) {
        this.driver = driver;
        this.wait = new WebDriverWait(driver, Duration.ofSeconds(15));
    }

    public WebElement waitForVisibility(By locator) {
        return wait.until(ExpectedConditions.visibilityOfElementLocated(locator));
    }

    public WebElement waitForClickable(By locator) {
        return wait.until(ExpectedConditions.elementToBeClickable(locator));
    }

    public void click(By locator) {
        waitForClickable(locator).click();
    }

    public void sendKeys(By locator, String text) {
        waitForVisibility(locator).sendKeys(text);
    }

    public String getText(By locator) {
        return waitForVisibility(locator).getText();
    }

    public boolean waitForURLToBe(String url) {
        return wait.until(ExpectedConditions.urlToBe(url));
    }

    public void hover(By locator) {
        WebElement element = waitForVisibility(locator);
        new Actions(driver).moveToElement(element).perform();
    }

    public boolean isDisplayed(By locator) {
        try {
            return waitForVisibility(locator).isDisplayed();
        } catch (TimeoutException e) {
            return false;
        }
    }

    public void clear(By locator) {
        waitForVisibility(locator).clear();
    }

    public void waitForDisplayed (By locator){
        waitForVisibility(locator);
    }

    public void waitForInvisibility(By locator) {
        wait.until(ExpectedConditions.invisibilityOfElementLocated(locator));
    }
}
