package tests;

import utils.BaseTest; // Import BaseTest for WebDriver management
import org.apache.logging.log4j.LogManager; // For Log4j2
import org.apache.logging.log4j.Logger;     // For Log4j2
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;         // Import for WebDriver
import org.openqa.selenium.WebElement;
import org.openqa.selenium.JavascriptExecutor; // For scrolling
import org.openqa.selenium.support.ui.ExpectedConditions; // For explicit waits
import org.openqa.selenium.support.ui.WebDriverWait;     // For explicit waits
import org.testng.Assert;                   // Import for Assertions
import org.testng.annotations.Test;

import java.time.Duration; // For Duration in WebDriverWait

public class ThemeToggle extends BaseTest {

    private static final Logger logger = LogManager.getLogger(ThemeToggle.class); // Log4j2 Logger

    @Test
    public void testFooterDarkModeToggle() { // Removed InterruptedException
        WebDriver driver = getDriver(); // Get the WebDriver instance from BaseTest
        driver.get("https://vhpkarnataka.org");
        logger.info("Navigated to homepage: {}", driver.getCurrentUrl());

        WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(10)); // Initialize WebDriverWait

        // Scroll to footer to ensure toggle visibility
        ((JavascriptExecutor) driver).executeScript("window.scrollTo(0, document.body.scrollHeight);");
        // Use an explicit wait for the element to be clickable after scroll, instead of Thread.sleep
        WebElement toggle = wait.until(ExpectedConditions.elementToBeClickable(By.id("dark-mode")));
        logger.debug("Scrolled to bottom and waited for dark mode toggle.");

        // Check initial data-state attribute
        String initialState = toggle.getAttribute("data-state");
        logger.info("🌗 Initial Theme State: {}", initialState);

        // Click the toggle button
        toggle.click();
        logger.debug("Clicked dark mode toggle.");

        // Wait for the data-state attribute to change
        // This is a much more robust wait than Thread.sleep
        wait.until(ExpectedConditions.attributeToBe(toggle, "data-state", initialState.equals("dark") ? "light" : "dark"));

        // Recheck the state
        String toggledState = toggle.getAttribute("data-state");
        logger.info("🌗 Toggled Theme State: {}", toggledState);

        // Assert that the state has changed
        Assert.assertNotEquals(initialState, toggledState, "Theme did not toggle. Initial: " + initialState + ", Toggled: " + toggledState);
        logger.info("✅ Dark/Light theme toggled successfully. State changed from '{}' to '{}'.", initialState, toggledState);

        // Optional: Verify if HTML class changed (dark/light class)
        // This provides an additional layer of verification that the theme actually applied
        WebElement htmlElement = driver.findElement(By.tagName("html"));
        String htmlClass = htmlElement.getAttribute("class");
        logger.info("📄 HTML Class after toggle: {}", htmlClass);

        // Further assertion: Check if the HTML class reflects the toggled state
        if ("dark".equals(toggledState)) {
            Assert.assertTrue(htmlClass.contains("dark"), "HTML tag class should contain 'dark' after toggling to dark mode.");
            logger.info("Verified HTML class contains 'dark'.");
        } else if ("light".equals(toggledState)) {
            Assert.assertTrue(htmlClass.contains("light"), "HTML tag class should contain 'light' after toggling to light mode.");
            logger.info("Verified HTML class contains 'light'.");
        } else {
            logger.warn("Unexpected toggled state: {}. Cannot verify HTML class precisely.", toggledState);
        }
        logger.info("Theme toggle test completed.");
    }
}