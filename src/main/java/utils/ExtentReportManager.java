package utils;

import com.aventstack.extentreports.ExtentReports;
import com.aventstack.extentreports.ExtentTest;
import com.aventstack.extentreports.Status;
import com.aventstack.extentreports.reporter.ExtentSparkReporter;
import com.aventstack.extentreports.reporter.configuration.Theme;
import config.ConfigReader;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public class ExtentReportManager {

    private static final Logger log = LogManager.getLogger(ExtentReportManager.class);
    private static ExtentReports extent;
    private static final ThreadLocal<ExtentTest> testThread = new ThreadLocal<>();

    private ExtentReportManager() {}

    public static void initReports() {
        String reportPath = ConfigReader.get("report.path");
        ExtentSparkReporter spark = new ExtentSparkReporter(reportPath);
        spark.config().setTheme(Theme.DARK);
        spark.config().setDocumentTitle(ConfigReader.get("report.title"));
        spark.config().setReportName(ConfigReader.get("report.name"));
        spark.config().setTimeStampFormat("dd-MMM-yyyy HH:mm:ss");

        extent = new ExtentReports();
        extent.attachReporter(spark);
        extent.setSystemInfo("Environment", ConfigReader.get("base.url"));
        extent.setSystemInfo("Browser", ConfigReader.get("browser"));
        log.info("Extent report initialised at: {}", reportPath);
    }

    public static ExtentTest createTest(String testName, String description) {
        ExtentTest test = extent.createTest(testName, description);
        testThread.set(test);
        return test;
    }

    public static ExtentTest getTest() {
        return testThread.get();
    }

    public static void log(Status status, String message) {
        if (testThread.get() != null) testThread.get().log(status, message);
    }

    public static void pass(String message) {
        log(Status.PASS, message);
        log.info("PASS — {}", message);
    }

    public static void fail(String message) {
        log(Status.FAIL, message);
        log.error("FAIL — {}", message);
    }

    public static void warn(String message) {
        log(Status.WARNING, message);
        log.warn("WARN — {}", message);
    }

    public static void info(String message) {
        log(Status.INFO, message);
        log.info("INFO — {}", message);
    }

    public static void flushReports() {
        if (extent != null) {
            extent.flush();
            log.info("Extent report flushed.");
        }
    }
}
