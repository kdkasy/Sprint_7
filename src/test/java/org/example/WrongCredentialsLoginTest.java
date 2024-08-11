package org.example;

import io.qameta.allure.Description;
import io.qameta.allure.junit4.DisplayName;
import io.restassured.response.ValidatableResponse;
import org.apache.http.HttpStatus;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.Parameterized;

import static org.junit.Assert.*;

@RunWith(Parameterized.class)
public class WrongCredentialsLoginTest {
    private CourierClient courierClient;
    private Courier courier;
    private int courierId;

    private final String login;
    private final String password;

    public WrongCredentialsLoginTest(String login, String password) {
        this.login = login;
        this.password = password;
    }

    @Parameterized.Parameters
    public static Object[][] getLoginCredentials() {
        return new Object[][]{
                {null, "123460243"},
                {"3718301874", null},
                {"12454823", "374884124"}
        };
    }


    @Before
    public void setUp(){
        courierClient = new CourierClient();
        courier = CourierGenerator.createNewCourier();
        ValidatableResponse createCourierResponse = courierClient.create(courier);
        assertEquals(HttpStatus.SC_CREATED, createCourierResponse.extract().statusCode());
        ValidatableResponse loginResponse = courierClient.login(CourierCredentials.from(courier));
        assertEquals(HttpStatus.SC_OK, loginResponse.extract().statusCode());

        courierId = loginResponse.extract().path("id");
        assertNotEquals(0, courierId);
    }

    @After
    public void cleanUp(){
        ValidatableResponse deleteResponse = courierClient.delete(courierId);
        assertTrue(deleteResponse.extract().path("ok"));
    }

    @Test
    @DisplayName("Log in with wrong credentials")
    @Description("Check wrong password, login and both")
    public void logInWithWrongCredentials() {
        CourierCredentials credentials = new CourierCredentials(courier.getLogin() + login, courier.getPassword() + password);
        ValidatableResponse loginResponse = courierClient.login(credentials);
        int loginStatusCode = loginResponse.extract().statusCode();
        assertEquals(HttpStatus.SC_NOT_FOUND, loginStatusCode);

        String message = loginResponse.extract().path("message");
        assertEquals("Учетная запись не найдена", message);
    }
}

