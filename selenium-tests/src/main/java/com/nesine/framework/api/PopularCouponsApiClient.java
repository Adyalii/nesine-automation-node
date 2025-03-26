package com.nesine.framework.api;

import com.nesine.framework.api.models.Coupon;
import io.restassured.RestAssured;
import io.restassured.http.ContentType;
import io.restassured.response.Response;

import java.util.List;

public class PopularCouponsApiClient {

    public List<Coupon> getPopularCoupons() {
        Response response = RestAssured
                .given()
                .contentType(ContentType.JSON)
                .body("{\"eventCount\":0}")
                .get("https://pc.nesine.com/v1/PopularCoupons?eventCount=0")
                .then()
                .statusCode(200)
                .extract()
                .response();

        return response.jsonPath().getList("d", Coupon.class);
    }
}