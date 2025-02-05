package ru.netology.test;


import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import static ru.netology.data.DataGenerator.*;
import static ru.netology.data.SQLHelper.*;


public class LoginTest {
    LoginInfo userInfo = getTestUser();

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
        var token = sendVer();
        int startCardBalance = getCardBalance();
        int transitBalance = getTestTransitInfo().getAmount() * 100;
        sendTransit(token);

        int expectedBalance = startCardBalance - transitBalance;
        int actualBalance = getCardBalance();

        Assertions.assertEquals(expectedBalance, actualBalance);
    }

}