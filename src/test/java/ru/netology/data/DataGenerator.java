package ru.netology.data;

import com.google.gson.JsonArray;
import io.restassured.response.Response;
import lombok.Data;
import lombok.Value;

public class DataGenerator {
    private DataGenerator() {
    }

    public static LoginInfo getTestUser(String login, String password) {
        return new LoginInfo(login, password);
    }

    public static VerInfo getTestVer(LoginInfo user) {
        String login = user.getLogin();
        String code = SQLHelper.getVerCode();
        return new VerInfo(login, code);
    }

    public static TransitInfo getTestTransitInfo(String cardFrom, String cardTo, int amount) {
        return new TransitInfo(cardFrom, cardTo, amount);
    }

    //public static int getCrdInfoByJsonToData(String cardNumber, Response response) {
    //    String jsonResponse = response.path("balance", )
    //    JsonArray jsonArray = ;
    //}

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

    //@Value
    //public static class CardInfo {
    //    String id;
    //    String number;
    //    int balance;
    //}
}