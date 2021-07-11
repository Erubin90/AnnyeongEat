package tech.erubin.annyeong_eat.telegramBot;

import org.junit.Assert;
import org.junit.jupiter.api.Test;

class CheckMessageTest {

    @Test
    void checkName() {
        CheckMessage checkMessage = new CheckMessage(new SettingMessage());
        String[] testNames = {
                "Магомед",
                "Магомед-Амин",
                "Магомед Амин",
                "Магомед-Ам ин",
                "Мaгомед",
                "М@г0мeд",
                "Магомедawewd2ws sdsd22 ssss sdfsdfs sdfs dsdfssdsfdsfsfsfwwe32323e2edsddefsdfgdfgdfdfg3dfsd sdfsdf sdf ",
                "М"
        };
        String[] actualise = {
                "ok",
                "ok",
                "ok",
                "NoRussianChars ",
                "NoRussianChars ",
                "Number NoCorrectChar NoRussianChars ",
                "Number BigLength 20 NoRussianChars ",
                "LittleLength 2 NoRussianChars "
        };

        for (int i = 0; i < testNames.length; i++) {
            String expected = checkMessage.checkName(testNames[i]);
            String actual = actualise[i];
            System.out.println(testNames[i]);
            Assert.assertEquals(expected, actual);
        }

    }

    @Test
    void checkSurname() {
        CheckMessage checkMessage = new CheckMessage(new SettingMessage());
        String[] testSurnames = {
                "Магомед",
                "Магомед-Амин",
                "Магомед Амин",
                "Магомед-Ам ин",
                "Мaгомед",
                "М@г0мeд",
                "Магомедawewd2ws sdsd22 ssss sdfsdfs sdfs dsdfssdsfdsfsfsfwwe32323e2edsddefsdfgdfgdfdfg3dfsd sdfsdf sdf ",
                "М"
        };
        String[] actualise = {
                "ok",
                "ok",
                "ok",
                "NoRussianChars ",
                "NoRussianChars ",
                "Number NoCorrectChar NoRussianChars ",
                "Number BigLength 32 NoRussianChars ",
                "LittleLength 2 NoRussianChars "
        };

        for (int i = 0; i < testSurnames.length; i++) {
            String expected = checkMessage.checkSurname(testSurnames[i]);
            String actual = actualise[i];
            System.out.println(testSurnames[i]);
            Assert.assertEquals(expected, actual);
        }
    }

    @Test
    void checkPhoneNumber() {
        CheckMessage checkMessage = new CheckMessage(new SettingMessage());
        String[] testPhoneNumbers = {
                "+79288352140",
                "+74288352140",
                "+742883521",
                "+54288352140",
                "+7428835214e2",
                "aefkfsdkIII#@DSDKALD",
        };
        String[] actualise = {
                "ok",
                "ok",
                "LittleLength 12 FormatPhoneNumber ",
                "FormatPhoneNumber ",
                "BigLength 12 NoCorrectChar FormatPhoneNumber ",
                "BigLength 12 NoCorrectChar FormatPhoneNumber ",
        };

        for (int i = 0; i < testPhoneNumbers.length; i++) {
            String expected = checkMessage.checkPhoneNumber(testPhoneNumbers[i]);
            String actual = actualise[i];
            System.out.println(testPhoneNumbers[i]);
            Assert.assertEquals(expected, actual);
        }
    }

    @Test
    void checkCity() {
    }

    @Test
    void checkIsNumber() {
    }

    @Test
    void checkRussianChar() {
    }

    @Test
    void checkNoCorrectCharNameAndSurname() {
    }

    @Test
    void checkNoCorrectCharPhoneNumber() {
    }

    @Test
    void checkLength() {
    }

    @Test
    void checkFormatPhoneNumber() {
    }

    @Test
    void checkObsceneWord() {
    }

    @Test
    void checkCorrectly() {
    }
}