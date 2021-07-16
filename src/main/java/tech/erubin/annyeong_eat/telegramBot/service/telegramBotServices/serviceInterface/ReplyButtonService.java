package tech.erubin.annyeong_eat.telegramBot.service.telegramBotServices.serviceInterface;

import org.telegram.telegrambots.meta.api.objects.replykeyboard.ReplyKeyboardMarkup;
import tech.erubin.annyeong_eat.telegramBot.entity.Client;
import tech.erubin.annyeong_eat.telegramBot.entity.Order;

public interface ReplyButtonService {
    //Registration Module
    ReplyKeyboardMarkup clientRegistrationCity();

    //Main Menu Module
    ReplyKeyboardMarkup clientMainMenu();
    ReplyKeyboardMarkup clientCheckOrder();
    ReplyKeyboardMarkup clientHelp();
    ReplyKeyboardMarkup clientProfileInfo(Client client);

    //Order Module
    ReplyKeyboardMarkup clientOrderCafe(Client client);
    ReplyKeyboardMarkup clientOrderMenu(Order order);
    ReplyKeyboardMarkup clientOrderAddress(Client client);
    ReplyKeyboardMarkup clientOrderPhoneNumber(Client client);
    ReplyKeyboardMarkup clientOrderPayment();
    ReplyKeyboardMarkup clientOrderConfirmation();
    ReplyKeyboardMarkup clientFixOrder();

    //All Module
}