package listeners;

import com.aventstack.extentreports.ExtentReports;
import com.aventstack.extentreports.ExtentTest;
import com.aventstack.extentreports.Status;
import com.aventstack.extentreports.reporter.ExtentSparkReporter;
import com.aventstack.extentreports.reporter.configuration.Theme;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.openqa.selenium.OutputType;
import org.openqa.selenium.TakesScreenshot;
import org.openqa.selenium.WebDriver;
import org.testng.ITestContext;
import org.testng.ITestListener;
import org.testng.ITestResult;
import utils.BaseTest; // Import your BaseTest class, which contains getDriver()

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.text.SimpleDateFormat;
import java.util.Date;

// Renamed the class from ExtentReports to ExtentReportListener to avoid name collision
public class ExtentReportListener implements ITestListener {

    // Using the imported ExtentReports class
    private static ExtentReports extent;
    private static ThreadLocal<ExtentTest> test = new ThreadLocal<>();
    private static final Logger logger = LogManager.getLogger(ExtentReportListener.class); // Log4j2 Logger

    @Override
    public void onStart(ITestContext context) {
        String reportName = "TestAutomationReport_" + new SimpleDateFormat("yyyyMMdd_HHmmss").format(new Date()) + ".html";
        String path = System.getProperty("user.dir") + "/test-output/ExtentReports/" + reportName;
        ExtentSparkReporter sparkReporter = new ExtentSparkReporter(path);
        sparkReporter.config().setDocumentTitle("VHP Website Test Report");
        sparkReporter.config().setReportName("VHP Automation Results");
        sparkReporter.config().setTheme(Theme.STANDARD); // Or Theme.DARK

        extent = new ExtentReports(); // Correct usage of the imported class
        extent.attachReporter(sparkReporter);

        extent.setSystemInfo("Tester", "Srinivas K S");
        extent.setSystemInfo("OS", System.getProperty("os.name"));
        extent.setSystemInfo("Browser", "Chrome"); // You can dynamically get this

        logger.info("ExtentReports generation started. Report will be saved at: {}", path); // Log start
    }

    @Override
    public void onTestStart(ITestResult result) {
        ExtentTest extentTest = extent.createTest(result.getMethod().getMethodName());
        test.set(extentTest);
        logger.info("Test Started: {}", result.getMethod().getMethodName()); // Log test start
    }

    @Override
    public void onTestSuccess(ITestResult result) {
        test.get().log(Status.PASS, "Test Passed");
        logger.info("Test Passed: {}", result.getMethod().getMethodName()); // Log test success
    }

    @Override
    public void onTestFailure(ITestResult result) {
        test.get().log(Status.FAIL, "Test Failed");
        test.get().fail(result.getThrowable()); // Log the exception/error

        logger.error("Test Failed: {} - {}", result.getMethod().getMethodName(), result.getThrowable().getMessage()); // Log test failure

        // Capture screenshot on failure
        // Corrected: Cast to BaseTest to access getDriver()
        WebDriver driver = null;
        Object instance = result.getInstance();
        if (instance instanceof BaseTest) { // Check if the test instance is a BaseTest (or extends it)
            driver = ((BaseTest) instance).getDriver();
        } else {
            logger.warn("Warning: Test instance is not a BaseTest for {}. Cannot capture screenshot reliably.", result.getMethod().getMethodName());
        }

        if (driver != null) {
            String screenshotPath = takeScreenshot(driver, result.getMethod().getMethodName());
            test.get().addScreenCaptureFromPath(screenshotPath, "Screenshot of Failure");
			logger.debug("Screenshot captured for failed test: {}", result.getMethod().getMethodName()); // Log screenshot
        }
    }

    @Override
    public void onTestSkipped(ITestResult result) {
        test.get().log(Status.SKIP, "Test Skipped");
        test.get().skip(result.getThrowable());
        logger.warn("Test Skipped: {} - {}", result.getMethod().getMethodName(), result.getThrowable() != null ? result.getThrowable().getMessage() : "No reason specified"); // Log test skip
    }

    @Override
    public void onFinish(ITestContext context) {
        extent.flush(); // Essential to write report to file
        logger.info("ExtentReports generation finished."); // Log finish
    }

    // Helper method to take screenshot
    private String takeScreenshot(WebDriver driver, String screenshotName) {
        String dest = null;
        try {
            File src = ((TakesScreenshot) driver).getScreenshotAs(OutputType.FILE);
            String timestamp = new SimpleDateFormat("yyyyMMdd_HHmmss").format(new Date());
            dest = System.getProperty("user.dir") + "/test-output/ExtentReports/screenshots/" + screenshotName + "_" + timestamp + ".png";
            Files.createDirectories(Paths.get(System.getProperty("user.dir") + "/test-output/ExtentReports/screenshots/"));
            Files.copy(src.toPath(), Paths.get(dest));
        } catch (IOException e) {
            e.printStackTrace();
        }
        return dest;
    }
}