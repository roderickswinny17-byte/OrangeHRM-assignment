package keywords;

import config.ConfigReader;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.openqa.selenium.WebDriver;
import pages.EmployeePage;
import utils.ExtentReportManager;
import utils.TestDataReader;

import java.io.File;

import static org.testng.Assert.assertTrue;

public class EmployeeKeywords {

    private static final Logger log = LogManager.getLogger(EmployeeKeywords.class);
    private final EmployeePage employeePage;

    public EmployeeKeywords(WebDriver driver) {
        this.employeePage = new EmployeePage(driver);
    }

    public void addNewEmployee() {
        String firstName = TestDataReader.get("firstName");
        String middleName = TestDataReader.get("middleName");
        String lastName = TestDataReader.get("lastName");
        String employeeId = TestDataReader.get("employeeId");

        employeePage.goToAddEmployee();
        employeePage.fillEmployeeForm(firstName, middleName, lastName, employeeId);

        String picturePath = new File(ConfigReader.get("profile.picture.path")).getAbsolutePath();
        employeePage.uploadProfilePicture(picturePath);
        employeePage.saveEmployee();

        log.info("Add employee flow complete — ID: {}", employeeId);
    }

    public void verifyEmployeeAdded() {
        boolean success = employeePage.isSaveSuccessful();
        assertTrue(success, "Success toast should appear after saving a new employee");
        ExtentReportManager.pass("New employee created — success toast confirmed.");
    }

    public void searchEmployee() {
        String employeeId = TestDataReader.get("employeeId");
        // Navigate to list page first — required regardless of current page state
        employeePage.goToEmployeeList();
        employeePage.searchByEmployeeId(employeeId);
    }

    public void verifyEmployeeAppearsInResults() {
        String employeeId = TestDataReader.get("employeeId");
        int count = employeePage.getSearchResultCount();
        assertTrue(count > 0, "At least one result expected for Employee ID [" + employeeId + "] — actual row count: " + count);
        ExtentReportManager.pass("Employee [" + employeeId + "] found — rows: <b>" + count + "</b>");
        log.info("Search returned {} row(s) for ID: {}", count, employeeId);
    }

    public void openEmployeeForEditing() {
        employeePage.clickEditOnFirstResult();
    }

    public void updateJobDetails() {
        String jobTitle = TestDataReader.get("jobTitle");
        String empStatus = TestDataReader.get("employmentStatus");

        employeePage.goToJobTab();
        employeePage.updateJobTitle(jobTitle);
        employeePage.updateEmploymentStatus(empStatus);
        employeePage.saveJobDetails();

        log.info("Job details updated — title: {} | status: {}", jobTitle, empStatus);
    }

    public void verifyJobDetailsUpdated() {
        String expectedTitle = TestDataReader.get("jobTitle");
        String expectedStatus = TestDataReader.get("employmentStatus");

        String actualTitle = employeePage.getSelectedJobTitle();
        String actualStatus = employeePage.getSelectedEmploymentStatus();

        assertTrue(actualTitle.contains(expectedTitle), "Job Title should contain [" + expectedTitle + "] — actual: [" + actualTitle + "]");
        assertTrue(actualStatus.contains(expectedStatus), "Employment Status should contain [" + expectedStatus + "] — actual: [" + actualStatus + "]");

        ExtentReportManager.pass("Job details verified — Title: <b>" + actualTitle + "</b> | Status: <b>" + actualStatus + "</b>");
        log.info("Job details verification passed.");
    }

    public void deleteEmployee() {
        searchEmployee();
        verifyEmployeeAppearsInResults();
        employeePage.deleteFirstResultEmployee();
        log.info("Delete triggered for employee ID: {}", TestDataReader.get("employeeId"));
    }

    public void verifyEmployeeDeleted() {
        boolean toastVisible = employeePage.isDeleteSuccessful();
        assertTrue(toastVisible, "Success toast should appear after deleting an employee");
        ExtentReportManager.pass("Delete success toast confirmed.");

        searchEmployee();
        boolean noRecords = employeePage.isNoRecordsDisplayed();
        assertTrue(noRecords, "No Records Found should be shown after deleting Employee ID: " + TestDataReader.get("employeeId"));
        ExtentReportManager.pass("Employee no longer in search results — deletion fully verified.");
        log.info("Employee deletion verified.");
    }
}