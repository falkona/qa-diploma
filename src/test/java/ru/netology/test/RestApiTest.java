package ru.netology.test;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static io.restassured.RestAssured.given;
import static org.hamcrest.Matchers.equalTo;

public class RestApiTest {
    @DisplayName("13. Проверка покупки с несуществующей карты - REST")
    @Test
    public void shouldBeErrorCardDoesNotExistRest() {
        String request = "{\n" +
                "           \"number\":\"4444 4444 4444 4443\",\n" +
                "           \"year\":\"22\",\n" +
                "           \"month\":\"08\",\n" +
                "           \"holder\":\"Нос Дарья\",\n" +
                "           \"cvc\":\"999\"}";

        given().
                header("Content-Type", "application/json").
                body(request).
                when().
                post("http://localhost:8080/api/v1/credit").
                then().
                statusCode(500).
                body("message", equalTo("400 Bad Request"));
    }
}
