package utils;

import config.ConfigReader;
import io.restassured.RestAssured;
import io.restassured.response.Response;
import io.restassured.specification.RequestSpecification;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.util.HashMap;
import java.util.Map;

public class ApiHelper {

    private static final Logger log = LogManager.getLogger(ApiHelper.class);
    private final Map<String, String> sessionCookies = new HashMap<>();

    public ApiHelper() {
        RestAssured.baseURI = ConfigReader.get("api.base.url");
    }

    public void createSession(String username, String password) {
        Response loginPage = RestAssured.given()
                .get("/web/index.php/auth/login");

        String token = loginPage.cookie("PHPSESSID");

        Response response = RestAssured.given()
                .cookie("PHPSESSID", token)
                .contentType("application/x-www-form-urlencoded")
                .formParam("_token", extractCsrfToken(loginPage.getBody().asString()))
                .formParam("username", username)
                .formParam("password", password)
                .post("/web/index.php/auth/validateCredentials");

        response.getCookies().forEach(sessionCookies::put);
        if (token != null) sessionCookies.put("PHPSESSID", token);

        log.info("API session established — cookies acquired: {}", sessionCookies.keySet());
    }

    public Response get(String endpoint) {
        RequestSpecification req = RestAssured.given().cookies(sessionCookies);
        Response response = req.get(endpoint);
        log.info("GET {} — status: {}", endpoint, response.getStatusCode());
        return response;
    }

    public Response get(String endpoint, Map<String, String> queryParams) {
        RequestSpecification req = RestAssured.given().cookies(sessionCookies);
        queryParams.forEach(req::queryParam);
        Response response = req.get(endpoint);
        log.info("GET {} params:{} — status: {}", endpoint, queryParams, response.getStatusCode());
        return response;
    }

    private String extractCsrfToken(String html) {
        int idx = html.indexOf("_token\" value=\"");
        if (idx == -1) return "";
        int start = idx + 15;
        int end = html.indexOf("\"", start);
        return end > start ? html.substring(start, end) : "";
    }

    public Map<String, String> getSessionCookies() {
        return sessionCookies;
    }
}
