package com.yandex.scooter.order;

import com.yandex.scooter.BaseTest;
import io.qameta.allure.Step;
import org.junit.Test;

import static io.restassured.RestAssured.given;
import static org.hamcrest.Matchers.*;

public class CreateOrderTest extends BaseTest {

    @Step("Формируем тело заказа с цветами: {colors}")
    private String buildOrderJson(String[] colors) {
        StringBuilder colorJson = new StringBuilder();
        if (colors != null && colors.length > 0) {
            colorJson.append(",\"color\":[");
            for (int i = 0; i < colors.length; i++) {
                colorJson.append("\"").append(colors[i]).append("\"");
                if (i < colors.length - 1) colorJson.append(",");
            }
            colorJson.append("]");
        }
        return "{"
                + "\"firstName\":\"Naruto\","
                + "\"lastName\":\"Uchiha\","
                + "\"address\":\"Konoha, 142 apt.\","
                + "\"metroStation\":\"4\","
                + "\"phone\":\"+7 800 355 35 35\","
                + "\"rentTime\":5,"
                + "\"deliveryDate\":\"2020-06-06\","
                + "\"comment\":\"Saske, come back to Konoha\""
                + colorJson.toString()
                + "}";
    }

    @Step("Отправляем запрос на создание заказа")
    private void sendCreateOrderRequest(String orderJson) {
        given()
                .header("Content-type", "application/json")
                .body(orderJson)
                .when()
                .post("/orders")
                .then()
                .statusCode(201)
                .body("track", notNullValue());
    }

    @Test
    public void createOrderWithOneColor() {
        String[] colors = {"BLACK"};
        String orderJson = buildOrderJson(colors);
        sendCreateOrderRequest(orderJson);
    }

    @Test
    public void createOrderWithTwoColors() {
        String[] colors = {"BLACK", "GREY"};
        String orderJson = buildOrderJson(colors);
        sendCreateOrderRequest(orderJson);
    }

    @Test
    public void createOrderWithoutColor() {
        String orderJson = buildOrderJson(null);
        sendCreateOrderRequest(orderJson);
    }
}