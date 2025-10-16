Feature: The application should be running

  @test1 @smoke
  Scenario: simple search
    Given I am on the home page
    When I search for "wooden spoon"
    Then I should see the results

  @test2 @regression
  Scenario: another search
    Given I am on the home page
    When I search for "useless box"
    Then I should see more results
