package com.nesine.framework.api;

import com.nesine.framework.api.models.Coupon;
import io.restassured.RestAssured;
import io.restassured.http.ContentType;
import io.restassured.response.Response;
import java.util.List;

public class ApiMappingTest {

    public static void main(String[] args) {
        Response response = RestAssured
                .given()
                .contentType(ContentType.JSON)
                .body("{\"eventCount\":0}")
                .get("https://pc.nesine.com/v1/PopularCoupons?eventCount=0")
                .then()
                .statusCode(200)
                .extract()
                .response();

        System.out.println("Raw JSON response:");
        System.out.println(response.asString());

        List<Coupon> coupons = response.jsonPath().getList("d", Coupon.class);

    }
}