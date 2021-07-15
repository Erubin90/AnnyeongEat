package tech.erubin.annyeong_eat.telegramBot.module;

import lombok.AllArgsConstructor;
import org.springframework.stereotype.Component;
import tech.erubin.annyeong_eat.telegramBot.messages.TextMessages;
import tech.erubin.annyeong_eat.telegramBot.service.entityServises.CafeServiceImpl;

import java.util.Set;

@Component
@AllArgsConstructor
public class CheckMessage{
    TextMessages message;
    CafeServiceImpl cafeService;

    //    message.error.obsceneWord = message.error.obsceneWord
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

    public String checkAddress(String sourceText) {
        StringBuilder resultText = createStringBuilder();
        resultText.append(checkRussianCharAddress(sourceText));
        resultText.append(checkNoCorrectCharAddress(sourceText));
        return checkCorrectlyAddress(resultText);
    }

    private StringBuilder createStringBuilder(){
        return new StringBuilder().append(message.getRegularError());
    }

    private String checkIsNumber(String sourceText) {
        if (sourceText.matches(".*\\d+.*")){
             return message.getErrorNumber();
        }
        return "";
    }

    private String checkRussianChar(String sourceText) {
        if (sourceText.matches("([А-Яа-яёЁ]+)[ -]{0,1}([А-Яа-яёЁ]+)")) {
            return "";
        }
        else {
            return message.getErrorNoRussianChars();
        }
    }

    private String checkRussianCharAddress(String sourceText) {
        if (sourceText.matches("[А-Яа-яёЁ 0-9.,-]+")) {
            return "";
        }
        else {
            return message.getErrorNoRussianChars();
        }
    }

    private String checkNoCorrectCharNameAndSurname(String sourceText) {
        if (sourceText.matches(".*[@#$%^&*+!(){};:<>?~`_|/,\"\\[\\]\\n\\t\\f\\r]?[- ]{2,}.*")) {
            return message.getErrorNoCorrectChar();
        }
        else {
            return "";
        }
    }

    private String checkNoCorrectCharPhoneNumber(String sourceText) {
        if (sourceText.matches("[0-9+]+")) {
            return "";
        }
        else {
            return message.getErrorNoCorrectChar();
        }

    }

    private String checkNoCorrectCharAddress(String sourceText) {
        if (sourceText.matches(".*[@#$%^&*+!(){};:<>?~`_|/\"\\[\\]\\n\\t\\f\\r].*")) {
            return message.getErrorNoCorrectChar();
        }
        else {
            return "";
        }
    }

    private String checkLength(String text, int minLength, int maxLength) {
        int lengthText = text.length();
        if (lengthText < minLength) {
            return String.format(message.getErrorLittleLength(), minLength);
        }
        if (lengthText > maxLength){
            return String.format(message.getErrorBigLength(), maxLength);
        }
        return "";
    }

    private String checkFormatPhoneNumber(String sourcePhoneNumber){
        if (sourcePhoneNumber.matches("(\\+79)\\d{9}") ||
            sourcePhoneNumber.matches("89\\d{9}")){
            return "";
        }
        else {
            return message.getErrorFormatPhoneNumber();
        }
    }

    private String checkNameCity(String sourceCity) {
        Set<String> correctCity = cafeService.getAllCity();
        if (correctCity.contains(sourceCity)) {
            return "";
        }
        else {
            return message.getErrorNameCity();
        }
    }

    private String checkObsceneWord() {
        return "";
    }

    private String checkCorrectlyName(StringBuilder resultText){
        if (resultText.toString().equals(message.getRegularError())){
            return message.getNameNoError();
        }
        else {
            return resultText.toString();
        }
    }

    private String checkCorrectlySurname(StringBuilder resultText){
        if (resultText.toString().equals(message.getRegularError())){
            return message.getSurnameNoError();
        }
        else {
            return resultText.toString();
        }
    }

    private String checkCorrectlyPhoneNumber(StringBuilder resultText){
        if (resultText.toString().equals(message.getRegularError())){
            return message.getPhoneNumberNoError();
        }
        else {
            return resultText.toString();
        }
    }

    public String checkCorrectlyCity(StringBuilder resultText){
        if (resultText.toString().equals(message.getRegularError())){
            return message.getCityNoError();
        }
        else {
            return resultText.toString();
        }
    }

    public String checkCorrectlyAddress(StringBuilder resultText){
        if (resultText.toString().equals(message.getRegularError())){
            return message.getCityNoError();
        }
        else {
            return resultText.toString();
        }
    }
}
