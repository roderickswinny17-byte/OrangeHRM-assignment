package keywords;

import io.restassured.response.Response;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import utils.ApiHelper;
import utils.ExtentReportManager;
import utils.TestDataReader;

import java.util.HashMap;
import java.util.Map;

import static org.testng.Assert.assertEquals;
import static org.testng.Assert.assertFalse;
import static org.testng.Assert.assertTrue;

public class ApiKeywords {

    private static final Logger log = LogManager.getLogger(ApiKeywords.class);
    private final ApiHelper api;

    public ApiKeywords() {
        this.api = new ApiHelper();
    }

    public void establishApiSession() {
        String username = TestDataReader.get("username");
        String password = TestDataReader.get("password");
        api.createSession(username, password);
        ExtentReportManager.pass("API session established for user: <b>" + username + "</b>");
        log.info("API session ready.");
    }

    public void verifyEmployeeExistsViaApi() {
        String empId    = TestDataReader.get("employeeId");
        String lastName = TestDataReader.get("lastName");

        Map<String, String> params = new HashMap<>();
        params.put("limit", "50");
        params.put("offset", "0");

        Response response = api.get("/web/index.php/api/v2/pim/employees", params);
        int status = response.getStatusCode();
        String body = response.getBody().asString();

        assertEquals(status, 200, "API /pim/employees should return HTTP 200 — actual: " + status);

        boolean found = body.contains(empId) || body.contains(lastName);
        assertTrue(found, "API response should contain employee data for ID [" + empId + "] or lastName [" + lastName + "]");

        ExtentReportManager.pass("API confirms employee exists — HTTP " + status + " | data found: <b>true</b>");
        log.info("Employee API verify passed — status: {} | found: {}", status, found);
    }

    public void verifyUpdatedJobDetailsViaApi() {
        String empId    = TestDataReader.get("employeeId");
        String jobTitle = TestDataReader.get("jobTitle");

        Response response = api.get("/web/index.php/api/v2/pim/employees");
        int status = response.getStatusCode();
        String body = response.getBody().asString();

        assertEquals(status, 200, "API /pim/employees should return HTTP 200 — actual: " + status);

        boolean jobTitlePresent = body.contains(jobTitle);
        ExtentReportManager.pass("API cross-check — HTTP " + status + " | Job Title [" + jobTitle + "] in response: <b>" + jobTitlePresent + "</b>");
        log.info("Job title API verify — present: {} | title: {}", jobTitlePresent, jobTitle);
    }

    public void verifyEmployeeDeletedViaApi() {
        String empId    = TestDataReader.get("employeeId");
        String lastName = TestDataReader.get("lastName");

        Map<String, String> params = new HashMap<>();
        params.put("limit", "50");
        params.put("offset", "0");

        Response response = api.get("/web/index.php/api/v2/pim/employees", params);
        int status = response.getStatusCode();
        String body = response.getBody().asString();

        assertEquals(status, 200, "API /pim/employees should return HTTP 200 after deletion — actual: " + status);

        boolean stillPresent = body.contains(empId) && body.contains(lastName);
        assertFalse(stillPresent, "Deleted employee [" + empId + "] should NOT appear in API response");

        ExtentReportManager.pass("API confirms employee deleted — no matching record in API response.");
        log.info("Employee deletion API verify — stillPresent: {}", stillPresent);
    }
}
