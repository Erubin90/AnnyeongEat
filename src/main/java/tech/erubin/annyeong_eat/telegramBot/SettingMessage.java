package tech.erubin.annyeong_eat.telegramBot;

import lombok.Getter;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.PropertySource;
import org.springframework.stereotype.Component;

import java.util.List;

@Getter
@Component
@PropertySource(value = "classpath:messages.properties", encoding = "UTF-8")
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

    public List<String> getFlags(){
        return List.of(nameFlag, surnameFlag, phoneNumberFlag, cityFlag);
    }
}
