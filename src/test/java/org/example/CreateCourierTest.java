package org.example;

import io.qameta.allure.junit4.DisplayName;
import io.restassured.response.ValidatableResponse;
import org.apache.http.HttpStatus;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.*;

public class CreateCourierTest {
    private CourierClient courierClient;
    private Courier courier;
    private int courierId;


    @Before
    public void setUp(){
        courierClient = new CourierClient();
        courier = CourierGenerator.createNewCourier();
    }

    @After
    public void cleanUp(){
        ValidatableResponse deleteResponse = courierClient.delete(courierId);
        assertTrue(deleteResponse.extract().path("ok"));

    }

    @Test
    @DisplayName("Courier can be created")
    public void courierCanBeCreated() {
        ValidatableResponse createResponse = courierClient.create(courier);
        int createStatusCode = createResponse.extract().statusCode();
        assertEquals(HttpStatus.SC_CREATED, createStatusCode);

        boolean created = createResponse.extract().path("ok");
        assertTrue(created);

        ValidatableResponse loginResponse = courierClient.login(CourierCredentials.from(courier));
        int loginStatusCode = loginResponse.extract().statusCode();
        assertEquals(HttpStatus.SC_OK, loginStatusCode);

        courierId = loginResponse.extract().path("id");
        assertNotEquals(0, courierId);
    }

    @Test
    @DisplayName("Could not create two identical couriers")
    public void couldNotCreateTwoIdenticalCouriers() {
        ValidatableResponse createResponse = courierClient.create(courier);
        assertEquals(HttpStatus.SC_CREATED, createResponse.extract().statusCode());
        ValidatableResponse loginResponse = courierClient.login(CourierCredentials.from(courier));
        assertEquals(HttpStatus.SC_OK, loginResponse.extract().statusCode());
        courierId = loginResponse.extract().path("id");

        Courier identicalCourier = new Courier(courier.getLogin(), courier.getPassword(), courier.getFirstName()); //
        ValidatableResponse createIdenticalResponse = courierClient.create(identicalCourier);
        int createStatusCode = createIdenticalResponse.extract().statusCode();
        assertEquals(HttpStatus.SC_CONFLICT, createStatusCode);
        String message = createIdenticalResponse.extract().path("message");
        assertEquals("Этот логин уже используется. Попробуйте другой.", message);
    }

    @Test
    @DisplayName("Could not create courier with same login")
    public void couldNotCreateCourierWithSameLogin() {
        ValidatableResponse createResponse = courierClient.create(courier);
        assertEquals(HttpStatus.SC_CREATED, createResponse.extract().statusCode());
        ValidatableResponse loginResponse = courierClient.login(CourierCredentials.from(courier));
        assertEquals(HttpStatus.SC_OK, loginResponse.extract().statusCode());
        courierId = loginResponse.extract().path("id");

        Courier courierWithSameLogin = CourierGenerator.createNewCourier();
        courierWithSameLogin.setLogin(courier.getLogin());
        ValidatableResponse createWithSameLogin = courierClient.create(courierWithSameLogin);
        int createStatusCode = createWithSameLogin.extract().statusCode();
        assertEquals(HttpStatus.SC_CONFLICT, createStatusCode);
        String message = createWithSameLogin.extract().path("message");
        assertEquals("Этот логин уже используется. Попробуйте другой.", message);
    }
}
