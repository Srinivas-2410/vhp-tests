package utils;

import io.github.bonigarcia.wdm.WebDriverManager;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.testng.annotations.AfterMethod;
import org.testng.annotations.BeforeMethod;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.time.Duration;

public class BaseTest {

    // Using ThreadLocal to manage WebDriver instances for parallel execution
    private static final ThreadLocal<WebDriver> driver = new ThreadLocal<>();
    private static final Logger logger = LogManager.getLogger(BaseTest.class);

    @BeforeMethod
    public void setup() {
        logger.info("Setting up WebDriver...");
        // Use WebDriverManager to automatically download and set up ChromeDriver
        WebDriverManager.chromedriver().setup();
        WebDriver chromeDriver = new ChromeDriver();
        driver.set(chromeDriver); // Store the WebDriver instance in ThreadLocal
        logger.info("WebDriver initialized.");

        // Maximize the browser window
        getDriver().manage().window().maximize();
        // Implicit wait (optional, but good for stability for general element presence)
        getDriver().manage().timeouts().implicitlyWait(Duration.ofSeconds(10));
    }

    // This method provides access to the WebDriver instance for test classes
    public WebDriver getDriver() {
        return driver.get();
    }

    @AfterMethod
    public void teardown() {
        if (getDriver() != null) {
            logger.info("Quitting WebDriver...");
            getDriver().quit(); // Close the browser
            driver.remove(); // Remove the driver from ThreadLocal
            logger.info("WebDriver quit.");
        }
    }
}