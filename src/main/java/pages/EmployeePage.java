package pages;

import org.openqa.selenium.*;
import utils.ExtentReportManager;

import java.util.List;

public class EmployeePage extends BasePage {

    // Left sidebar PIM menu item
    private static final By PIM_MENU = By.xpath("//li[contains(@class,'oxd-main-menu-item-wrapper')]//span[normalize-space()='PIM']");

    // Top nav tabs that appear after PIM is clicked — matched by visible text only, no class dependency
    private static final By ADD_EMPLOYEE_TAB = By.xpath("//button[normalize-space()='Add']//i");
    private static final By EMPLOYEE_LIST_TAB = By.xpath("//div[contains(@class,'oxd-topbar-body-nav')]//a[normalize-space()='Employee List']");

    // Add Employee form fields
    private static final By FIRST_NAME = By.xpath("//input[@name='firstName']");
    private static final By MIDDLE_NAME = By.xpath("//input[@name='middleName']");
    private static final By LAST_NAME = By.xpath("//input[@name='lastName']");
    private static final By EMPLOYEE_ID = By.xpath("//label[text()='Employee Id']/ancestor::div[contains(@class,'oxd-input-group')]//input");
    private static final By PROFILE_PIC_INPUT = By.xpath("//input[contains(@class,'oxd-file-input')]");
    private static final By SAVE_BTN = By.xpath("//button[normalize-space()='Save']");
    private static final By SUCCESS_TOAST = By.xpath("//div[contains(@class,'oxd-toast') and contains(@class,'oxd-toast--success')]");

    // Employee List search form
    private static final By EMP_ID_SEARCH = By.xpath("//div[contains(@class,'oxd-input-group')][.//label[normalize-space()='Employee Id']]//input");
    private static final By SEARCH_BTN = By.xpath("//button[normalize-space()='Search']");
    private static final By RESULT_ROWS = By.xpath("//div[contains(@class,'oxd-table-body')]//div[@role='row']");
    private static final By NO_RECORDS_MSG = By.xpath("//span[normalize-space()='No Records Found']");

    // Row action buttons
    private static final By FIRST_ROW_EDIT = By.xpath("(//div[contains(@class,'oxd-table-body')]//div[@role='row'])[1]//button[.//i[contains(@class,'bi-pencil-fill')]]");
    private static final By FIRST_ROW_DELETE = By.xpath("(//div[contains(@class,'oxd-table-body')]//div[@role='row'])[1]//button[.//i[contains(@class,'bi-trash')]]");

    // Edit employee — Job tab
    private static final By JOB_TAB = By.xpath("//div[contains(@class,'orangehrm-tabs')]//a[normalize-space()='Job']");
    private static final By JOB_TITLE_DDL = By.xpath("//label[normalize-space()='Job Title']/following-sibling::div//div[contains(@class,'oxd-select-text')]");
    private static final By EMP_STATUS_DDL = By.xpath("//label[normalize-space()='Employment Status']/following-sibling::div//div[contains(@class,'oxd-select-text')]");

    // Delete confirm modal
    private static final By DELETE_CONFIRM_YES = By.xpath("//button[@type='button' and normalize-space()='Yes, Delete']");

    public EmployeePage(WebDriver driver) {
        super(driver);
    }

    public void goToAddEmployee() {
        // Click PIM in left sidebar
        wait.waitForClickable(PIM_MENU).click();
        log.info("Clicked PIM menu");
        wait.hardWait(2000);

        // Wait for topbar to render then click Add Employee tab
        wait.waitForClickable(ADD_EMPLOYEE_TAB).click();
        log.info("Clicked Add Employee tab");

        wait.waitForVisible(FIRST_NAME);
        ExtentReportManager.pass("Navigated to PIM > Add Employee.");
    }

    public void goToEmployeeList() {
        wait.waitForClickable(PIM_MENU).click();
        log.info("Clicked PIM menu");
        wait.hardWait(2000);

        ExtentReportManager.info("Navigated to Employee List.");
    }

