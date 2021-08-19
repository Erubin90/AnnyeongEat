package tech.erubin.annyeong_eat.telegramBot.module;

import lombok.AllArgsConstructor;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.ReplyKeyboardMarkup;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.KeyboardRow;
import tech.erubin.annyeong_eat.telegramBot.entity.Client;
import tech.erubin.annyeong_eat.telegramBot.entity.Order;
import tech.erubin.annyeong_eat.telegramBot.module.mainMenu.MainMenuButtonNames;
import tech.erubin.annyeong_eat.telegramBot.module.order.OrderButtonNames;
import tech.erubin.annyeong_eat.telegramBot.module.registration.RegistrationButtonName;
import tech.erubin.annyeong_eat.telegramBot.service.CafeServiceImpl;

import java.util.*;

@Component
@AllArgsConstructor
public class ReplyButtons {
    private final CafeServiceImpl cafeService;

    private final RegistrationButtonName registrationButtonName;
    private final MainMenuButtonNames mainMenuButtonNames;
    private final OrderButtonNames orderButtonNames;

    public ReplyKeyboardMarkup clientRegistrationCity() {
        List<List<String>> buttonNames = registrationButtonName.getCityList();
        return getReplyKeyboardMarkup(buttonNames, true);
    }

    public ReplyKeyboardMarkup clientMainMenu() {
        List<String> buttonNames = mainMenuButtonNames.getMainMenuClientButton();
        return getReplyKeyboardMarkup(buttonNames);
    }

    public ReplyKeyboardMarkup clientCheckOrder() {
        List<String> buttonNames = mainMenuButtonNames.getCheckOrderButton();
        return getReplyKeyboardMarkup(buttonNames);
    }

    public ReplyKeyboardMarkup clientHelp() {
        List<String> buttonNames = mainMenuButtonNames.getHelpClientButton();
        return getReplyKeyboardMarkup(buttonNames);
    }

    public ReplyKeyboardMarkup clientProfileInfo(Client client) {
        List<String> buttonNames = mainMenuButtonNames.getProfileInfoButton();
        return getReplyKeyboardMarkup(buttonNames);
    }

    public ReplyKeyboardMarkup clientOrderCafe(Client client) {
        List<List<String>> buttonName = new ArrayList<>();
        List<String> cafeNames = cafeService.getCafeNameByCity(client.getCity());
        List<String> back = List.of(orderButtonNames.getBack());
        for (String cafeName : cafeNames) {
            buttonName.add(List.of(cafeName));
        }
        buttonName.add(back);
        return getReplyKeyboardMarkup(buttonName, false);
    }

    public ReplyKeyboardMarkup clientOrderMenu(Order order) {
        List<List<String>> buttonNames = orderButtonNames.getMenuRows(order);
        return getReplyKeyboardMarkup(buttonNames, false);
    }

    public ReplyKeyboardMarkup clientOrderAddress(Client client) {
        Set<String> buttonNames = new HashSet<>();
        List<Order> orderList = client.getOrderList();

        for (Order order : orderList){
            if(order.getAddress() != null){
                buttonNames.add(order.getAddress());
            }
        }

        List<KeyboardRow> keyboardRows = getKeyboardManyButtonRows(buttonNames);
        keyboardRows.add(getKeyboardOneButtonRow(orderButtonNames.getBack()));
        return new ReplyKeyboardMarkup(keyboardRows, true, false, false);
    }

    public ReplyKeyboardMarkup clientOrderPhoneNumber(Client client) {
        String clientPhoneNumber = client.getPhoneNumber();
        Set<String> buttonNames = new HashSet<>();
        buttonNames.add(clientPhoneNumber);
        List<Order> orderList = client.getOrderList();

        for (Order order : orderList) {
            if(order.getPhoneNumber() != null){
                buttonNames.add(order.getPhoneNumber());
            }
        }

        List<KeyboardRow> keyboardRows = getKeyboardManyButtonRows(buttonNames);
        keyboardRows.add(getKeyboardOneButtonRow(orderButtonNames.getBack()));
        return new ReplyKeyboardMarkup(keyboardRows, true, false, false);
    }

    public ReplyKeyboardMarkup clientOrderPayment() {
        List<List<String>> buttonNames = orderButtonNames.getPaymentRows();
        return getReplyKeyboardMarkup(buttonNames, false);
    }

    public ReplyKeyboardMarkup clientOrderConfirmation() {
        List<List<String>> buttonNames = orderButtonNames.getConfirmRows();
        return getReplyKeyboardMarkup(buttonNames, false);
    }

    public ReplyKeyboardMarkup clientFixOrder() {
        List<List<String>> buttonNames = orderButtonNames.getFixOrderRows();
        return getReplyKeyboardMarkup(buttonNames, false);
    }

    private ReplyKeyboardMarkup getReplyKeyboardMarkup(Collection<String> buttonNames) {
        List<KeyboardRow> keyboardRows = new ArrayList<>();
        for (String buttonName : buttonNames) {
            KeyboardRow row = new KeyboardRow();
            row.add(buttonName);
            keyboardRows.add(row);
        }
        return new ReplyKeyboardMarkup(keyboardRows, true, false, false);
    }

    private ReplyKeyboardMarkup getReplyKeyboardMarkup(List<List<String>> buttonNames, boolean oneTimeKeyboard) {
        List<KeyboardRow> keyboardRows = new ArrayList<>();
        for (List<String> buttonName : buttonNames) {
            KeyboardRow row = new KeyboardRow();
            row.addAll(buttonName);
            keyboardRows.add(row);
        }
        return new ReplyKeyboardMarkup(keyboardRows, true, oneTimeKeyboard, false);
    }

    private List<KeyboardRow> getKeyboardManyButtonRows(Set<String> buttonNames) {
        List<KeyboardRow> keyboardRows = new ArrayList<>();
        for (String buttonName : buttonNames) {
            KeyboardRow row = new KeyboardRow();
            row.add(buttonName);
            keyboardRows.add(row);
        }
        return keyboardRows;
    }

    private KeyboardRow getKeyboardOneButtonRow(String buttonName) {
        KeyboardRow row = new KeyboardRow();
        row.add(buttonName);
        return row;
    }
}
