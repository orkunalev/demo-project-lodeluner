package cydeo;

import io.cucumber.java.After;
import io.cucumber.java.Scenario;
import io.cucumber.java.en.Given;
import io.cucumber.java.en.Then;
import io.cucumber.java.en.When;
import org.junit.Assert;
import org.openqa.selenium.*;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;
import java.time.Duration;
import java.util.concurrent.TimeUnit;

public class StepDefs {

    @Given("^I am on the home page$")
    public void i_am_on_the_home_page() {
        Driver.getDriver().manage().timeouts().implicitlyWait(5, TimeUnit.SECONDS);
        Driver.getDriver().manage().window().maximize();
        Driver.getDriver().get("https://www.etsy.com");
    }

    @When("I search for {string}")
    public void i_search_for(String item) {
        try {
            WebDriverWait wait = new WebDriverWait(Driver.getDriver(), Duration.ofSeconds(5));
            WebElement privacyOverlay = wait.until(ExpectedConditions.presenceOfElementLocated(
                    By.id("gdpr-single-choice-overlay")
            ));

            // GDPR popup kapatma butonu
            WebElement acceptButton = Driver.getDriver().findElement(
                    By.xpath("//button[contains(.,'Accept') or contains(.,'Allow')]")
            );
            acceptButton.click();

            // Overlay’in kaybolmasını bekle
            wait.until(ExpectedConditions.invisibilityOf(privacyOverlay));
            System.out.println("✅ Privacy popup kapatıldı.");

        } catch (Exception e) {
            System.out.println("⚠️ Privacy popup görünmedi, devam ediliyor.");
        }

        WebDriverWait wait = new WebDriverWait(Driver.getDriver(), Duration.ofSeconds(10));
        WebElement searchBox = wait.until(ExpectedConditions.elementToBeClickable(
                By.xpath("(//input[@data-id='search-query' and not(@aria-hidden='true')])[1]")
        ));
        searchBox.click();
        searchBox.sendKeys(item, Keys.ENTER);  // 🔥 feature’dan gelen kelimeyle arama yapar
    }

    @Then("^I should see the results$")
    public void i_should_see_the_results() throws InterruptedException {
        Thread.sleep(2000);
        Assert.assertTrue(Driver.getDriver().getCurrentUrl().contains("search"));
    }

    @Then("^I should see more results$")
    public void i_should_see_more_results() throws InterruptedException {
        Thread.sleep(2000);
        Assert.assertTrue(Driver.getDriver().getCurrentUrl().contains("search"));
    }

    @After
    public void tearDown(Scenario scenario) {
        if (scenario.isFailed()) {
            final byte[] screenshot = ((TakesScreenshot) Driver.getDriver()).getScreenshotAs(OutputType.BYTES);
            scenario.attach(screenshot, "image/png", scenario.getName());
        }
        Driver.closeDriver();
    }
}
