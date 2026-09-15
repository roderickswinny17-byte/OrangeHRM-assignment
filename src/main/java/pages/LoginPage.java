package pages;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import utils.ExtentReportManager;

public class LoginPage extends BasePage {

    private static final By USERNAME_INPUT  = By.name("username");
    private static final By PASSWORD_INPUT  = By.name("password");
    private static final By LOGIN_BTN       = By.xpath("//button[@type='submit']");
    private static final By DASHBOARD_HDR   = By.xpath("//h6[normalize-space()='Dashboard']");
    private static final By INVALID_MSG     = By.xpath("//div[contains(@class,'oxd-alert-content')]//p");
    private static final By USER_DROPDOWN   = By.xpath("//li[contains(@class,'oxd-userdropdown')]");
    private static final By LOGOUT_LINK     = By.xpath("//a[@href='/web/index.php/auth/logout']");

    public LoginPage(WebDriver driver) {
        super(driver);
    }

    public void navigateTo(String url) {
        driver.get(url);
        wait.waitForVisible(USERNAME_INPUT);
        ExtentReportManager.info("Navigated to: " + url);
    }

    public void login(String username, String password) {
        type(USERNAME_INPUT, username, "Username");
        type(PASSWORD_INPUT, password, "Password");
        click(LOGIN_BTN, "Login button");
        wait.hardWait(2000);
        ExtentReportManager.pass("Login submitted for user: <b>" + username + "</b>");
    }

    public boolean isDashboardVisible() {
        return isDisplayed(DASHBOARD_HDR);
    }

    public void logout() {
        click(USER_DROPDOWN, "User dropdown");
        wait.hardWait(500);
        click(LOGOUT_LINK, "Logout link");
        wait.waitForVisible(USERNAME_INPUT);
        ExtentReportManager.pass("Logged out — login page displayed.");
    }

    public boolean isLoginPageDisplayed() {
        return isDisplayed(USERNAME_INPUT);
    }
}
