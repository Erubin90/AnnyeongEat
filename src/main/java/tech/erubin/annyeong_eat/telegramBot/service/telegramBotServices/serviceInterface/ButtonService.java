package tech.erubin.annyeong_eat.telegramBot.service.telegramBotServices.serviceInterface;

import org.telegram.telegrambots.meta.api.objects.replykeyboard.ReplyKeyboardMarkup;
import tech.erubin.annyeong_eat.telegramBot.entity.Client;

public interface ButtonService {
    ReplyKeyboardMarkup clientRegistrationCity();

    ReplyKeyboardMarkup clientMainMenu();

    ReplyKeyboardMarkup clientOrderRegistration();

    ReplyKeyboardMarkup clientCheckOrder();

    ReplyKeyboardMarkup clientHelp();

    ReplyKeyboardMarkup clientProfileInfo(Client client);

    ReplyKeyboardMarkup clientRefactorProfile();

    ReplyKeyboardMarkup clear();
}