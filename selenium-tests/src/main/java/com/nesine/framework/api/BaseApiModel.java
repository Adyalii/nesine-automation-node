package com.nesine.framework.api;

import io.restassured.builder.RequestSpecBuilder;
import io.restassured.builder.ResponseSpecBuilder;
import io.restassured.http.ContentType;
import io.restassured.specification.RequestSpecification;
import io.restassured.specification.ResponseSpecification;

import static org.hamcrest.Matchers.equalTo;

public class BaseApiModel {

    public static RequestSpecification requestSpec(String baseUri) {
        return new RequestSpecBuilder()
                .setBaseUri(baseUri)
                .build();
    }

    public static ResponseSpecification successResponseSpec() {
        return new ResponseSpecBuilder()
                .expectStatusCode(200)
                .expectContentType(ContentType.JSON)
                .expectBody("statusCode", equalTo(200))
                .build();
    }
}