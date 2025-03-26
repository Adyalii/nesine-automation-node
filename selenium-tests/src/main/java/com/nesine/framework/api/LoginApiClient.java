package com.nesine.framework.api;

import com.nesine.framework.utils.ConfigReader;
import com.nesine.framework.utils.EnvFileUpdater;
import io.restassured.RestAssured;
import io.restassured.response.Response;

import java.util.Map;

public class LoginApiClient {

    public Map<String, String> doLogin(String username, String password) {
        String baseUrl = ConfigReader.getBaseUrl();
        String loginEndpoint = "/Auth/LoginV2";

        Response response = RestAssured
                .given()
                .spec(BaseApiModel.requestSpec(baseUrl))
                .multiPart("i", username)
                .multiPart("pw", password)
                .multiPart("rme", "false")
                .multiPart("rn", "227232")
                .multiPart("rol", "")
                .when()
                .post(loginEndpoint)
                .then()
                .extract()
                .response();

        Map<String, String> cookies = response.getCookies();

        EnvFileUpdater.updateKeys(cookies);

        return cookies;
    }
}
