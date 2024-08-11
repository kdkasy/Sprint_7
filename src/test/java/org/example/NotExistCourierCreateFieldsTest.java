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
public class NotExistCourierCreateFieldsTest {
    private final String login;
    private final String password;
    private final String firstName;

    public NotExistCourierCreateFieldsTest(String login, String password, String firstName) {
        this.login = login;
        this.password = password;
        this.firstName = firstName;
    }

    @Parameterized.Parameters
    public static Object[][] getCredentials() {
        return new Object[][]{
                {null, "123", "123"},
                {"", "123", "123"},
                {"123", null, "123"},
                {"123", "", "123"}
        };
    }

    @Test
    @DisplayName("Create courier with empty credentials fields")
    @Description("Check \"\" and null fields")
    public void emptyCredentialsTest() {
        CourierClient courierClient = new CourierClient();
        Courier courier = new Courier(login, password, firstName);

        ValidatableResponse createResponse = courierClient.create(courier);
        int createStatusCode = createResponse.extract().statusCode();
        assertEquals(HttpStatus.SC_BAD_REQUEST, createStatusCode);

        String errorMessage = createResponse.extract().path("message");
        assertEquals("Недостаточно данных для создания учетной записи", errorMessage);
    }

}
