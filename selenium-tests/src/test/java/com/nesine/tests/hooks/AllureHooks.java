package com.nesine.tests.hooks;

import io.cucumber.java.Before;
import io.cucumber.java.BeforeStep;
import io.cucumber.java.AfterStep;
import io.cucumber.java.After;
import io.cucumber.java.Scenario;

import io.qameta.allure.*;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import org.openqa.selenium.OutputType;
import org.openqa.selenium.TakesScreenshot;

import static com.nesine.framework.utils.DriverManager.getDriver;

import java.io.*;
import java.nio.file.*;

public class AllureHooks {

    private static final Logger logger = LogManager.getLogger(AllureHooks.class);

    /**
     * The directory where Allure result files are stored.
     * Usually "allure-results" at project root or target folder.
     */
    private static final String ALLURE_RESULTS_DIR = "allure-results";

    /**
     * Called before each Scenario. We can add environment labels,
     * device/browser info, etc. And create an environment.properties
     * file inside allure-results (similar to your WebdriverIO example).
     */
    @Before
    public void beforeScenario(Scenario scenario) {
        String scenarioName = scenario.getName();
        String featureName = getFeatureName(scenario);

        Allure.getLifecycle().updateTestCase(testResult -> {
            testResult.setName(scenarioName);
        });
        Allure.label("execution", "automated");
        Allure.label("severity", "critical");

        String browserName = System.getProperty("browser", "chrome");
        String platform    = System.getProperty("os.name", "Unknown OS");

        Allure.label("Browser", browserName);
        Allure.label("Platform", platform);
        Allure.label("Device", "Local Machine");

        createEnvironmentFile(browserName, platform);

        logger.info("===== Starting Scenario: {} (feature: {}) =====", scenarioName, featureName);
    }

    @BeforeStep
    public void beforeStep(io.cucumber.java.Scenario scenario) {

        logger.debug(">>> Starting Step: " + getCurrentStepText(scenario));
    }
    @AfterStep
    public void afterStep(io.cucumber.java.Scenario scenario) {
        String stepText = getCurrentStepText(scenario);
        String status = scenario.getStatus().toString();

        if ("FAILED".equalsIgnoreCase(status)) {
            byte[] screenshot = ((TakesScreenshot) getDriver()).getScreenshotAs(OutputType.BYTES);
            Allure.addAttachment("Error Screenshot - " + stepText, new ByteArrayInputStream(screenshot));
            logger.error("Step FAILED: {}", stepText);

        } else if ("PASSED".equalsIgnoreCase(status)) {
            logger.debug("Step PASSED: {}", stepText);
        } else {
            logger.warn("Step status: {} for {}", status, stepText);
        }
    }
    @After
    public void afterScenario(Scenario scenario) {
        if (scenario.isFailed()) {
            logger.error("Scenario FAILED => " + scenario.getName());

            byte[] screenshot = ((TakesScreenshot) getDriver()).getScreenshotAs(OutputType.BYTES);
            Allure.addAttachment("Failure Screenshot - final", new ByteArrayInputStream(screenshot));

            String dummyBrowserLogs = "[{level: SEVERE, message: 'test error'}]";
            Allure.addAttachment("Browser Logs", "application/json", dummyBrowserLogs);
        } else {
            logger.info("Scenario PASSED => " + scenario.getName());
        }

        Allure.getLifecycle().updateTestCase(testResult -> {
            testResult.setFullName(scenario.getId());
        });

        Allure.link("TestID", scenario.getId());
    }


    private void createEnvironmentFile(String browser, String platform) {
        try {
            Path resultsDir = Paths.get(ALLURE_RESULTS_DIR);
            if (!Files.exists(resultsDir)) {
                Files.createDirectories(resultsDir);
            }

            Path envFile = resultsDir.resolve("environment.properties");
            String environmentData = String.format(
                    "Browser=%s%nPlatform=%s%nDevice=%s%nOS=%s",
                    browser,
                    platform,
                    "Local Machine",
                    System.getProperty("os.version", "Unknown")
            );
            Files.write(envFile, environmentData.getBytes());
            logger.debug("Environment properties file created at: {}", envFile);
        } catch (IOException e) {
            logger.error("Failed to create environment.properties", e);
        }
    }

    private String getFeatureName(Scenario scenario) {
        String rawUri = String.valueOf(scenario.getUri());
        int lastSlash = rawUri.lastIndexOf('/');
        String filename = rawUri.substring(lastSlash + 1).replace(".feature", "");
        return filename.isEmpty() ? "UnknownFeature" : filename;
    }

    private String getCurrentStepText(Scenario scenario) {
        return scenario.getName();
    }
}
