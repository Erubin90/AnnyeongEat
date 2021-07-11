package tech.erubin.annyeong_eat.telegramBot;

import lombok.Getter;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.PropertySource;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Map;

@Component
@PropertySource(value = "classpath:messages.properties", encoding = "UTF-8")
@Getter
public class SettingMessage {
    @Value("${regular.errorTrigger}")
    private String regularError;

    @Value("${message.error.blank}")
    private String messageErrorBlank;

    @Value("${message.error.obsceneWord}")
    private String messageErrorObsceneWord;

    @Value("${message.error.noRussianChars}")
    private String messageErrorNoRussianChars;

    @Value("${message.error.number}")
    private String messageErrorNumber;

    @Value("${message.error.noCorrectChar}")
    private String messageErrorNoCorrectChar;

    @Value("${message.error.bigLength}")
    private String messageErrorBigLength;

    @Value("${message.error.littleLength}")
    private String messageErrorLittleLength;

    @Value("${message.error.formatPhoneNumber}")
    private String messageErrorFormatPhoneNumber;

    @Value("${name.flag}")
    private String nameFlag;

    @Value("${surname.flag}")
    private String surnameFlag;

    @Value("${flag.phoneNumber}")
    private String phoneNumberFlag;

    @Value("${city.flag}")
    private String cityFlag;

    @Value("${name.noError}")
    private String nameNoError;

    @Value("${surname.noError}")
    private String surnameNoError;

    @Value("${phoneNumber.noError}")
    private String phoneNumberNoError;

    @Value("${city.noError}")
    private String cityNoError;

    @Value("${message.introductory}")
    private String messageIntroductory;



    public Map<String, String> getNoErrorMessage(){
        return Map.of(
                nameFlag, nameNoError,
                surnameFlag, surnameNoError,
                phoneNumberFlag, phoneNumberNoError,
                cityFlag, cityNoError);
    }

    public List<String> getFlags(){
        return List.of(nameFlag, surnameFlag, phoneNumberFlag, cityFlag);
    }

    public Map<String, String> getErrorMessage(){
        return Map.of(
                "IsBlank", messageErrorBlank,
                "RussianChar", messageErrorNoRussianChars,
                "мат", messageErrorObsceneWord,
                "IsNumber", messageErrorNumber,
                "неразрешенные символы", messageErrorNoCorrectChar,
                "большая длина", messageErrorBigLength,
                "маленькая длина", messageErrorLittleLength
        );
    }

    @Override
    public String toString() {
        return "SettingMessage{" +
                "regularError='" + regularError + '\'' +
                ", messageErrorBlank='" + messageErrorBlank + '\'' +
                ", messageErrorObsceneWord='" + messageErrorObsceneWord + '\'' +
                ", messageErrorEnglish='" + messageErrorNoRussianChars + '\'' +
                ", messageErrorNumber='" + messageErrorNumber + '\'' +
                ", messageErrorNoCorrectChar='" + messageErrorNoCorrectChar + '\'' +
                ", nameFlag='" + nameFlag + '\'' +
                ", surnameFlag='" + surnameFlag + '\'' +
                ", phoneNumberFlag='" + phoneNumberFlag + '\'' +
                ", cityFlag='" + cityFlag + '\'' +
                ", nameNoError='" + nameNoError + '\'' +
                ", surnameNoError='" + surnameNoError + '\'' +
                ", phoneNumberNoError='" + phoneNumberNoError + '\'' +
                ", cityNoError='" + cityNoError + '\'' +
                ", messageIntroductory='" + messageIntroductory + '\'' +
                '}';
    }
}
