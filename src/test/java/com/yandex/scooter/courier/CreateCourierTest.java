package com.yandex.scooter.courier;

import com.yandex.scooter.BaseTest;
import io.qameta.allure.Step;
import io.restassured.response.Response;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import static io.restassured.RestAssured.given;
import static org.hamcrest.Matchers.*;

public class CreateCourierTest extends BaseTest {

    private String courierLogin = "ninja" + System.currentTimeMillis();
    private String courierPassword = "1234";
    private int courierId;

    @Before
    public void createCourierBeforeTest() {
        courierId = createCourier(courierLogin, courierPassword, "Saske");
    }

    @After
    public void deleteCourierAfterTest() {
        if (courierId != 0) {
            deleteCourier(courierId);
        }
    }

    @Test
    public void createCourierSuccessfully() {
        String login = "newLogin" + System.currentTimeMillis();
        String password = "pass123";
        String firstName = "John";

        given()
                .header("Content-type", "application/json")
                .body("{\"login\":\"" + login + "\",\"password\":\"" + password + "\",\"firstName\":\"" + firstName + "\"}")
                .when()
                .post("/courier")
                .then()
                .statusCode(201)
                .body("ok", equalTo(true));
    }

    @Test
    public void createDuplicateCourierReturnsError() {
        given()
                .header("Content-type", "application/json")
                .body("{\"login\":\""+ courierLogin +"\",\"password\":\""+ courierPassword +"\",\"firstName\":\"Saske\"}")
                .when()
                .post("/courier")
                .then()
                .statusCode(409)
                .body("message", containsString("Этот логин уже используется"));
    }

    @Step("Создать курьера")
    private int createCourier(String login, String password, String firstName) {
        Response response = given()
                .header("Content-type", "application/json")
                .body("{\"login\":\""+login+"\",\"password\":\""+password+"\",\"firstName\":\""+firstName+"\"}")
                .when()
                .post("/courier")
                .then()
                .statusCode(201)
                .extract()
                .response();
        Response loginResponse = given()
                .header("Content-type", "application/json")
                .body("{\"login\":\""+login+"\",\"password\":\""+password+"\"}")
                .when()
                .post("/courier/login")
                .then()
                .statusCode(200)
                .extract()
                .response();

        return loginResponse.path("id");
    }

    @Step("Удалить курьера")
    private void deleteCourier(int id) {
        given()
                .when()
                .delete("/courier/" + id)
                .then()
                .statusCode(200)
                .body("ok", equalTo(true));
    }
}