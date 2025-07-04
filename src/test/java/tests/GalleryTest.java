package tests;

import utils.BaseTest; // Import BaseTest for WebDriver management
import org.apache.logging.log4j.LogManager; // For Log4j2
import org.apache.logging.log4j.Logger;     // For Log4j2
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;         // Import for WebDriver
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.ExpectedConditions; // For explicit waits
import org.openqa.selenium.support.ui.WebDriverWait;     // For explicit waits
import org.testng.annotations.Test;

import java.time.Duration; // For Duration in WebDriverWait
import java.util.List;

public class GalleryTest extends BaseTest {

    private static final Logger logger = LogManager.getLogger(GalleryTest.class); // Log4j2 Logger

    @Test
    public void testGalleryImages() { // Removed InterruptedException as Thread.sleep is replaced
        WebDriver driver = getDriver(); // Get the WebDriver instance from BaseTest
        driver.get("https://vhpkarnataka.org/resources/multimedia");
        logger.info("Navigated to gallery page: {}", driver.getCurrentUrl());

        WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(20)); // Increased wait time for images

        // Wait for at least one image to be present/visible, or for a reasonable number of elements
        // This is more reliable than Thread.sleep() for lazy-loaded images
        try {
            wait.until(ExpectedConditions.presenceOfElementLocated(By.cssSelector("img.object-cover")));
            logger.debug("Waited for images to be present.");
        } catch (org.openqa.selenium.TimeoutException e) {
            logger.warn("Timeout waiting for gallery images to load. Proceeding with found elements.");
        }


        // Select images by the Next.js-specific class
        List<WebElement> images = driver.findElements(By.cssSelector("img.object-cover"));

        logger.info("🖼️ Total gallery images found: " + images.size());

        int invisibleCount = 0;

        for (WebElement img : images) {
            String src = img.getAttribute("src");
            String srcset = img.getAttribute("srcset");

            // You might want to scroll the image into view before checking isDisplayed()
            // This is crucial for elements that are lazy-loaded or not in the viewport.
            // ((org.openqa.selenium.JavascriptExecutor) driver).executeScript("arguments[0].scrollIntoView(true);", img);
            // wait.until(ExpectedConditions.visibilityOf(img)); // Wait for it to become visible after scrolling

            boolean isDisplayed = img.isDisplayed();

            if (!isDisplayed) {
                logger.warn("❌ NOT DISPLAYED: {}", src);
                invisibleCount++;
            } else {
                logger.info("✅ IMAGE DISPLAYED: {}", src);
            }

            // Optional: Print original image URL if possible
            if (srcset != null) {
                logger.debug("🔗 srcset for {}: {}", src, srcset);
            }
        }

        logger.info("✅ Gallery test completed. Hidden images: {}", invisibleCount);
        // You might want to add an assertion here, e.g., to ensure no images are hidden
        // Assert.assertEquals(invisibleCount, 0, "Some gallery images are not displayed!");
    }
}