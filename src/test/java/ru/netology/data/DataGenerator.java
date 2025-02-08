package ru.netology.data;

import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonParser;
import lombok.Data;
import lombok.Value;

import static ru.netology.data.APIHelper.getCardInfoJson;

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

    public static int getCrdBalanceData(String cardNumber, TokenCode tokenCode) {
        int cardBalance = 0;
        String cardNumberShortExp = cardNumber.substring(cardNumber.length() - 4);
        String jsonResponse = getCardInfoJson(tokenCode);
        JsonArray jsonArray = JsonParser.parseString(jsonResponse).getAsJsonArray();

        for (JsonElement element : jsonArray) {
            String number = element.getAsJsonObject().get("number").getAsString();
            String numberShortAct = number.substring(number.length() - 4);

            if (cardNumberShortExp.equals(numberShortAct)) {
                cardBalance =  element.getAsJsonObject().get("balance").getAsInt();
            }
        }

        return cardBalance;

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

    //@Value
    //public static class CardInfo {
    //    String id;
    //    String number;
    //    int balance;
    //}
}