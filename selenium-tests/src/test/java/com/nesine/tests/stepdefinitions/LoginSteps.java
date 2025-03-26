package com.nesine.tests.stepdefinitions;

import com.nesine.framework.api.CredentialsApiClient;
import com.nesine.framework.utils.DriverManager;
import com.nesine.framework.pages.HomePage;
import io.cucumber.java.en.Given;
import io.cucumber.java.en.Then;
import io.cucumber.java.en.When;
import org.testng.Assert;

import java.util.Map;

public class LoginSteps {

    private HomePage homePage = new HomePage(DriverManager.getDriver());
    private CredentialsApiClient apiClient = new CredentialsApiClient();

    @Given("^User navigates to \"([^\"]*)\"$")
    public void userNavigatesTo(String url) {
        DriverManager.getDriver().get(url);
    }

    @When("^User logs in with credentials from the API$")
    public void userLogsInWithCredentialsFromTheApi() {
        Map<String, String> credentials = apiClient.getCredentials();
        homePage.login(credentials.get("username"), credentials.get("password"));
    }

    @Then("^User should be logged in successfully$")
    public void userShouldBeLoggedInSuccessfully() {
        Assert.assertTrue(homePage.isLoginSuccessful(),"Login failed!");
    }
}
