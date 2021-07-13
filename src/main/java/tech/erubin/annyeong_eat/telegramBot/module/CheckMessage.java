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

    public StringBuilder createStringBuilder(){
        return new StringBuilder().append(message.getRegularError());
    }

    public String checkIsNumber(String sourceText) {
        if (sourceText.matches(".*\\d+.*")){
             return message.getErrorNumber();
        }
        return "";
    }

    public String checkRussianChar(String sourceText) {
        if (sourceText.matches("([А-Яа-яёЁ]+)[- ]{0,1}([А-Яа-яёЁ]+)")) {
            return "";
        }
        else {
            return message.getErrorNoRussianChars();
        }
    }

    public String checkNoCorrectCharNameAndSurname(String sourceText) {
        if (sourceText.matches(".*[@#$%^&*+!(){};:<>?~`_|/\"\\[\\]\\n\\t\\f\\r].*")) {
            return message.getErrorNoCorrectChar();
        }
        else {
            return "";
        }
    }

    public String checkNoCorrectCharPhoneNumber(String sourceText) {
        if (sourceText.matches("[0-9+]+")) {
            return "";
        }
        else {
            return message.getErrorNoCorrectChar();
        }

    }

    public String checkLength(String text, int minLength, int maxLength) {
        int lengthText = text.length();
        if (lengthText < minLength) {
            return String.format(message.getMessageErrorLittleLength(), minLength);
        }
        if (lengthText > maxLength){
            return String.format(message.getMessageErrorBigLength(), maxLength);
        }
        return "";
    }

    public String checkFormatPhoneNumber(String sourcePhoneNumber){
        if (sourcePhoneNumber.matches("(\\+7)\\d{10}")){
            return "";
        }
        else {
            return message.getMessageErrorFormatPhoneNumber();
        }
    }

    public String checkNameCity(String sourceCity) {
        Set<String> correctCity = cafeService.getAllCity();
        if (correctCity.contains(sourceCity)) {
            return "";
        }
        else {
            return message.getMessageErrorNameCity();
        }
    }

    public String checkObsceneWord() {
        return "";
    }

    public String checkCorrectlyName(StringBuilder resultText){
        if (resultText.toString().equals(message.getRegularError())){
            return message.getNameNoError();
        }
        else {
            return resultText.toString();
        }
    }

    public String checkCorrectlySurname(StringBuilder resultText){
        if (resultText.toString().equals(message.getRegularError())){
            return message.getSurnameNoError();
        }
        else {
            return resultText.toString();
        }
    }

    public String checkCorrectlyPhoneNumber(StringBuilder resultText){
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
}
