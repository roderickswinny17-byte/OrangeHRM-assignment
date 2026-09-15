package keywords;

import config.ConfigReader;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.openqa.selenium.WebDriver;
import pages.LoginPage;
import utils.ExtentReportManager;
import utils.TestDataReader;

import static org.testng.Assert.assertTrue;

public class LoginKeywords {

    private static final Logger log = LogManager.getLogger(LoginKeywords.class);
    private final LoginPage loginPage;

    public LoginKeywords(WebDriver driver) {
        this.loginPage = new LoginPage(driver);
    }

    public void launchApplication() {
        String url = ConfigReader.get("base.url");
        loginPage.navigateTo(url);
        log.info("Application launched: {}", url);
        ExtentReportManager.pass("Application launched: " + url);
    }

    public void loginWithTestData() {
        String username = TestDataReader.get("username");
        String password = TestDataReader.get("password");
        loginPage.login(username, password);
    }

    public void verifyLoginSuccess() {
        boolean visible = loginPage.isDashboardVisible();
        assertTrue(visible, "Dashboard should be visible after successful login");
        ExtentReportManager.pass("Login verified — Dashboard is visible.");
        log.info("Login success confirmed.");
    }

    public void logoutFromApplication() {
        loginPage.logout();
        log.info("Logged out successfully.");
    }

    public void verifySessionTerminated() {
        boolean loginVisible = loginPage.isLoginPageDisplayed();
        assertTrue(loginVisible, "Login page should be displayed after logout — session should be invalidated");
        ExtentReportManager.pass("Session terminated — login page displayed after logout.");
        log.info("Session termination verified.");
    }
}
