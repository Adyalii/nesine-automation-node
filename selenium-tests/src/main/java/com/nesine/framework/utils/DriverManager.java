package com.nesine.framework.utils;

import java.net.MalformedURLException;
import java.net.URL;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.openqa.selenium.MutableCapabilities;
import org.openqa.selenium.PageLoadStrategy;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;
import org.openqa.selenium.edge.EdgeDriver;
import org.openqa.selenium.edge.EdgeOptions;
import org.openqa.selenium.firefox.FirefoxDriver;
import org.openqa.selenium.firefox.FirefoxOptions;
import org.openqa.selenium.remote.RemoteWebDriver;

import io.github.bonigarcia.wdm.WebDriverManager;

public class DriverManager {

    private static final ThreadLocal<WebDriver> webDriver = new ThreadLocal<>();
    private static final Logger logger = LogManager.getLogger(DriverManager.class);

    public static WebDriver getDriver() {
        if (webDriver.get() == null) {
            webDriver.set(initializeDriver());
        }
        return webDriver.get();
    }

    private static WebDriver initializeDriver() {
        logger.info("Initializing WebDriver for browser: {}", ConfigReader.getBrowser());
        WebDriver driver = null;
        String browser = ConfigReader.getBrowser().toLowerCase();
        ChromeOptions chromeOptions = new ChromeOptions();
        FirefoxOptions firefoxOptions = new FirefoxOptions();
        EdgeOptions edgeOptions = new EdgeOptions();

        switch (browser) {
            case "chrome":
                WebDriverManager.chromedriver().setup();
                chromeOptions.addArguments("--disable-notifications");
                chromeOptions.addArguments("--disable-extensions");
                chromeOptions.setExperimentalOption("excludeSwitches", new String[]{"enable-automation"});
                chromeOptions.addArguments("--disable-blink-features=AutomationControlled");
                driver = new ChromeDriver(chromeOptions);
                break;

            case "firefox":
                WebDriverManager.firefoxdriver().setup();
                firefoxOptions.addArguments("--disable-notifications");
                firefoxOptions.setPageLoadStrategy(PageLoadStrategy.NORMAL);
                firefoxOptions.addArguments("--disable-blink-features=AutomationControlled");
                driver = new FirefoxDriver(firefoxOptions);
                break;

            case "edge":
                WebDriverManager.edgedriver().setup();
                edgeOptions.setCapability("useAutomationExtension", false);
                edgeOptions.addArguments("--disable-notifications");
                driver = new EdgeDriver(edgeOptions);
                break;

            case "grid-chrome":
                chromeOptions.addArguments("--headless");
                chromeOptions.addArguments("--no-sandbox");
                chromeOptions.addArguments("--disable-dev-shm-usage");
                
                MutableCapabilities capabilities = new MutableCapabilities();
                capabilities.setCapability(ChromeOptions.CAPABILITY, chromeOptions);
                capabilities.setCapability("browserName", "chrome");

                try {
                    String gridUrl = System.getenv("GRID_URL");
                    driver = new RemoteWebDriver(new URL(gridUrl), capabilities);
                } catch (MalformedURLException e) {
                    throw new RuntimeException("Grid URL is invalid: " + e.getMessage());
                }
                break;

            case "grid-firefox":
                firefoxOptions.addArguments("start-maximized");
                firefoxOptions.addArguments("--disable-blink-features=AutomationControlled");
                try {
                    driver = new RemoteWebDriver(new URL("http://localhost:4444/wd/hub"), firefoxOptions);
                } catch (MalformedURLException e) {
                    throw new RuntimeException("Grid URL is invalid: " + e.getMessage());
                }
                break;

            default:
                throw new IllegalArgumentException("Unsupported browser: " + browser);
        }

        driver.manage().window().maximize();
        driver.get(ConfigReader.getBaseUrl());
        return driver;
    }

    public static void quitDriver() {
        if (webDriver.get() != null) {
            logger.info("Quitting WebDriver.");
            webDriver.get().quit();
            webDriver.remove();
            logger.info("WebDriver quit successfully.");
        }
    }
}