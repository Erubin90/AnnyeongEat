package tech.erubin.annyeong_eat.telegramBot.module.handler;

import lombok.AllArgsConstructor;
import org.springframework.stereotype.Component;
import tech.erubin.annyeong_eat.telegramBot.module.order.OrderTextMessage;
import tech.erubin.annyeong_eat.telegramBot.module.registration.RegistrationTextMessage;

@Component
@AllArgsConstructor
public class CheckMessage{
    private final RegistrationTextMessage registrationMessage;
    private final OrderTextMessage orderMessage;

    //    message.error.obsceneWord = message.error.obsceneWord
    public String checkName(String sourceText) {
        StringBuilder resultText = new StringBuilder();
        resultText.append(checkIsNumber(sourceText));
        resultText.append(checkObsceneWord());
        resultText.append(checkLength(sourceText, 2, 20));
        resultText.append(checkNoCorrectCharNameAndSurname(sourceText));
        resultText.append(checkFormNameAndSurname(sourceText));
        return checkCorrectlyName(resultText);
    }

    public String checkSurname(String sourceText) {
        StringBuilder resultText = new StringBuilder();
        resultText.append(checkIsNumber(sourceText));
        resultText.append(checkObsceneWord());
        resultText.append(checkLength(sourceText, 2, 32));
        resultText.append(checkNoCorrectCharNameAndSurname(sourceText));
        resultText.append(checkFormNameAndSurname(sourceText));
        return checkCorrectlySurname(resultText);
    }

    public String checkPhoneNumber(String sourceText) {
        StringBuilder resultText = new StringBuilder();
        resultText.append(checkLength(sourceText, 11, 12));
        resultText.append(checkNoCorrectCharPhoneNumber(sourceText));
        resultText.append(checkFormatPhoneNumber(sourceText));
        return checkCorrectlyPhoneNumber(resultText);
    }

    public String checkAddress(String sourceText) {
        StringBuilder resultText = new StringBuilder();
        resultText.append(checkFormAddress(sourceText));
        resultText.append(checkNoCorrectCharAddress(sourceText));
        resultText.append(checkLength(sourceText,5,100));
        return checkCorrectlyAddress(resultText);
    }

    private String checkIsNumber(String sourceText) {
        if (sourceText.matches(".*\\d+.*")){
             return registrationMessage.getErrorNumber();
        }
        return "";
    }

    private String checkFormNameAndSurname(String sourceText) {
        if (sourceText.matches("([А-Яа-яёЁ]+)[- ]?([А-Яа-яёЁ]+)[- ]?([А-Яа-яёЁ]+)")) {
            return "";
        }
        return registrationMessage.getErrorFalseTextForm();
    }

    private String checkFormAddress(String sourceText) {
        if (sourceText.matches("[А-Яа-я0-9 ,./-]+")) {
            return "";
        }
        return registrationMessage.getErrorFalseTextForm();
    }

    private String checkNoCorrectCharNameAndSurname(String sourceText) {
        if (sourceText.matches(".*[@#$%^&*+!(){};:<>?~`_|/,\"\\[\\]\\n\\t\\f\\r].*")) {
            return registrationMessage.getErrorNoCorrectChar();
        }
        return "";
    }

    private String checkNoCorrectCharPhoneNumber(String sourceText) {
        if (sourceText.matches("[0-9+]+")) {
            return "";
        }
        return registrationMessage.getErrorNoCorrectChar();
    }

    private String checkNoCorrectCharAddress(String sourceText) {
        if (sourceText.matches(".*[@#$%^&*+!(){};:<>?~`_|\"\\[\\]\\n\\t\\f\\r].*")) {
            return registrationMessage.getErrorNoCorrectChar();
        }
        return "";
    }

    private String checkLength(String text, int minLength, int maxLength) {
        int lengthText = text.length();
        if (lengthText < minLength) {
            return String.format(registrationMessage.getErrorLittleLength(), minLength);
        }
        if (lengthText > maxLength){
            return String.format(registrationMessage.getErrorBigLength(), maxLength);
        }
        return "";
    }

    private String checkFormatPhoneNumber(String sourcePhoneNumber){
        if (sourcePhoneNumber.matches("(\\+79)\\d{9}") ||
            sourcePhoneNumber.matches("89\\d{9}")){
            return "";
        }
        return registrationMessage.getErrorFormatPhoneNumber();
    }

    private String checkObsceneWord() {
        return "";
    }

    private String checkCorrectlyName(StringBuilder resultText){
        if (!resultText.toString().contains(registrationMessage.getErrorTrigger())){
            return registrationMessage.getNameNoError();
        }
        else {
            return resultText.toString();
        }
    }

    private String checkCorrectlySurname(StringBuilder resultText){
        if (!resultText.toString().contains(registrationMessage.getErrorTrigger())){
            return registrationMessage.getSurnameNoError();
        }
        else {
            return resultText.toString();
        }
    }

    private String checkCorrectlyPhoneNumber(StringBuilder resultText){
        if (!resultText.toString().contains(registrationMessage.getErrorTrigger())){
            return registrationMessage.getPhoneNumberNoError();
        }
        else {
            return resultText.toString();
        }
    }

    private String checkCorrectlyAddress(StringBuilder resultText){
        if (!resultText.toString().contains(orderMessage.getErrorTrigger())){
            return orderMessage.getAddressNoError();
        }
        else {
            return resultText.toString();
        }
    }
}
