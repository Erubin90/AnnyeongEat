package tech.erubin.annyeong_eat.telegramBot.abstractClass;

import lombok.Getter;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.PropertySource;
import org.springframework.stereotype.Component;
import tech.erubin.annyeong_eat.entity.Cafe;
import tech.erubin.annyeong_eat.entity.Dish;
import tech.erubin.annyeong_eat.entity.Order;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@Getter
@Component
@PropertySource(value = "classpath:messages.properties", encoding = "UTF-8")
public abstract class AbstractButton {

    @Value("${button.back}")
    protected String back;

    @Value("${button.next}")
    protected String next;

    @Value("${button.delete}")
    protected String delete;

    @Value("${button.add}")
    protected String add;

    @Value("${button.sub}")
    protected String sub;

    @Value("${order.button.cash}")
    protected String cash;

    @Value("${order.button.cards}")
    protected String cards;

    @Value("${order.button.delivery}")
    protected String delivery;

    @Value("${order.button.pickup}")
    protected String pickup;

    @Value("${order.button.delivery.courier}")
    protected String courier;

    @Value("${order.button.delivery.taxi}")
    protected String taxi;

    @Value("${order.button.confirm}")
    protected String confirm;

    @Value("${order.button.editor.payment}")
    protected String editionPayment;

    @Value("${mainMenu.button.order}")
    protected String createOrder;

    @Value("${mainMenu.button.checkOrder}")
    protected String checkOrder;

    @Value("${mainMenu.button.help}")
    protected String help;

    @Value("${mainMenu.button.info}")
    protected String clientInfo;

    @Value("${error.putButton}")
    protected String putButton;

    @Value("${employee.button.editOrder}")
    protected String editOrder;

    @Value("${employee.button.accept}")
    protected String accept;

    @Value("${employee.button.cancel}")
    protected String cancel;

    @Value("${employee.button.acceptState}")
    protected String acceptState;

    @Value("${employee.button.cancelState}")
    protected String cancelState;

    @Value("${employee.button.restart}")
    protected String restart;

    @Value("${button.tag.info}")
    protected String tagInfo;

    @Value("${message.tag.info}")
    protected String messageInfo;

    @Value("${employee.button.addDishes}")
    protected String addDishes;

    @Value("${employee.button.changeQuantity}")
    protected String changeQuantity;

    @Value("${operator.button.form}")
    protected String form;

    public List<String> typeDishesInCafe(Cafe cafe) {
        return cafe.getDishesMenu()
                .stream()
                .map(Dish::getType)
                .distinct()
                .collect(Collectors.toList());
    }

    protected List<String> obtainingButtons() {
        return List.of(delivery, pickup, back);
    }

    protected List<String> operatorMainMenuButtons() {
        return List.of(createOrder, form);
    }

    protected List<List<String>> dishesMenuButtons(Order order) {
        List<List<String>> orderMenuRows = new ArrayList<>();
        List<String> typeDishes = typeDishesInCafe(order.getCafeId());
        List<String> littleButtonName = typeDishes.stream()
                .filter(x -> x.length() < 15)
                .collect(Collectors.toList());
        int size = littleButtonName.size();
        boolean bigButtonNameFlag = typeDishes.removeAll(littleButtonName);

        for (int i = 0; i < size - size % 2; i += 2) {
            orderMenuRows.add(List.of(littleButtonName.get(i), littleButtonName.get(i + 1)));
        }
        if (size % 2 == 1) {
            orderMenuRows.add(List.of(littleButtonName.get(size - 1)));
        }
        if (bigButtonNameFlag) {
            for (String type : typeDishes) {
                orderMenuRows.add(List.of(type));
            }
        }
        orderMenuRows.add(backAndBasketAndNextButton());
        return orderMenuRows;
    }

    protected List<List<String>> paymentButtons() {
        List<List<String>> orderPayment = new ArrayList<>();
        orderPayment.add(List.of(cash));
        orderPayment.add(List.of(cards));
        orderPayment.add(List.of(back));
        return orderPayment;
    }

    protected List<List<String>> confirmButtons() {
        List<List<String>> orderConfirm = new ArrayList<>();
        orderConfirm.add(List.of(confirm));
        orderConfirm.add(List.of(back));
        return orderConfirm;
    }

    protected List<List<String>> getCityListButtons(Set<String> cafeCits) {
        List<List<String>> buttonNames = new ArrayList<>();
        for (String cafeCity : cafeCits) {
            buttonNames.add(List.of(cafeCity));
        }
        return buttonNames;
    }

    protected List<String> clientMainMenuButtons(){
        List<String> buttonsName = new ArrayList<>();
        buttonsName.add(createOrder);
        buttonsName.add(checkOrder);
        buttonsName.add(clientInfo);
        buttonsName.add(help);
        return buttonsName;
    }

    protected List<String> clientHelpButtons(){
        return List.of(back);
    }

    protected List<String> checkOrderButtons(){
        return List.of(back);
    }

    protected List<String> profileInfoButtons(){
        return List.of(back);
    }

    private List<String> backAndBasketAndNextButton() {
        String basket = "\uD83D\uDED2";
        return List.of(back, basket, next);
    }
}
