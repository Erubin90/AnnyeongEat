package tech.erubin.annyeong_eat.telegramBot.module.registration;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.ToString;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.PropertySource;
import org.springframework.stereotype.Component;

@Getter
@ToString
@Component
@PropertySource(value = "classpath:messages.properties", encoding = "UTF-8")
@NoArgsConstructor
public class RegistrationTextMessage {
    @Value("${regular.errorTrigger}")
    private String errorTrigger;

    //Error Messeges
    @Value("${message.error.blank}")
    private String errorBlank;

    @Value("${message.error.obsceneWord}")
    private String errorObsceneWord;

    @Value("${message.error.falseTextForm}")
    private String errorFalseTextForm;

    @Value("${message.error.number}")
    private String errorNumber;

    @Value("${message.error.noCorrectChar}")
    private String errorNoCorrectChar;

    @Value("${message.error.bigLength}")
    private String errorBigLength;

    @Value("${message.error.littleLength}")
    private String errorLittleLength;

    @Value("${message.error.formatPhoneNumber}")
    private String errorFormatPhoneNumber;

    @Value("${message.error.nameCity}")
    private String errorNameCity;

    //noError Message's
    @Value("${registration.message.client.name.noError}")
    private String nameNoError;

    @Value("${registration.message.client.surname.noError}")
    private String surnameNoError;

    @Value("${registration.message.phoneNumber.noError}")
    private String phoneNumberNoError;

    @Value("${registration.message.city.noError}")
    private String cityNoError;

    @Value("${registration.message.client.error}")
    private String error;

    //Introduction message
    @Value("${registration.message.client.start}")
    private String startClientRegistration;

    @Value("${registration.message.client.end}")
    private String endClientRegistration;
}
