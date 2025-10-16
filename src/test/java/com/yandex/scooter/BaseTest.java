package com.yandex.scooter;

import io.qameta.allure.restassured.AllureRestAssured;
import io.restassured.RestAssured;
import io.restassured.config.HttpClientConfig;
import io.restassured.config.RestAssuredConfig;
import io.restassured.filter.log.LogDetail;
import io.restassured.filter.log.RequestLoggingFilter;
import io.restassured.filter.log.ResponseLoggingFilter;
import org.apache.http.params.CoreConnectionPNames;
import org.junit.BeforeClass;

public class BaseTest {
    @BeforeClass
    public static void setup() {
        RestAssured.baseURI = "https://qa-scooter.praktikum-services.ru/api/v1";
        RestAssured.filters(new RequestLoggingFilter(LogDetail.ALL), new ResponseLoggingFilter(LogDetail.ALL),new AllureRestAssured());
        RestAssured.config = RestAssuredConfig.config()
                .httpClient(HttpClientConfig.httpClientConfig()
                        .setParam(CoreConnectionPNames.CONNECTION_TIMEOUT, 10000)
                        .setParam(CoreConnectionPNames.SO_TIMEOUT, 10000)
                );
    }
}