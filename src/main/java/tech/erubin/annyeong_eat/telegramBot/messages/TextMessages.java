package tech.erubin.annyeong_eat.telegramBot.messages;

import lombok.Getter;
import lombok.ToString;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.PropertySource;
import org.springframework.stereotype.Component;

@Getter
@ToString
@Component
@PropertySource(value = "classpath:messages.properties", encoding = "UTF-8")
public class TextMessages {
    @Value("${regular.errorTrigger}")
    private String regularError = "";

    //Error Messeges
    @Value("${message.error.blank}")
    private String errorBlank = "Blank ";

    @Value("${message.error.obsceneWord}")
    private String errorObsceneWord = "ObsceneWord ";

    @Value("${message.error.noRussianChars}")
    private String errorNoRussianChars = "NoRussianChars ";

    @Value("${message.error.number}")
    private String errorNumber = "Number ";

    @Value("${message.error.noCorrectChar}")
    private String errorNoCorrectChar = "NoCorrectChar ";

    @Value("${message.error.bigLength}")
    private String messageErrorBigLength = "BigLength %s ";

    @Value("${message.error.littleLength}")
    private String messageErrorLittleLength = "LittleLength %s ";

    @Value("${message.error.formatPhoneNumber}")
    private String messageErrorFormatPhoneNumber = "FormatPhoneNumber ";

    @Value("${message.error.nameCity}")
    private String messageErrorNameCity = "NameCity ";

    //noError Message's
    @Value("${name.noError}")
    private String nameNoError = "ok";

    @Value("${surname.noError}")
    private String surnameNoError = "ok";

    @Value("${phoneNumber.noError}")
    private String phoneNumberNoError = "ok";

    @Value("${city.noError}")
    private String cityNoError = "ok";

    //Introduction message
    @Value("${message.start.client.registration}")
    private String messageStartClientRegistration;

    @Value("${message.start.client.mainMenu}")
    private String messageStartMainClientMenu;

    //    public Map<String, String> getMainMenuClientMessage() {
//        return Map.of(mainMenuButtonClientOrder, "Оформление заказа",
//                mainMenuButtonClientCheckOrder, "",
//                mainMenuButtonClientInfo, "",
//                mainMenuButtonClientHelp, "");
//    }


}
