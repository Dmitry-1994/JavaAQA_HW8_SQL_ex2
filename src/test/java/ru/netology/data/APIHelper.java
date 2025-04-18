package ru.netology.data;

import io.restassured.RestAssured;
import io.restassured.builder.RequestSpecBuilder;
import io.restassured.filter.log.LogDetail;
import io.restassured.http.ContentType;
import io.restassured.response.Response;
import io.restassured.specification.RequestSpecification;

import static io.restassured.RestAssured.given;
import static ru.netology.data.DataGenerator.getTestVer;

public class APIHelper {
    APIHelper() {
    }

    private static final RequestSpecification requestSpec = new RequestSpecBuilder()
            .setBaseUri("http://localhost")
            .setPort(9999)
            .setAccept(ContentType.JSON)
            .setContentType(ContentType.JSON)
            .log(LogDetail.ALL)
            .build();

    public static void sendAuth(DataGenerator.LoginInfo user) {
        given()
                .spec(requestSpec)
                .body(user)
                .when()
                .post("/api/auth")
                .then()
                .statusCode(200);
    }

    public static DataGenerator.TokenCode sendVer(DataGenerator.LoginInfo user) {
        var ver = getTestVer(user);
        RequestSpecification request = RestAssured.given()
                .spec(requestSpec)
                .body(ver);
        Response response = request.when()
                .post("/api/auth/verification");
        String token = response.then()
                .statusCode(200)
                .extract()
                .path("token");


        DataGenerator.TokenCode tokenCode = new DataGenerator.TokenCode();
        tokenCode.setToken(token);

        return tokenCode;

    }

    public static void sendTransit(DataGenerator.TransitInfo transitInfo, DataGenerator.TokenCode tokenCode) {
        given()
                .spec(requestSpec)
                .header("Authorization", "Bearer " + tokenCode.getToken())
                .body(transitInfo)
                .when()
                .post("/api/transfer")
                .then()
                .statusCode(200);

    }

    public static DataGenerator.CardInfo[] getCardInfoJson(DataGenerator.TokenCode tokenCode) {
        RequestSpecification request = given()
                .spec(requestSpec)
                .header("Authorization", "Bearer " + tokenCode.getToken());
        Response response = request.when()
                .get("/api/cards");
        DataGenerator.CardInfo cardInfo[] = response.then()
                .statusCode(200)
                        .extract()
                .body().as(DataGenerator.CardInfo[].class);

        return cardInfo;
    }
}
