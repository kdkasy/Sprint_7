package org.example;

import io.qameta.allure.junit4.DisplayName;
import io.restassured.response.ValidatableResponse;
import org.apache.http.HttpStatus;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.*;

public class LoginTest {
    private CourierClient courierClient;
    private Courier courier;
    private int courierId;


    @Before
    public void setUp(){
        courierClient = new CourierClient();
        courier = CourierGenerator.createNewCourier();
        ValidatableResponse createResponse = courierClient.create(courier);
        assertEquals(HttpStatus.SC_CREATED, createResponse.extract().statusCode());
        ValidatableResponse loginResponse = courierClient.login(CourierCredentials.from(courier));
        assertEquals(HttpStatus.SC_OK, loginResponse.extract().statusCode());
        courierId = loginResponse.extract().path("id");
    }

    @After
    public void cleanUp(){
        ValidatableResponse deleteResponse = courierClient.delete(courierId);
        assertTrue(deleteResponse.extract().path("ok"));
    }

    @Test
    @DisplayName("Courier can log in")
    public void courierCanBeLoggedIn() {
        ValidatableResponse loginResponse = courierClient.login(CourierCredentials.from(courier));
        int loginStatusCode = loginResponse.extract().statusCode();
        assertEquals(HttpStatus.SC_OK, loginStatusCode);

        courierId = loginResponse.extract().path("id");
        assertNotEquals(0, courierId);
    }

    @Test
    @DisplayName("Log in with wrong password")
    public void logInWithWrongPassword() {
        CourierCredentials credentials = new CourierCredentials(courier.getLogin(), courier.getPassword() + "123");
        ValidatableResponse loginResponse = courierClient.login(credentials);
        int loginStatusCode = loginResponse.extract().statusCode();
        assertEquals(HttpStatus.SC_NOT_FOUND, loginStatusCode);

        String message = loginResponse.extract().path("message");
        assertEquals("Учетная запись не найдена", message);
    }

    @Test
    @DisplayName("Log in with wrong login")
    public void logInWithWrongLogin() {
        CourierCredentials credentials = new CourierCredentials(courier.getLogin() + "7548", courier.getPassword());
        ValidatableResponse loginResponse = courierClient.login(credentials);
        int loginStatusCode = loginResponse.extract().statusCode();
        assertEquals(HttpStatus.SC_NOT_FOUND, loginStatusCode);

        String message = loginResponse.extract().path("message");
        assertEquals("Учетная запись не найдена", message);
    }
    
    @Test
    @DisplayName("Login with not existed credentials")
    public void loginWithUnExistedCredentials() {
        CourierCredentials credentials = new CourierCredentials(courier.getLogin() + "7548", courier.getPassword() + "7345608347");
        ValidatableResponse loginResponse = courierClient.login(credentials);
        int loginStatusCode = loginResponse.extract().statusCode();
        assertEquals(HttpStatus.SC_NOT_FOUND, loginStatusCode);

        String message = loginResponse.extract().path("message");
        assertEquals("Учетная запись не найдена", message);
        
    }
}
