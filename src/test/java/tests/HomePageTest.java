package tests;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.openqa.selenium.WebDriver; // For WebDriver interface
import org.testng.Assert;
import org.testng.annotations.Test;
import utils.BaseTest;// For @Test annotation

// Assuming BaseTest contains common setup and a getDriver() method.
// If BaseTest is in a different package, you might need to import it as well,
// e.g., import base.BaseTest; if its package is 'base'.
// import pages.BaseTest; // Example if your BaseTest is in a 'pages' package.


public class HomePageTest extends BaseTest {

    private static final Logger logger = LogManager.getLogger(HomePageTest.class);

    @Test
    public void verifyHomePageTitle() {
        logger.info("Starting verifyHomePageTitle test.");
        WebDriver driver = getDriver();
        driver.get("https://vhpkarnataka.org/");
        logger.debug("Navigated to homepage: {}", driver.getCurrentUrl());

        String actualTitle = driver.getTitle();
        String expectedTitlePart = "Vishwa Hindu Parishad Karnataka - Leading Hindu Organization in Karnataka";
        logger.info("Actual title: '{}', Expected part: '{}'", actualTitle, expectedTitlePart);

        if (!actualTitle.contains(expectedTitlePart)) {
            logger.error("Homepage title verification failed. Expected '{}' but found '{}'", expectedTitlePart, actualTitle);
        }
        Assert.assertTrue(actualTitle.contains(expectedTitlePart), "Title does not contain '" + expectedTitlePart + "'");
        logger.info("verifyHomePageTitle test completed successfully.");
    }
    //
}