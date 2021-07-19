package tech.erubin.annyeong_eat.telegramBot.service.telegramBotServices.serviceInterface;

import org.telegram.telegrambots.meta.api.objects.replykeyboard.ReplyKeyboardMarkup;
import tech.erubin.annyeong_eat.telegramBot.entity.Client;
import tech.erubin.annyeong_eat.telegramBot.entity.Order;

public interface ReplyButtonService {
    //Registration Module
    ReplyKeyboardMarkup clientRegistrationCity();
    ReplyKeyboardMarkup clientOrEmployeeRegistration();

    //Main Menu Module
    ReplyKeyboardMarkup clientMainMenu();
    ReplyKeyboardMarkup clientCheckOrder();
    ReplyKeyboardMarkup clientHelp();
    ReplyKeyboardMarkup clientProfileInfo(Client client);
//    ReplyKeyboardMarkup employeeMainMenu(Employee employee);

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