package org.example;

import io.qameta.allure.junit4.DisplayName;
import io.restassured.response.ValidatableResponse;
import org.apache.http.HttpStatus;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.Parameterized;

import java.io.File;

import static org.junit.Assert.*;

@RunWith(Parameterized.class)
public class ColoursInOrderTest {
    private final String fileName;
    private int track;
    private OrderClient orderClient;

    public ColoursInOrderTest(String fileName) {
        this.fileName = fileName;
    }

    @Parameterized.Parameters
    public static Object[] getOrderData() {
        return new Object[]{
                "src/test/java/org/example/resources/OrderWithBlackColour.json",
                "src/test/java/org/example/resources/OrderWithGrayColour.json",
                "src/test/java/org/example/resources/OrderWithoutColour.json"
        };
    }

    @Before
    public void setUp() {
        orderClient = new OrderClient();
    }

    @After
    public void cleanUp() {
        orderClient.cancel(track);
    }

    @Test
    @DisplayName("Create order with different colours")
    public void createOrderWithDifferentColours() {
        File orderJson = new File(fileName);
        ValidatableResponse createOrderResponse = orderClient.create(orderJson);
        int statusCode = createOrderResponse.extract().statusCode();
        assertEquals(HttpStatus.SC_CREATED, statusCode);

        track = createOrderResponse.extract().path("track");
        assertNotEquals(0, track);
    }
}
