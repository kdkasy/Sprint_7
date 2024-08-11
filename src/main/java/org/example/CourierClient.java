package org.example;

import io.qameta.allure.Step;
import io.restassured.response.ValidatableResponse;

import static io.restassured.RestAssured.given;

public class CourierClient extends RestClient{
    private static final String COURIER_PATH = "/api/v1/courier";
    private static final String LOGIN_PATH = "/api/v1/courier/login";


    @Step("Create client - post to /api/v1/courier")
    public ValidatableResponse create(Courier courier) {
        return given()
                .spec(getBaseSpec())
                .body(courier)
                .when()
                .post(COURIER_PATH)
                .then();
    }

    @Step("Login courier - post /api/v1/courier/login")
    public ValidatableResponse login(CourierCredentials courierCredentials) {
        return given()
                .spec(getBaseSpec())
                .body(courierCredentials)
                .when()
                .post(LOGIN_PATH)
                .then();
    }

    @Step("Delete courier - delete /api/v1/courier/:id")
    public ValidatableResponse delete(int courierID) {
        return given()
                .spec(getBaseSpec())
                .body(CourierId.from(courierID))
                .when()
                .delete(COURIER_PATH + "/" + courierID)
                .then();
    }
}
