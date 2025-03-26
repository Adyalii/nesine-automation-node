package com.nesine.tests.stepdefinitions;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

import org.testng.Assert;

import com.nesine.framework.api.PopularCouponsApiClient;
import com.nesine.framework.api.models.Coupon;
import com.nesine.framework.pages.HomePage;
import com.nesine.framework.pages.PopularCouponsPage;
import com.nesine.framework.utils.DriverManager;

import io.cucumber.java.en.Then;
import io.cucumber.java.en.When;

public class PopularCouponSteps {

    PopularCouponsPage popularCouponsPage = new PopularCouponsPage(DriverManager.getDriver());
    PopularCouponsApiClient popularCouponsApiClient = new PopularCouponsApiClient();
    HomePage homePage = new HomePage(DriverManager.getDriver());

    @When("^User clicks on \"([^\"]*)\" button$")
    public void userClicksOnButton(String buttonName) {
        popularCouponsPage.clickButton(buttonName);
    }

    @Then("^User should be navigated to \"([^\"]*)\"$")
    public void userValidatesURL(String expectedURL) {
        Assert.assertTrue(popularCouponsPage.isOnPage(expectedURL));
    }

    @Then("^User validates the coupons details from the API$")
    public void userValidatesCouponDetails() {
        List<Coupon> couponsFromApi = popularCouponsApiClient.getPopularCoupons();

        int i = 0;
        for (Coupon coupon : couponsFromApi) {
            String hash = coupon.getCouponHash();
            int matchCountUi = popularCouponsPage.getMatchCountFromUI(hash);
            int eventCountApi = coupon.getEventCount();

            Assert.assertEquals(matchCountUi, eventCountApi,
                    "Match count mismatch for couponHash = " + hash);

            double totalOddUi = popularCouponsPage.getTotalOddFromUI(hash, i);
            double calculatedTotalOdd = coupon.calculateTotalOdd();

            Assert.assertEquals(totalOddUi, calculatedTotalOdd, 0.01,
                    "Total odd mismatch for " + hash);

            i++;
        }
    }

    @Then("^User verifies coupons are ordered by play count descending$")
    public void userVerifiesOrderingByPlayCount() {
        List<Integer> playCounts = popularCouponsPage.getCouponPlayCounts();

        List<Integer> sortedCounts = new ArrayList<>(playCounts);
        sortedCounts.sort(Comparator.reverseOrder());

        Assert.assertEquals(playCounts, sortedCounts,
                "Play counts not in descending order!");
    }

    @Then("^User should be able to logged out successfully$")
    public void userShouldBeLoggedOutSuccessfully() {
        Assert.assertTrue(homePage.isLogoutSuccessful(), "Logout failed!" );
    }
}


