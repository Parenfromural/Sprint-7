package com.yandex.scooter.order;

import com.yandex.scooter.BaseTest;
import io.qameta.allure.Step;
import org.junit.Test;

import static io.restassured.RestAssured.given;
import static org.hamcrest.Matchers.*;

public class ListOrdersTest extends BaseTest {

    @Step("Запрос списка заказов")
    private void getOrders() {
        given()
                .when()
                .get("/orders")
                .then()
                .statusCode(200)
                .body("orders", not(empty()))
                .body("orders[0].id", notNullValue());
    }

    @Test
    public void getOrdersListReturnsOrders() {
        getOrders();
    }
}