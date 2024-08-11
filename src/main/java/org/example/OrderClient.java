package org.example;

import io.qameta.allure.Step;
import io.restassured.response.ValidatableResponse;

import java.io.File;

import static io.restassured.RestAssured.given;

public class OrderClient extends RestClient{
    private static final String ORDER_PATH = "/api/v1/orders";

    @Step("Create order - post /api/v1/orders")
    public ValidatableResponse create(File orderJson) {
        return given()
                .spec(getBaseSpec())
                .body(orderJson)
                .when()
                .post(ORDER_PATH)
                .then();
    }

    @Step("Cancel order - put /api/v1/orders/cancel")
    public ValidatableResponse cancel(int track){
        return given()
                .spec(getBaseSpec())
                .body(OrderTrack.from(track))
                .when()
                .put(ORDER_PATH + "/cancel")
                .then();
    }

    @Step("Get all orders - get /api/v1/orders")
    public ValidatableResponse getAllOrders() {
        return given()
                .spec(getBaseSpec())
                .get(ORDER_PATH)
                .then();
    }
}
