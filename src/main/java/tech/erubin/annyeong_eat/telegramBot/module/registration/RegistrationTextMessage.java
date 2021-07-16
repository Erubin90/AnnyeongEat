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
    private String errorTrigger = "error";

    //Error Messeges
    @Value("${message.error.blank}")
    private String errorBlank = " error Blank ";

    @Value("${message.error.obsceneWord}")
    private String errorObsceneWord = "error ObsceneWord ";

    @Value("${message.error.falseTextForm}")
    private String errorFalseTextForm = "error NoRussianChars ";

    @Value("${message.error.number}")
    private String errorNumber = "error Number ";

    @Value("${message.error.noCorrectChar}")
    private String errorNoCorrectChar = "error NoCorrectChar ";

    @Value("${message.error.bigLength}")
    private String errorBigLength = "error BigLength %s ";

    @Value("${message.error.littleLength}")
    private String errorLittleLength = "error LittleLength %s ";

    @Value("${message.error.formatPhoneNumber}")
    private String errorFormatPhoneNumber = "error FormatPhoneNumber ";

    @Value("${message.error.nameCity}")
    private String errorNameCity = "error NameCity ";

    //noError Message's
    @Value("${registration.message.client.name.noError}")
    private String nameNoError = "ok";

    @Value("${registration.message.client.surname.noError}")
    private String surnameNoError = "ok";

    @Value("${registration.message.phoneNumber.noError}")
    private String phoneNumberNoError = "ok";

    @Value("${registration.message.city.noError}")
    private String cityNoError = "ok";

    //Introduction message
    @Value("${registration.message.client.start}")
    private String startClientRegistration;

    @Value("${registration.message.client.end}")
    private String endClientRegistration;
}
