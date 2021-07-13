package tech.erubin.annyeong_eat.telegramBot.service.telegramBotServices;

import org.springframework.stereotype.Service;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.ReplyKeyboardMarkup;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.KeyboardButton;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.KeyboardRow;
import tech.erubin.annyeong_eat.telegramBot.entity.Client;
import tech.erubin.annyeong_eat.telegramBot.messages.ButtonNames;
import tech.erubin.annyeong_eat.telegramBot.service.entityServises.CafeServiceImpl;
import tech.erubin.annyeong_eat.telegramBot.service.telegramBotServices.serviceInterface.ReplyButtonService;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

@Service
public class ReplyButtonServiceImpl implements ReplyButtonService {
    CafeServiceImpl cafeService;
    ButtonNames buttonNames;

    public ReplyButtonServiceImpl(CafeServiceImpl cafeService, ButtonNames buttonNames) {
        this.cafeService = cafeService;
        this.buttonNames = buttonNames;
    }

    public ReplyKeyboardMarkup clientRegistrationCity(){
        Set<String> buttonNames = cafeService.getAllCity();
        List<KeyboardRow> keyboardRows = new ArrayList<>();
        for (String buttonName: buttonNames) {
            KeyboardRow row = new KeyboardRow();
            row.add(new KeyboardButton(buttonName));
            keyboardRows.add(row);
        }
        return new ReplyKeyboardMarkup(keyboardRows, true, true, false);
    }

    @Override
    public ReplyKeyboardMarkup clientMainMenu() {
        List<String> buttonNames = this.buttonNames.getMainMenuClientButton();
        return getReplyKeyboardMarkup(buttonNames);
    }

    @Override
    public ReplyKeyboardMarkup clear() {
        List<KeyboardRow> keyboardRows = new ArrayList<>();
        return new ReplyKeyboardMarkup(keyboardRows, true, true, false);
    }

    @Override
    public ReplyKeyboardMarkup clientOrderRegistration() {

        return null;
    }

    @Override
    public ReplyKeyboardMarkup clientCheckOrder() {
        List<String> buttonNames = this.buttonNames.getCheckOrderButton();
        return getReplyKeyboardMarkup(buttonNames);
    }

    @Override
    public ReplyKeyboardMarkup clientHelp() {
        List<String> buttonNames = this.buttonNames.getHelpClientButton();
        return getReplyKeyboardMarkup(buttonNames);
    }


    @Override
    public ReplyKeyboardMarkup clientProfileInfo(Client client) {
        List<String> buttonNames = this.buttonNames.getProfileInfoButton();
        return getReplyKeyboardMarkup(buttonNames);
    }

    @Override
    public ReplyKeyboardMarkup clientRefactorProfile() {
        return null;
    }

    private ReplyKeyboardMarkup getReplyKeyboardMarkup(List<String> buttonNames) {
        List<KeyboardRow> keyboardRows = new ArrayList<>();
        for (String buttonName: buttonNames) {
            KeyboardRow row = new KeyboardRow();
            row.add(new KeyboardButton(buttonName));
            keyboardRows.add(row);
        }
        return  new ReplyKeyboardMarkup(keyboardRows, true, false, false);
    }
}
