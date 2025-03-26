package com.nesine.tests.runners;

import com.nesine.framework.pages.PopularCouponsPage;
import io.cucumber.testng.AbstractTestNGCucumberTests;
import io.cucumber.testng.CucumberOptions;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.testng.annotations.DataProvider;


@CucumberOptions(
        features = "src/test/java/resources/features",
        glue = {
                "com.nesine.tests.stepdefinitions",
                "com.nesine.tests.hooks"
        },
        plugin = {
                "pretty",
                "html:target/cucumber-html-report",
                "json:target/cucumber-report.json",
                "io.qameta.allure.cucumber7jvm.AllureCucumber7Jvm"
        },
        monochrome = true,
        dryRun = false,
        tags = "@smoke"
)
public class TestRunner extends AbstractTestNGCucumberTests {

    private static final Logger logger = LogManager.getLogger(TestRunner.class);

    @Override
    @DataProvider(parallel = true)
    public Object[][] scenarios() {
        logger.info("Starting parallel test execution.");
        return super.scenarios();
    }
}