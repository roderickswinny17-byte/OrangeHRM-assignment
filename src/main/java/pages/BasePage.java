package pages;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.openqa.selenium.By;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import utils.WaitHelper;

import java.util.List;

public abstract class BasePage {

    protected final Logger log = LogManager.getLogger(this.getClass());
    protected final WebDriver driver;
    protected final WaitHelper wait;

    protected BasePage(WebDriver driver) {
        this.driver = driver;
        this.wait = new WaitHelper(driver);
    }

    protected WebElement find(By locator) {
        return wait.waitForVisible(locator);
    }

    protected void click(By locator, String elementName) {
        wait.waitForClickable(locator).click();
        log.info("Clicked: {}", elementName);
    }

    protected void type(By locator, String text, String fieldName) {
        WebElement el = find(locator);
        el.clear();
        el.sendKeys(text);
        log.info("Typed into {}: {}", fieldName, text);
    }

    protected String getText(By locator) {
        return find(locator).getText().trim();
    }

    protected boolean isDisplayed(By locator) {
        try {
            return driver.findElement(locator).isDisplayed();
        } catch (Exception e) {
            return false;
        }
    }

    protected List<WebElement> findAll(By locator) {
        return driver.findElements(locator);
    }

    protected void scrollIntoView(WebElement element) {
        ((JavascriptExecutor) driver).executeScript("arguments[0].scrollIntoView({block:'center'});", element);
    }

    protected void selectDropdownOption(By dropdownLocator, String optionText) {
        wait.waitForClickable(dropdownLocator).click();
        wait.hardWait(600);
        By option = By.xpath("//div[@role='listbox']//span[normalize-space()='" + optionText + "']");
        wait.waitForVisible(option);
        wait.waitForClickable(option).click();
        wait.hardWait(400);
        log.info("Selected dropdown option: {}", optionText);
    }
}