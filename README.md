# OrangeHRM Automation Framework

Standalone Selenium + Java + TestNG framework for the OrangeHRM Employee Lifecycle test scenario.
No external proprietary dependencies — everything runs from public Maven Central packages.

---

## Framework Structure

```
orangehrm-automation/
├── src/
│   ├── main/java/
│   │   ├── config/
│   │   │   └── ConfigReader.java          # Reads config.properties
│   │   ├── pages/
│   │   │   ├── BasePage.java              # Common Selenium actions
│   │   │   ├── LoginPage.java             # Login page locators + actions
│   │   │   └── EmployeePage.java          # Employee module locators + actions
│   │   ├── keywords/
│   │   │   ├── LoginKeywords.java         # Login/logout business steps
│   │   │   ├── EmployeeKeywords.java      # Add/edit/delete employee steps
│   │   │   └── ApiKeywords.java           # REST API cross-validation steps
│   │   └── utils/
│   │       ├── DriverManager.java         # Thread-safe WebDriver factory
│   │       ├── WaitHelper.java            # Explicit wait wrappers
│   │       ├── ExtentReportManager.java   # HTML report wrapper
│   │       ├── ApiHelper.java             # REST Assured session + requests
│   │       └── TestDataReader.java        # JSON test data reader
│   └── test/
│       ├── java/tests/
│       │   ├── BaseTest.java              # TestNG @Before/@After + driver/report lifecycle
│       │   └── EmployeeLifecycleTest.java # E2E test — pure orchestrator
│       └── resources/
│           ├── config.properties          # Environment, browser, paths
│           ├── log4j2.xml                 # Logging config
│           └── testdata/
│               └── EmployeeData.json      # All test input values
├── reports/                               # Generated after test run
├── pom.xml
├── testng.xml
└── README.md
```

---

## Dependencies

| Library          | Version  | Purpose                        |
|------------------|----------|--------------------------------|
| Selenium Java    | 4.18.1   | Browser automation             |
| WebDriverManager | 5.7.0    | Auto browser driver management |
| TestNG           | 7.9.0    | Test runner                    |
| REST Assured     | 5.4.0    | API validation                 |
| ExtentReports    | 5.1.1    | HTML test report               |
| Log4j2           | 2.23.1   | Logging                        |
| Jackson Databind | 2.17.0   | JSON test data parsing         |
| Lombok           | 1.18.32  | Boilerplate reduction          |

---

## Setup Instructions

**Prerequisites:**
- Java 11+
- Maven 3.8+
- Chrome / Firefox / Edge installed

**Steps:**

1. Clone the repository
2. Place a sample image at `src/test/resources/testdata/profile.png`
3. Adjust `src/test/resources/config.properties` if needed (browser, headless, URLs)
4. Adjust `src/test/resources/testdata/EmployeeData.json` for your test employee values

---

## Running the Tests

```bash
# Full suite
mvn clean test

# Headless Chrome
mvn clean test -Dheadless=true

# Specific browser
mvn clean test -Dbrowser=firefox
```

The HTML report is generated at `reports/ExtentReport.html` after each run.

---

## Test Scenario

| Step | Action                                         | Verification                              |
|------|------------------------------------------------|-------------------------------------------|
| 1    | Login with Admin credentials                   | Dashboard is visible                      |
| 2    | Navigate to PIM > Add Employee, fill form, save | Success toast appears                    |
| 3    | Search by Employee ID, open edit, update Job tab | Changes saved and reflected on form      |
| 4    | API call to /pim/employees endpoint             | Employee data present in API response     |
| 5    | Delete employee from UI                         | Toast confirms, No Records in re-search, API confirms deletion |
| 6    | Logout                                          | Login page displayed — session terminated |
