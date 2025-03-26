package com.nesine.framework.pages;

import java.util.*;
import java.util.regex.*;

import org.openqa.selenium.*;

import com.nesine.framework.api.CredentialsApiClient;
import com.nesine.framework.utils.DriverManager;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public class PopularCouponsPage extends BasePage {

    private static final Logger logger = LogManager.getLogger(PopularCouponsPage.class);
    private HomePage homePage = new HomePage(DriverManager.getDriver());
    private CredentialsApiClient apiClient = new CredentialsApiClient();

    private final By populerKuponlarBtn = By.cssSelector(".popular-coupons");
    private final By hemenOynaTab       = By.cssSelector("[data-testid='TabMenu']>div>div>button:nth-child(1)");
    private final By tumuTab            = By.cssSelector("#popularCouponsViewContainer>div>div>div>button:nth-child(1)");
    private final By hesabimBtn         = By.cssSelector("[data-testid='header-hesabim-btn']");
    private final By cikisBtn           = By.cssSelector("[data-testid='header-hesabim-cikis']");
    private final By couponContainer    = By.cssSelector("[data-test-id='couponEvents']");
    private final By eventRow           = By.cssSelector(".eventRow");
    private final By playCountElement   = By.cssSelector(".play-count");
    private String RELATIVE_TOTAL_ODD_XPATH = "//div[./span[text()='Oran']]/b";

    public PopularCouponsPage(WebDriver driver) {
        super(driver);
    }
    public void clickButton(String buttonName) {
        logger.info("Attempting to click button: {}", buttonName);
        switch (buttonName) {
            case "Popüler Kuponlar":
                click(populerKuponlarBtn);
                logger.debug("Clicked 'Popüler Kuponlar' button.");
                break;
            case "Hemen Oyna":
                click(hemenOynaTab);
                logger.debug("Clicked 'Hemen Oyna' tab.");
                break;
            case "Tümü":
                click(tumuTab);
                logger.debug("Clicked 'Tümü' tab.");
                break;
            case "Hesabım":
                Map<String, String> credentials = apiClient.getCredentials();
                homePage.login(credentials.get("username"), credentials.get("password"));
                click(hesabimBtn);
                logger.debug("Clicked 'Hesabım' button.");
                break;
            case "Çıkış":
                click(cikisBtn);
                logger.debug("Clicked 'Çıkış' button.");
                break;
            default:
                logger.warn("Button '{}' not recognized.", buttonName);
        }
    }
    public boolean isOnPage(String expectedURL) {
        logger.info("Verifying current URL: {}", expectedURL);
        boolean result = waitForURLToBe(expectedURL);
        logger.info("URL verification result: {}", result);
        return result;
    }
    public Map<String, WebElement> getCouponContainersByHash() {
        logger.info("Getting coupon containers by hash.");
        List<WebElement> containers = driver.findElements(couponContainer);
        Map<String, WebElement> hashContainerMap = new HashMap<>();

        Pattern pattern = Pattern.compile("populerCoupons_([a-z0-9]+)_\\d+");

        for (WebElement container : containers) {
            String containerId = container.getAttribute("id");
            if (containerId == null) continue;

            Matcher m = pattern.matcher(containerId);
            if (m.find()) {
                String hash = m.group(1);
                hashContainerMap.put(hash, container);
                logger.debug("Found container with hash: {}", hash);
            }
        }
        return hashContainerMap;
    }
    public int getMatchCountFromUI(String couponHash) {
        logger.info("Getting match count from UI for coupon hash: {}", couponHash);
        Map<String, WebElement> map = getCouponContainersByHash();
        if (!map.containsKey(couponHash)) {
            logger.warn("Coupon hash '{}' not found.", couponHash);
            return 0;
        }
        int count = map.get(couponHash).findElements(eventRow).size();
        logger.info("Match count for coupon '{}' is: {}", couponHash, count);
        return count;
    }
    public double getTotalOddFromUI(String couponHash, int elementIndex) {
        logger.info("Getting total odd from UI for coupon hash: {}, index: {}", couponHash, elementIndex);
        WebElement container = getContainerByHash(couponHash);
        if (container == null) {
            logger.warn("Coupon container for hash '{}' not found.", couponHash);
            return 0.0;
        }

        List<WebElement> oddElements = container.findElements(By.xpath(RELATIVE_TOTAL_ODD_XPATH));
        if (elementIndex < 0 || elementIndex >= oddElements.size()) {
            logger.warn("Invalid element index: {}", elementIndex);
            return 0.0;
        }

        String rawText = oddElements.get(elementIndex).getText().replace(".", "").replace(",", ".").replaceAll("[^0-9.]", "");

        try {
            double totalOdd = Double.parseDouble(rawText);
            logger.info("Extracted total odd: {}", totalOdd);
            return totalOdd;
        } catch (NumberFormatException e) {
            logger.error("Failed to parse total odd from text: {}", rawText, e);
            return 0.0;
        }

    }

    private WebElement getContainerByHash(String couponHash) {
        logger.debug("Fetching container by hash: {}", couponHash);
        return getCouponContainersByHash().getOrDefault(couponHash, null);
    }

    public List<Integer> getCouponPlayCounts() {
        logger.info("Getting coupon play counts from UI.");
        List<Integer> result = new ArrayList<>();
        for (WebElement c : driver.findElements(couponContainer)) {
            try {
                int count = Integer.parseInt(c.findElement(playCountElement).getText());
                result.add(count);
                logger.debug("Play count: {}", count);
            } catch (Exception e) {
                logger.warn("Failed to parse play count", e);
            }
        }
        return result;
    }
}