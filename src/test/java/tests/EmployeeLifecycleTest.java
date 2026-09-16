package tests;

import keywords.ApiKeywords;
import keywords.EmployeeKeywords;
import keywords.LoginKeywords;
import org.testng.annotations.Test;
import utils.ExtentReportManager;

public class EmployeeLifecycleTest extends BaseTest {

    @Test()
    public void employeeLifecycleManagement() {

        LoginKeywords    login    = new LoginKeywords(getDriver());
        EmployeeKeywords employee = new EmployeeKeywords(getDriver());
        ApiKeywords      api      = new ApiKeywords();

        // Step 1 — Login
        ExtentReportManager.info("=== STEP 1: Login ===");
        login.launchApplication();
        login.loginWithTestData();
        login.verifyLoginSuccess();

        // Step 2 — Add New Employee
        ExtentReportManager.info("=== STEP 2: Add New Employee ===");
        employee.addNewEmployee();
       // employee.verifyEmployeeAdded();

        // Step 3 — Edit Employee
        ExtentReportManager.info("=== STEP 3: Edit Employee Information ===");
        employee.searchEmployee();
        employee.verifyEmployeeAppearsInResults();
        employee.openEmployeeForEditing();
        employee.updateJobDetails();
        employee.verifyJobDetailsUpdated();

        // Step 4 — Delete Employee
        ExtentReportManager.info("=== STEP 4: Delete Employee ===");
        employee.deleteEmployee();
        employee.verifyEmployeeDeleted();
        api.verifyEmployeeDeletedViaApi();

        // Step 5 — Logout
        ExtentReportManager.info("=== STEP 5: Logout ===");
        login.logoutFromApplication();
        login.verifySessionTerminated();
    }
}
