package pages;

import org.openqa.selenium.WebDriver;
import org.openqa.selenium.support.PageFactory;
import org.openqa.selenium.support.ui.WebDriverWait;
import java.time.Duration;

public class BasePage {

    protected WebDriver driver;
    protected WebDriverWait wait;

    public BasePage(WebDriver driver) {
        this.driver = driver;
        // Initialize elements of the page
        PageFactory.initElements(driver, this);
        // Initialize WebDriverWait for explicit waits
        this.wait = new WebDriverWait(driver, Duration.ofSeconds(15));
    }

    // You can add common methods here that are applicable to all pages, e.g.:
    public String getPageTitle() {
        return driver.getTitle();
    }

    // Example of a common navigation method (optional, depending on your app)
    public void navigateToUrl(String url) {
        driver.get(url);
    }
}