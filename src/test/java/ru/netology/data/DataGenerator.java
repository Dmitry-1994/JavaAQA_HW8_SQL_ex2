package ru.netology.data;

import io.restassured.RestAssured;
import io.restassured.builder.RequestSpecBuilder;
import io.restassured.filter.log.LogDetail;
import io.restassured.http.ContentType;
import io.restassured.response.Response;
import io.restassured.specification.RequestSpecification;
import lombok.Data;
import lombok.Value;

import static io.restassured.RestAssured.given;

public class DataGenerator {
    private DataGenerator() {
    }

    public static LoginInfo getTestUser() {
        return new LoginInfo("vasya", "qwerty123");
    }

    public static VerInfo getTestVer() {
        var user = getTestUser();
        String login = user.getLogin();
        String code = SQLHelper.getVerCode();
        return new VerInfo(login, code);
    }

    public static TransitInfo getTestTransitInfo() {
        return new TransitInfo("5559 0000 0000 0002", "5559 0000 0000 0008", 5000);
    }


    private static final RequestSpecification requestSpec = new RequestSpecBuilder()
            .setBaseUri("http://localhost")
            .setPort(9999)
            .setAccept(ContentType.JSON)
            .setContentType(ContentType.JSON)
            .log(LogDetail.ALL)
            .build();

    public static void sendAuth(LoginInfo user) {
        given()
                .spec(requestSpec)
                .body(user)
                .when()
                .post("/api/auth")
                .then()
                .statusCode(200);
    }

    public static TokenCode sendVer() {
        var ver = getTestVer();
        RequestSpecification request = RestAssured.given()
                .spec(requestSpec)
                .body(ver);
        Response response = request.when()
                .post("/api/auth/verification");
        response.then()
                .statusCode(200);

        TokenCode tokenCode = new TokenCode();
        tokenCode.setToken(response.jsonPath().getString("token"));

        return tokenCode;

    }

    public static void sendTransit(TokenCode tokenCode) {
        var transitInfo = getTestTransitInfo();
        RequestSpecification request = RestAssured.given()
                .spec(requestSpec)
                .header("Authorization", "Bearer " + tokenCode.getToken())
                .body(transitInfo);
        Response response = request.when()
                .post("/api/transfer");
        response.then()
                .statusCode(200);

    }

    @Value
    public static class LoginInfo {
        String login;
        String password;
    }

    @Value
    public static class VerInfo {
        String login;
        String code;
    }

    @Data
    public static class TokenCode {
        String token;
    }

    @Value
    public static class TransitInfo {
        String from;
        String to;
        int amount;
    }
}