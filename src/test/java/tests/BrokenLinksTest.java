package tests;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver; // Import for WebDriver
import org.openqa.selenium.WebElement; // Import for WebElement
import org.testng.annotations.Test;
import utils.BaseTest; // Import your BaseTest class

import java.net.HttpURLConnection;
import java.net.URL;
import java.util.List;

public class BrokenLinksTest extends BaseTest {

    // Initialize Log4j2 Logger
    private static final Logger logger = LogManager.getLogger(BrokenLinksTest.class);

    @Test
    public void checkForBrokenLinksOnHomePage() {
        WebDriver driver = getDriver(); // Get the WebDriver instance from BaseTest
        driver.get("https://vhpkarnataka.org/");
        logger.info("Navigated to homepage: https://vhpkarnataka.org/");

        List<WebElement> allLinks = driver.findElements(By.tagName("a"));
        logger.info("Total links found: " + allLinks.size());

        for (WebElement link : allLinks) {
            String url = link.getAttribute("href");

            if (url == null || url.isEmpty() || url.startsWith("javascript")) {
                logger.debug("Skipping invalid or javascript link: " + url);
                continue;
            }

            try {
                HttpURLConnection connection = (HttpURLConnection) (new URL(url).openConnection());
                connection.setRequestMethod("HEAD");
                connection.connect();
                int statusCode = connection.getResponseCode();

                if (statusCode >= 400) {
                    logger.warn("❌ BROKEN LINK: {} → Status: {}", url, statusCode);
                } else {
                    logger.info("✅ OK LINK: {} → Status: {}", url, statusCode);
                }

            } catch (Exception e) {
                logger.error("⚠️ ERROR LINK: {} → Exception: {}", url, e.getMessage());
            }
        }
        logger.info("Broken links test completed.");
    }
}