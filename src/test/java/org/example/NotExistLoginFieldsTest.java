package org.example;

import io.qameta.allure.Description;
import io.qameta.allure.junit4.DisplayName;
import io.restassured.response.ValidatableResponse;
import org.apache.http.HttpStatus;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.Parameterized;

import static org.junit.Assert.assertEquals;

@RunWith(Parameterized.class)
public class NotExistLoginFieldsTest {
    private final String login;
    private final String password;

    public NotExistLoginFieldsTest(String login, String password) {
        this.login = login;
        this.password = password;
    }

    @Parameterized.Parameters
    public static Object[][] getLoginCredentials() {
        return new Object[][]{
                {null, "123"},
                {"", "123"},
                {"123", null},
                {"123", ""}
        };
    }

    @Test
    @DisplayName("Log in with empty credentials fields")
    @Description("Check \"\" and null fields")
    public void logInWithWrongCredentials() {
        CourierClient courierClient = new CourierClient();
        CourierCredentials credentials = new CourierCredentials(login, password);
        ValidatableResponse loginResponse = courierClient.login(credentials);
        int loginStatusCode = loginResponse.extract().statusCode();
        assertEquals(HttpStatus.SC_BAD_REQUEST, loginStatusCode);

        String message = loginResponse.extract().path("message");
        assertEquals("Недостаточно данных для входа", message);
    }
}
