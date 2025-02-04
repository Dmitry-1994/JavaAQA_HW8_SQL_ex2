package ru.netology.test;


import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Test;
import static ru.netology.data.DataGenerator.*;
import static ru.netology.data.SQLHelper.*;


public class LoginTest {

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
        sendRequest();
        sendVer();
    }

}