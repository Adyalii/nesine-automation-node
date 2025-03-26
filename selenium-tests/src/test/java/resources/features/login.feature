Feature: Login to Nesine.com

  Background:
    Given User navigates to "https://www.nesine.com"

  @smoke
  Scenario: Successful Login with credentials from API
    When User logs in with credentials from the API
    Then User should be logged in successfully