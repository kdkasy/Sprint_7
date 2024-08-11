package org.example;

import io.qameta.allure.junit4.DisplayName;
import io.restassured.response.ValidatableResponse;
import org.apache.http.HttpStatus;
import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import java.io.File;
import java.util.List;
import java.util.Map;

import static org.hamcrest.Matchers.greaterThanOrEqualTo;
import static org.junit.Assert.*;

public class ListOfOrderTest {
    private OrderClient orderClient;
    private static final String[] fileNames = new String[] {
            "src/test/java/org/example/resources/OrderWithBlackColour.json",
            "src/test/java/org/example/resources/OrderWithGrayColour.json",
            "src/test/java/org/example/resources/OrderWithoutColour.json"
    };
    private int[] tracks = new int[fileNames.length];

    @Before
    public void setUp() {
        orderClient = new OrderClient();
        for (int i = 0; i < fileNames.length; i++) {
            File orderJson = new File(fileNames[i]);
            ValidatableResponse createOrderResponse =  orderClient.create(orderJson);
            tracks[i] = createOrderResponse.extract().path("track");
        }
    }

    @After
    public void cleanUp() {
        for(int track : tracks) {
            orderClient.cancel(track);
        }
    }

    @Test
    @DisplayName("Number of orders not empty")
    public void ordersAreNotEmpty () {
        ValidatableResponse getOrdersResponse = orderClient.getAllOrders();
        int statusCode = getOrdersResponse.extract().statusCode();
        assertEquals(HttpStatus.SC_OK, statusCode);
        List<Map<String, Object>> orders = getOrdersResponse.extract().path("orders");
        Assert.assertThat(orders.size(), greaterThanOrEqualTo(3));
    }
}
