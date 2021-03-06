package tech.erubin.annyeong_eat.telegramBot.abstractClass;

import lombok.Getter;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.PropertySource;
import org.springframework.stereotype.Component;

@Getter
@Component
@PropertySource(value = "classpath:messages.properties", encoding = "UTF-8")
public abstract class AbstractHandler {

    @Value("${regular.error.trigger}")
    protected String errorTrigger;

    //Error Messages
    @Value("${message.error.obsceneWord}")
    protected String errorObsceneWord;

    @Value("${message.error.falseTextForm}")
    protected String errorFalseTextForm;

    @Value("${message.error.number}")
    protected String errorNumber;

    @Value("${message.error.noCorrectChar}")
    protected String errorNoCorrectChar;

    @Value("${message.error.bigLength}")
    protected String errorBigLength;

    @Value("${message.error.littleLength}")
    protected String errorLittleLength;

    @Value("${message.error.formatPhoneNumber}")
    protected String errorFormatPhoneNumber;

    //noError Message's
    @Value("${registration.message.name}")
    protected String nameNoError;

    @Value("${registration.message.surname}")
    protected String surnameNoError;

    @Value("${registration.message.phoneNumber}")
    protected String phoneNumberNoError;

    @Value("${registration.message.city}")
    protected String cityNoError;

    @Value("${noError.address}")
    protected String addressNoError;

    @Value("${button.tag.info}")
    protected String tagInfo;

    @Value("${message.tag.info}")
    protected String messageInfo;

    @Value("${message.buttonNotWork}")
    protected String buttonNotWork;
}
