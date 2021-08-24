package tech.erubin.annyeong_eat.telegramBot.module.buttons;

import lombok.AllArgsConstructor;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.ReplyKeyboardMarkup;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.KeyboardRow;
import tech.erubin.annyeong_eat.entity.User;
import tech.erubin.annyeong_eat.entity.Order;

import tech.erubin.annyeong_eat.service.CafeServiceImpl;

import java.util.*;

@Component
@AllArgsConstructor
public class ReplyButtons {
    private final CafeServiceImpl cafeService;

    private final ButtonNames buttonNames;

    public ReplyKeyboardMarkup clientRegistrationCity() {
        List<List<String>> buttonNames = this.buttonNames.getCityList();
        return getReplyKeyboardMarkup(buttonNames, true);
    }

    public ReplyKeyboardMarkup clientMainMenu() {
        List<String> buttons = buttonNames.getMainMenuClientButton();
        return getReplyKeyboardMarkup(buttons);
    }

    public ReplyKeyboardMarkup clientCheckOrder() {
        List<String> buttons = buttonNames.getCheckOrderButton();
        return getReplyKeyboardMarkup(buttons);
    }

    public ReplyKeyboardMarkup clientHelp() {
        List<String> buttons = buttonNames.getHelpClientButton();
        return getReplyKeyboardMarkup(buttons);
    }

    public ReplyKeyboardMarkup clientProfileInfo() {
        List<String> buttons = buttonNames.getProfileInfoButton();
        return getReplyKeyboardMarkup(buttons);
    }

    public ReplyKeyboardMarkup clientOrderCafe(User user) {
        List<List<String>> buttonName = new ArrayList<>();
        List<String> cafeNames = cafeService.getCafeNameByCity(user.getCity());
        List<String> back = List.of(buttonNames.getBack());
        for (String cafeName : cafeNames) {
            buttonName.add(List.of(cafeName));
        }
        buttonName.add(back);
        return getReplyKeyboardMarkup(buttonName, false);
    }

    public ReplyKeyboardMarkup clientOrderMenu(Order order) {
        List<List<String>> buttonNames = this.buttonNames.getMenuRows(order);
        return getReplyKeyboardMarkup(buttonNames, false);
    }

    public ReplyKeyboardMarkup clientOrderAddress(User user) {
        Set<String> buttonNames = new HashSet<>();
        List<Order> orderList = user.getOrderList();

        for (Order order : orderList){
            if(order.getAddress() != null){
                buttonNames.add(order.getAddress());
            }
        }

        List<KeyboardRow> keyboardRows = getKeyboardManyButtonRows(buttonNames);
        keyboardRows.add(getKeyboardOneButtonRow(this.buttonNames.getBack()));
        return new ReplyKeyboardMarkup(keyboardRows, true, false, false);
    }

    public ReplyKeyboardMarkup clientOrderPhoneNumber(User user) {
        String clientPhoneNumber = user.getPhoneNumber();
        Set<String> buttonNames = new HashSet<>();
        buttonNames.add(clientPhoneNumber);
        List<Order> orderList = user.getOrderList();

        for (Order order : orderList) {
            if(order.getPhoneNumber() != null){
                buttonNames.add(order.getPhoneNumber());
            }
        }

        List<KeyboardRow> keyboardRows = getKeyboardManyButtonRows(buttonNames);
        keyboardRows.add(getKeyboardOneButtonRow(this.buttonNames.getBack()));
        return new ReplyKeyboardMarkup(keyboardRows, true, false, false);
    }

    public ReplyKeyboardMarkup clientOrderPayment() {
        List<List<String>> buttonNames = this.buttonNames.getPaymentRows();
        return getReplyKeyboardMarkup(buttonNames, false);
    }

    public ReplyKeyboardMarkup clientOrderConfirmation() {
        List<List<String>> buttonNames = this.buttonNames.getConfirmRows();
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
