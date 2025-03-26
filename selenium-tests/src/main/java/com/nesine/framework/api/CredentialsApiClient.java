package com.nesine.framework.api;

import com.nesine.framework.pages.PopularCouponsPage;
import com.nesine.framework.utils.ConfigReader;
import io.restassured.RestAssured;
import io.restassured.response.Response;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.util.HashMap;
import java.util.Map;

public class CredentialsApiClient {

    private static final Logger logger = LogManager.getLogger(CredentialsApiClient.class);

    public Map<String, String> getCredentials() {
        logger.info("Requesting credentials from API.");
        Response response = RestAssured
                .given()
                .spec(BaseApiModel.requestSpec(ConfigReader.getBaseApiUrl()))
                .when()
                .get("/getCredentials")
                .then()
                .spec(BaseApiModel.successResponseSpec())
                .extract()
                .response();

        Map<String, String> credentials = new HashMap<>();
        credentials.put("username", response.path("data.username"));
        credentials.put("password", response.path("data.password"));

        logger.info("Credentials retrieved successfully.");
        return credentials;
    }


}