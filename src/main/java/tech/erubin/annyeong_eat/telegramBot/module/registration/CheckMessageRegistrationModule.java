package tech.erubin.annyeong_eat.telegramBot.module.registration;

import org.springframework.stereotype.Component;
import tech.erubin.annyeong_eat.telegramBot.module.CheckMessage;
import tech.erubin.annyeong_eat.telegramBot.messages.TextMessages;
import tech.erubin.annyeong_eat.telegramBot.service.entityServises.CafeServiceImpl;

@Component
public class CheckMessageRegistrationModule extends CheckMessage {
    public CheckMessageRegistrationModule(TextMessages textMessages, CafeServiceImpl cafeService) {
        super(textMessages, cafeService);
    }

    public String checkName(String sourceText) {
        StringBuilder resultText = createStringBuilder();
        resultText.append(checkIsNumber(sourceText));
        resultText.append(checkObsceneWord());
        resultText.append(checkLength(sourceText, 2, 20));
        resultText.append(checkNoCorrectCharNameAndSurname(sourceText));
        resultText.append(checkRussianChar(sourceText));
        return checkCorrectlyName(resultText);
    }

    public String checkSurname(String sourceText) {
        StringBuilder resultText = createStringBuilder();
        resultText.append(checkIsNumber(sourceText));
        resultText.append(checkObsceneWord());
        resultText.append(checkLength(sourceText, 2, 32));
        resultText.append(checkNoCorrectCharNameAndSurname(sourceText));
        resultText.append(checkRussianChar(sourceText));
        return checkCorrectlySurname(resultText);
    }

    public String checkPhoneNumber(String sourceText) {
        StringBuilder resultText = createStringBuilder();
        resultText.append(checkLength(sourceText, 12, 12));
        resultText.append(checkNoCorrectCharPhoneNumber(sourceText));
        resultText.append(checkFormatPhoneNumber(sourceText));
        return checkCorrectlyPhoneNumber(resultText);
    }

    public String checkCity(String sourceText) {
        StringBuilder resultText = createStringBuilder();
        resultText.append(checkNameCity(sourceText));
        return checkCorrectlyCity(resultText);
    }
}
