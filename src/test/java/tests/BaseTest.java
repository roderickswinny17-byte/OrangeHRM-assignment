package tests;

import com.aventstack.extentreports.Status;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.openqa.selenium.WebDriver;
import org.testng.ITestResult;
import org.testng.annotations.AfterMethod;
import org.testng.annotations.AfterSuite;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.BeforeSuite;
import utils.DriverManager;
import utils.ExtentReportManager;

import java.lang.reflect.Method;

public class BaseTest {

    protected static final Logger log = LogManager.getLogger(BaseTest.class);

    @BeforeSuite(alwaysRun = true)
    public void initReports() {
        ExtentReportManager.initReports();
        log.info("Test suite started — reports initialised.");
    }

    @BeforeMethod(alwaysRun = true)
    public void setUp(Method method) {
        DriverManager.initDriver();
        String testName = method.getName();
        String description = method.isAnnotationPresent(org.testng.annotations.Test.class)
                ? method.getAnnotation(org.testng.annotations.Test.class).description()
                : testName;
        ExtentReportManager.createTest(testName, description);
        log.info("Test started: {}", testName);
    }

    @AfterMethod(alwaysRun = true)
    public void tearDown(ITestResult result) {
        String testName = result.getName();
        if (result.getStatus() == ITestResult.FAILURE) {
            ExtentReportManager.fail("Test FAILED: " + result.getThrowable().getMessage());
            log.error("Test FAILED: {} — {}", testName, result.getThrowable().getMessage());
        } else if (result.getStatus() == ITestResult.SKIP) {
            ExtentReportManager.log(Status.SKIP, "Test SKIPPED.");
            log.warn("Test SKIPPED: {}", testName);
        } else {
            ExtentReportManager.pass("Test PASSED.");
            log.info("Test PASSED: {}", testName);
        }
        DriverManager.quitDriver();
    }

    @AfterSuite(alwaysRun = true)
    public void flushReports() {
        ExtentReportManager.flushReports();
        log.info("Test suite complete — report flushed.");
    }

    protected WebDriver getDriver() {
        return DriverManager.getDriver();
    }
}
