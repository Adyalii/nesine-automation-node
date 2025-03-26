Feature: Popular Coupons Verification

  Background:
    Given User navigates to "https://www.nesine.com"
    And User logs in with credentials from the API
    And User should be logged in successfully

  @smoke
  Scenario: Validate popular coupons page and data integrity
    When User clicks on "Popüler Kuponlar" button
    Then User should be navigated to "https://www.nesine.com/iddaa/populer-kuponlar"
    When User clicks on "Hemen Oyna" button
    And User clicks on "Tümü" button
    Then User validates the coupons details from the API
    And User verifies coupons are ordered by play count descending
    When User clicks on "Hesabım" button
    And User clicks on "Çıkış" button
    Then User should be able to logged out successfully