package ru.netology.data;

import com.github.javafaker.Faker;
import io.restassured.builder.RequestSpecBuilder;
import io.restassured.filter.log.LogDetail;
import io.restassured.http.ContentType;
import io.restassured.specification.RequestSpecification;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.Value;

import java.util.Locale;

import static io.restassured.RestAssured.given;

public class DataGenerator {
    private DataGenerator() {
    }

    private static final Faker faker = new Faker(new Locale("en"));

    private static String getRandomLogin() {
        return faker.name().username();
    }

    private static String getRandomPass() {
        return faker.internet().password();
    }

    public static LoginInfo getRandomUser() {
        return new LoginInfo(getRandomLogin(), getRandomPass());
    }

    public static LoginInfo getTestUser() {
        return new LoginInfo("vasya", "qwerty123");
    }

    public static CardsInfo getTestCardsFrom() {
        String balance = String.valueOf((int) Math.random() * 1_000_001);
        return new CardsInfo("5559 0000 0000 0002", balance);
    }

    public static VerInfo getTestVer() {
        var user = getTestUser();
        String login = user.getLogin();
        String code = SQLHelper.getVerCode();
        return new VerInfo(login, code);
    }


    public static VerificationCode getRandomVerCode() {
        return new VerificationCode(faker.numerify("######"));
    }

    private static final RequestSpecification requestSpec = new RequestSpecBuilder()
            .setBaseUri("http://localhost")
            .setPort(9999)
            .setAccept(ContentType.JSON)
            .setContentType(ContentType.JSON)
            .log(LogDetail.ALL)
            .build();

    public static void sendRequest() {
        var user = getTestUser();
        given()
                .spec(requestSpec)
                .body(user)
                .when()
                .post("/api/auth")
                .then()
                .statusCode(200);
    }

    public static void sendVer() {
        var ver = getTestVer();
        String token = "token";
        given()
                .spec(requestSpec)
                .body(ver)
                .when()
                    .post("/api/auth/verification")
                .then()
                    .statusCode(200)
                .extract()
                    .path(token);

    }

    @Value
    public static class LoginInfo {
        String login;
        String password;
    }

    @Value
    public static class CardsInfo {
        String number;
        String balanceInKopecks;
    }

    @Value
    public static class VerInfo {
        String login;
        String code;
    }

    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    public static class VerificationCode {
        String code;
    }
}