package tech.erubin.annyeong_eat.telegramBot.handler;

import lombok.NoArgsConstructor;
import org.springframework.stereotype.Component;
import tech.erubin.annyeong_eat.telegramBot.abstractClass.AbstractHandler;

@Component
@NoArgsConstructor
public class CheckMessage extends AbstractHandler {

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
        resultText.append(checkLength(sourceText,1,200));
        return checkCorrectlyAddress(resultText);
    }

    private String checkIsNumber(String sourceText) {
        if (sourceText.matches(".*\\d+.*")){
             return errorNumber;
        }
        return "";
    }

    private String checkFormNameAndSurname(String sourceText) {
        if (sourceText.matches("([??-????-??????]+)[- ]?([??-????-??????]+)[- ]?([??-????-??????]+)")) {
            return "";
        }
        return errorFalseTextForm;
    }

    private String checkFormAddress(String sourceText) {
        if (sourceText.matches("[??-????-??0-9 ,./-]+")) {
            return "";
        }
        return errorFalseTextForm;
    }

    private String checkNoCorrectCharNameAndSurname(String sourceText) {
        if (sourceText.matches(".*[@#$%^&*+!(){};:<>?~`_|/,\"\\[\\]\\n\\t\\f\\r].*")) {
            return errorNoCorrectChar;
        }
        return "";
    }

    private String checkNoCorrectCharPhoneNumber(String sourceText) {
        if (sourceText.matches("[0-9+]+")) {
            return "";
        }
        return errorNoCorrectChar;
    }

    private String checkNoCorrectCharAddress(String sourceText) {
        if (sourceText.matches(".*[@#$%^&*+!(){};:<>?~`_|\"\\[\\]\\n\\t\\f\\r].*")) {
            return errorNoCorrectChar;
        }
        return "";
    }

    private String checkLength(String text, int minLength, int maxLength) {
        int lengthText = text.length();
        if (lengthText < minLength) {
            return String.format(errorLittleLength, minLength);
        }
        if (lengthText > maxLength){
            return String.format(errorBigLength, maxLength);
        }
        return "";
    }

    private String checkFormatPhoneNumber(String sourcePhoneNumber){
        if (sourcePhoneNumber.matches("(\\+79)\\d{9}") ||
            sourcePhoneNumber.matches("89\\d{9}")){
            return "";
        }
        return errorFormatPhoneNumber;
    }

    private String checkObsceneWord() {
        return "";
    }

    private String checkCorrectlyName(StringBuilder resultText){
        if (!resultText.toString().contains(errorTrigger)){
            return nameNoError;
        }
        else {
            return resultText.toString();
        }
    }

    private String checkCorrectlySurname(StringBuilder resultText){
        if (!resultText.toString().contains(errorTrigger)){
            return surnameNoError;
        }
        else {
            return resultText.toString();
        }
    }

    private String checkCorrectlyPhoneNumber(StringBuilder resultText){
        if (!resultText.toString().contains(errorTrigger)){
            return phoneNumberNoError;
        }
        else {
            return resultText.toString();
        }
    }

    private String checkCorrectlyAddress(StringBuilder resultText){
        if (!resultText.toString().contains(errorTrigger)){
            return addressNoError;
        }
        else {
            return resultText.toString();
        }
    }
}
