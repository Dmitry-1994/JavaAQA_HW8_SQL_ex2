package ru.netology.test;


import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import static ru.netology.data.APIHelper.*;
import static ru.netology.data.DataGenerator.*;
import static ru.netology.data.SQLHelper.*;


public class LoginTest {
    LoginInfo userInfo = getTestUser("vasya", "qwerty123");

    String cardFirst = "5559 0000 0000 0001";
    String cardSecond = "5559 0000 0000 0002";
    String cardTo = "5559 0000 0000 0008";

    @AfterEach
    void clear() {
        clearAuthCodes();
    }

    @AfterAll
    static void clearAll() {
        clearDataBase();
    }

    @Test
    void successLoginWithTestData() {
        sendAuth(userInfo);

        var token = sendVer(userInfo);

        int startBalanceCardFirst = getCrdBalanceData(cardFirst, token);
        int startBalanceCardSecond = getCrdBalanceData(cardFirst, token);

        int amountTransit = getRandomTransitBalance(startBalanceCardFirst);

        TransitInfo transitInfo = getTestTransitInfo(cardFirst, cardSecond, amountTransit);
        int transitBalance = transitInfo.getAmount();

        sendTransit(transitInfo, token);

        int expectedBalanceCardFirst = startBalanceCardFirst - transitBalance;
        int expectedBalanceCardSecond = startBalanceCardSecond + transitBalance;

        int finishBalanceCardFirst = getCrdBalanceData(cardFirst, token);
        int finishBalanceCardSecond = getCrdBalanceData(cardSecond, token);

        Assertions.assertEquals(expectedBalanceCardFirst, finishBalanceCardFirst);
        Assertions.assertEquals(expectedBalanceCardSecond, finishBalanceCardSecond);
    }

}