    public void fillEmployeeForm(String firstName, String middleName, String lastName, String employeeId) {
        wait.waitForVisible(FIRST_NAME);

        WebElement fn = driver.findElement(FIRST_NAME);
        fn.clear();
        fn.sendKeys(firstName);

        if (middleName != null && !middleName.isEmpty()) {
            WebElement mn = driver.findElement(MIDDLE_NAME);
            mn.clear();
            mn.sendKeys(middleName);
        }

        WebElement ln = driver.findElement(LAST_NAME);
        ln.clear();
        ln.sendKeys(lastName);

// Clear existing Employee ID using Ctrl+A + Backspace
        WebElement idField = driver.findElement(EMPLOYEE_ID);

        idField.click();
        idField.sendKeys(Keys.CONTROL, "a");
        idField.sendKeys(Keys.BACK_SPACE);

        idField.sendKeys(employeeId);

        ExtentReportManager.pass("Form filled — Name: <b>" + firstName + " " + lastName + "</b> | ID: <b>" + employeeId + "</b>");
    }

    public void uploadProfilePicture(String absolutePath) {
        if (absolutePath == null || absolutePath.isEmpty()) {
            ExtentReportManager.warn("Profile picture path not provided — skipped.");
            return;
        }
        try {
            WebElement fileInput = driver.findElement(PROFILE_PIC_INPUT);
            fileInput.sendKeys(absolutePath);
            wait.hardWait(1500);
            ExtentReportManager.pass("Profile picture uploaded.");
        } catch (Exception e) {
            ExtentReportManager.warn("Profile picture upload skipped: " + e.getMessage());
        }
    }

    public void saveEmployee() {
        click(SAVE_BTN, "Save button");
        wait.hardWait(3000);
        ExtentReportManager.pass("Save button clicked.");
    }

    public boolean isSaveSuccessful() {
        return isDisplayed(SUCCESS_TOAST);
    }

    public void searchByEmployeeId(String employeeId) {
        wait.waitForVisible(PIM_MENU);
        click(PIM_MENU, "PIM menu");
        wait.waitForVisible(EMP_ID_SEARCH);
        WebElement field = driver.findElement(EMP_ID_SEARCH);
        field.clear();
        field.sendKeys(employeeId);
        wait.hardWait(500);
        click(SEARCH_BTN, "Search button");
        wait.hardWait(3000);
        ExtentReportManager.info("Searched by Employee ID: <b>" + employeeId + "</b>");
    }

    public int getSearchResultCount() {
        List<WebElement> rows = findAll(RESULT_ROWS);
        return rows == null ? 0 : rows.size();
    }

    public boolean isNoRecordsDisplayed() {
        return isDisplayed(NO_RECORDS_MSG);
    }

    public void clickEditOnFirstResult() {
        wait.waitForClickable(FIRST_ROW_EDIT);
        click(FIRST_ROW_EDIT, "Edit button — first row");
        wait.hardWait(3000);
        ExtentReportManager.info("Opened first result for editing.");
    }

    public void goToJobTab() {
        wait.waitForClickable(JOB_TAB);
        click(JOB_TAB, "Job tab");
        wait.hardWait(2000);
        ExtentReportManager.info("Job tab selected.");
    }

    public void updateJobTitle(String jobTitle) {
        selectDropdownOption(JOB_TITLE_DDL, jobTitle);
        ExtentReportManager.info("Job Title set to: <b>" + jobTitle + "</b>");
    }

    public void updateEmploymentStatus(String status) {
        selectDropdownOption(EMP_STATUS_DDL, status);
        ExtentReportManager.info("Employment Status set to: <b>" + status + "</b>");
    }

    public void saveJobDetails() {
        click(SAVE_BTN, "Save job details");
        wait.hardWait(3000);
        ExtentReportManager.pass("Job details saved.");
    }

    public String getSelectedJobTitle() {
        return getText(JOB_TITLE_DDL);
    }

    public String getSelectedEmploymentStatus() {
        return getText(EMP_STATUS_DDL);
    }

    public void deleteFirstResultEmployee() {
        wait.waitForClickable(FIRST_ROW_DELETE);
        click(FIRST_ROW_DELETE, "Delete button — first row");
        wait.hardWait(1000);
        wait.waitForClickable(DELETE_CONFIRM_YES);
        click(DELETE_CONFIRM_YES, "Confirm delete — Yes, Delete");
        wait.hardWait(3000);
        ExtentReportManager.pass("Employee deletion confirmed.");
    }

    public boolean isDeleteSuccessful() {
        return isDisplayed(SUCCESS_TOAST);
    }
}