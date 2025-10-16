package com.yandex.scooter.courier;

import com.yandex.scooter.BaseTest;
import io.qameta.allure.Step;
import org.junit.Test;

import static io.restassured.RestAssured.given;
import static org.hamcrest.Matchers.*;

public class LoginCourierTest extends BaseTest {

    private String login = "login" + System.currentTimeMillis();
    private String password = "pass123";
    private String firstName = "TestUser";

    @Step("Создать курьера с login={login}")
    private void createCourier() {
        given()
                .header("Content-type", "application/json")
                .body("{\"login\":\"" + login + "\",\"password\":\"" + password + "\",\"firstName\":\"" + firstName + "\"}")
                .when()
                .post("/courier")
                .then()
                .statusCode(201)
                .body("ok", equalTo(true));
    }

    @Step("Авторизоваться курьером login={login}")
    private void loginCourier(String login, String password) {
        given()
                .header("Content-type", "application/json")
                .body("{\"login\":\"" + login + "\",\"password\":\"" + password + "\"}")
                .when()
                .post("/courier/login")
                .then()
                .statusCode(200)
                .body("id", notNullValue());
    }


    @Step("Авторизация с неверным паролем")
    private void loginWithWrongPassword(String login) {
        given()
                .header("Content-type", "application/json")
                .body("{\"login\":\"" + login + "\",\"password\":\"wrongPass\"}")
                .when()
                .post("/courier/login")
                .then()
                .statusCode(404)
                .body("message", containsString("Учетная запись не найдена"));
    }

    @Step("Авторизация несуществующим пользователем")
    private void loginNonExistentUser() {
        given()
                .header("Content-type", "application/json")
                .body("{\"login\":\"nonexistent\",\"password\":\"somepass\"}")
                .when()
                .post("/courier/login")
                .then()
                .statusCode(404)
                .body("message", containsString("Учетная запись не найдена"));
    }

    @Test
    public void loginCourierSuccessfully() {
        createCourier();
        loginCourier(login, password);
    }


    @Test
    public void loginWithWrongPasswordReturnsError() {
        createCourier();
        loginWithWrongPassword(login);
    }

    @Test
    public void loginNonExistentUserReturnsError() {
        loginNonExistentUser();
    }
}