package tests;

import utils.BaseTest; // Assuming BaseTest handles WebDriver setup/teardown
import org.apache.logging.log4j.LogManager; // For Log4j2
import org.apache.logging.log4j.Logger;     // For Log4j2
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;         // Import for WebDriver
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.testng.Assert;                   // Import for Assertions
import org.testng.annotations.Test;
import org.testng.annotations.DataProvider; // Import DataProvider

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.time.Duration;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

public class DonatePageTest extends BaseTest {

    private static final Logger logger = LogManager.getLogger(DonatePageTest.class); // Log4j2 Logger

    // Data Provider to read from CSV
    @DataProvider(name = "donationFormData")
    public Iterator<Object[]> getDonationFormData() throws IOException {
        List<Object[]> data = new ArrayList<>();
        String csvFile = "src/test/resources/donation_data.csv"; // Path to your CSV file
        BufferedReader br = null;
        String line;
        String cvsSplitBy = ",";

        try {
            br = new BufferedReader(new FileReader(csvFile));
            br.readLine(); // Skip header row
            while ((line = br.readLine()) != null) {
                String[] values = line.split(cvsSplitBy);
                // Ensure the number of values matches the number of parameters in your test method
                // Adjusted to expect 9 columns based on your provided test method signature
                if (values.length == 9) {
                    data.add(new Object[]{
                        values[0], // firstName
                        values[1], // lastName
                        values[2], // email
                        values[3], // phone
                        values[4], // location
                        values[5], // pincode
                        values[6], // aadharOrPan
                        values[7], // amount
                        values[8]  // expectedOutcome
                    });
                } else {
                    logger.warn("Skipping malformed CSV row: {}. Expected 9 columns but found {}", line, values.length);
                }
            }
        } finally {
            if (br != null) {
                try {
                    br.close();
                } catch (IOException e) {
                    logger.error("Error closing BufferedReader: {}", e.getMessage());
                }
            }
        }
        return data.iterator();
    }

    @Test(dataProvider = "donationFormData")
    public void testFillDonationForm(String firstName, String lastName, String email, String phone,
                                      String location, String pincode, String aadharOrPan, String amount,
                                      String expectedOutcome) { // Removed InterruptedException, use waits

        WebDriver driver = getDriver(); // Get WebDriver instance from BaseTest
        logger.info("Starting donation form test for: {} {}", firstName, lastName);
        
        driver.get("https://vhpkarnataka.org/donate");
        logger.debug("Navigated to donation page: {}", driver.getCurrentUrl());

        WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(15)); // Increased wait time

        // Wait for and fill the first name field
        WebElement firstNameField = wait.until(ExpectedConditions.presenceOfElementLocated(By.name("firstName")));
        firstNameField.sendKeys(firstName);
        logger.debug("Filled First Name: {}", firstName);

        driver.findElement(By.name("lastName")).sendKeys(lastName);
        logger.debug("Filled Last Name: {}", lastName);
        
        driver.findElement(By.name("email")).sendKeys(email);
        logger.debug("Filled Email: {}", email);
        
        driver.findElement(By.name("phone")).sendKeys(phone);
        logger.debug("Filled Phone: {}", phone);
        
        driver.findElement(By.name("location")).sendKeys(location);
        logger.debug("Filled Location: {}", location);
        
        driver.findElement(By.name("pincode")).sendKeys(pincode);
        logger.debug("Filled Pincode: {}", pincode);
        
        driver.findElement(By.name("aadharOrPan")).sendKeys(aadharOrPan);
        logger.debug("Filled Aadhar/PAN: {}", aadharOrPan);
        
        driver.findElement(By.name("amount")).sendKeys(amount);
        logger.debug("Filled Amount: {}", amount);

        // Click the checkbox
        WebElement checkbox = wait.until(ExpectedConditions.elementToBeClickable(By.cssSelector("button[role='checkbox']")));
        checkbox.click();
        logger.debug("Clicked checkbox.");
        
        // No Thread.sleep() - rely on implicit or explicit waits for subsequent actions
        
        WebElement donateButton = driver.findElement(By.cssSelector("button[type='submit']"));
        
        // Assertions based on expected outcome
        if ("success".equalsIgnoreCase(expectedOutcome)) {
            logger.info("Test case for valid data (Expected: Success) for: {} {}", firstName, lastName);
            // Example assertion for success scenario:
            // Ensure the button is enabled and then click it
            Assert.assertTrue(donateButton.isEnabled(), "Donate button should be enabled for valid data.");
            donateButton.click();
            logger.info("Clicked Donate button for success scenario.");

            // Add more assertions for success, e.g., checking for a success message, redirection, etc.
            // For example, if it redirects to a confirmation page:
            wait.until(ExpectedConditions.urlContains("success")); // Wait for URL to change
            Assert.assertTrue(driver.getCurrentUrl().contains("success"), "Did not navigate to success page.");
            logger.info("Successfully navigated to success page.");

        } else if ("invalid_email".equalsIgnoreCase(expectedOutcome)) {
            logger.warn("Test case for invalid email (Expected: Failure) for: {} {}", firstName, lastName);
            // Assert that an email error message is displayed
            // You'll need to find the specific locator for the email error message
            WebElement emailErrorMessage = wait.until(ExpectedConditions.visibilityOfElementLocated(By.xpath("//*[@id='email-error' or contains(text(),'Please enter a valid email')]"))); // Example XPath
            Assert.assertTrue(emailErrorMessage.isDisplayed(), "Email error message not displayed for invalid email.");
            logger.info("Verified email error message is displayed.");
            Assert.assertFalse(donateButton.isEnabled(), "Donate button should be disabled for invalid email.");


        } else if ("invalid_phone".equalsIgnoreCase(expectedOutcome)) {
            logger.warn("Test case for invalid phone (Expected: Failure) for: {} {}", firstName, lastName);
            // Assert that a phone error message is displayed
            WebElement phoneErrorMessage = wait.until(ExpectedConditions.visibilityOfElementLocated(By.xpath("//*[@id='phone-error' or contains(text(),'Please enter a valid phone number')]"))); // Example XPath
            Assert.assertTrue(phoneErrorMessage.isDisplayed(), "Phone error message not displayed for invalid phone.");
            logger.info("Verified phone error message is displayed.");
            Assert.assertFalse(donateButton.isEnabled(), "Donate button should be disabled for invalid phone.");

        } else if ("empty_field".equalsIgnoreCase(expectedOutcome)) {
             logger.warn("Test case for empty mandatory field (Expected: Failure) for: {} {}", firstName, lastName);
             // Assert that a required field error message is displayed or button is disabled
             Assert.assertFalse(donateButton.isEnabled(), "Donate button should be disabled for empty mandatory fields.");
             // You can add more specific assertions for each empty field if needed
             logger.info("Verified donate button is disabled for empty fields.");

        } else {
            logger.error("Unhandled expectedOutcome: {}. Please define assertions for this case.", expectedOutcome);
            Assert.fail("Unhandled expectedOutcome: " + expectedOutcome); // Fail test if outcome is not handled
        }
        logger.info("Test for {} {} completed. Outcome: {}", firstName, lastName, expectedOutcome);
        logger.info("-------------------------------------");
    }
}