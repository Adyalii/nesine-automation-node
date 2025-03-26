package com.nesine.framework.pages;

import java.util.Map;

import org.openqa.selenium.By;
import org.openqa.selenium.Cookie;
import org.openqa.selenium.TimeoutException;
import org.openqa.selenium.WebDriver;

import com.nesine.framework.api.CredentialsApiClient;
import com.nesine.framework.api.LoginApiClient;
import com.nesine.framework.utils.CookieConfig;
import com.nesine.framework.utils.DriverManager;

import io.github.cdimascio.dotenv.Dotenv;

public class HomePage extends BasePage{

    private final CredentialsApiClient apiClient = new CredentialsApiClient();
    private static final String DOMAIN = "www.nesine.com";
    private static final Dotenv dotenv = Dotenv.load();
    private static final CookieConfig cookieConfig = new CookieConfig(dotenv);

    private final By announcementModalCloseBtn = By.cssSelector(".btn-close");
    private final By inputUsername = By.cssSelector("[data-test-id='header-username-input']");
    private final By inputPassword = By.cssSelector("[data-test-id='header-password-input']");
    private final By btnLogin = By.cssSelector("[data-test-id='header-login-btn']");
    private final By btnRegister = By.cssSelector("[data-test-id='header-register-btn']");
    private final By headerForgotBtn = By.cssSelector("[data-test-id='header-forgot-btn']");
    private final By headerUsername = By.cssSelector("[data-testid='header-username']");
    private final By headerUserId = By.cssSelector("[data-testid='header-userid']");
    private final By headerParaYatirBtn = By.cssSelector("[data-testid='header-parayatir-btn']");
    private final By headerParaCekBtn = By.cssSelector("[data-testid='header-paracek-btn']");
    private final By headerBakiye = By.cssSelector("[data-testid='header-bakiye']");
    private final By headerKuponlarimBtn = By.cssSelector("[data-testid='header-kuponlarim-btn']");
    private final By headerHesabimBtn = By.cssSelector("[data-testid='header-hesabim-btn']");



    public HomePage(WebDriver driver) {
        super(driver);
    }

    public void closeAnnouncementModalIfPresent() {
        try {
            logger.info("Trying to close announcement modal.");
            click(announcementModalCloseBtn);
            logger.info("Announcement modal closed successfully.");
        } catch (TimeoutException e) {
            logger.warn("Announcement modal was not displayed.");
        }
    }

    public void login(String username, String password) {
        logger.info("Starting login process.");
        setLoginCookies();
        performLogin(username, password);

        refreshPage();
        performLogin(username, password);
        logger.info("Login process completed.");

    }

    private void performLogin(String username, String password) {
        logger.info("Attempting login with user: {}", username);
        sendKeys(inputUsername, username);
        sendKeys(inputPassword, password);
        click(btnLogin);
        logger.debug("Login button clicked.");
    }

    private void setLoginCookies() {
        logger.info("Setting login cookies via API.");
        LoginApiClient loginApiClient = new LoginApiClient();
        Map<String, String> credentials = apiClient.getCredentials();
        Map<String, String> apiCookies = loginApiClient.doLogin(credentials.get("username"), credentials.get("password"));

        if (apiCookies == null || apiCookies.isEmpty()) {
            logger.error("Login API returned no cookies.");
            return;
        }

        Map<String, Cookie> cookies = cookieConfig.getCookies(DOMAIN);

        if (cookies == null || cookies.isEmpty()) {
            logger.error("Cookies map is null or empty after API login.");
            return;
        }

        cookies.forEach((key, cookie) -> {
            if (cookie != null) {
                DriverManager.getDriver().manage().addCookie(cookie);
                logger.debug("Added cookie: {}", key);
            } else {
                logger.warn("Cookie value is null: {}", key);
            }
        });
        logger.info("Cookies set successfully.");
    }

    private void refreshPage() {
        logger.info("Refreshing the page.");
        DriverManager.getDriver().navigate().refresh();
    }


    public boolean isLoginSuccessful() {
        logger.info("Checking if login was successful.");
        boolean isSuccess = isElementDisplayed(headerUsername) &&
                isElementDisplayed(headerUserId) &&
                isElementDisplayed(headerParaYatirBtn) &&
                isElementDisplayed(headerParaCekBtn) &&
                isElementDisplayed(headerBakiye) &&
                isElementDisplayed(headerKuponlarimBtn) &&
                isElementDisplayed(headerHesabimBtn);
        if (isSuccess) {
            logger.info("Login verification passed.");
        } else {
            logger.error("Login verification failed.");
        }
        return isSuccess;
    }

    public boolean isLogoutSuccessful() {
        logger.info("Checking if logout was successful.");
        boolean isSuccess = isElementDisplayed(inputUsername) &&
                isElementDisplayed(inputPassword) &&
                isElementDisplayed(btnLogin) &&
                isElementDisplayed(btnRegister) &&
                isElementDisplayed(headerForgotBtn);
        if (isSuccess) {
            logger.info("Logout verification passed.");
        } else {
            logger.error("Logout verification failed.");
        }
        return isSuccess;
    }
}
