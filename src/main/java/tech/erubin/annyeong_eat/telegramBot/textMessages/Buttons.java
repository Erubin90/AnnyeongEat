package tech.erubin.annyeong_eat.telegramBot.textMessages;

import lombok.Getter;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.PropertySource;
import org.springframework.stereotype.Component;
import tech.erubin.annyeong_eat.entity.Dish;
import tech.erubin.annyeong_eat.entity.Order;
import tech.erubin.annyeong_eat.telegramBot.enums.EmployeeEnum;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@Getter
@Component
@PropertySource(value = "classpath:messages.properties", encoding = "UTF-8")
public abstract class Buttons {

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

    @Value("${button.good}")
    protected String good;

    @Value("${order.button.cash}")
    protected String cash;

    @Value("${order.button.cards}")
    protected String cards;

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

    @Value("${employee.button.client}")
    protected String client;

    @Value("${employee.button.employee}")
    protected String employee;

    @Value("${message.error.putButton}")
    protected String putButton;

    @Value("${employee.button.exit}")
    protected String exit;

    @Value("${employee.button.accept}")
    protected String accept;

    @Value("${employee.button.cancel}")
    protected String cancel;

    @Value("${button.tag.info}")
    protected String tagInfo;

    @Value("${message.tag.info}")
    protected String messageInfo;


    public List<String> paymentMethod(){
        return List.of(cash, cards);
    }

    public List<String> backAndBasketAndNextButton() {
        String basket = "\uD83D\uDED2";
        return List.of(back, basket, next);
    }

    public List<String> typeDishesInCafe(Order order) {
        return order.getCafeId().getDishesMenu()
                .stream()
                .map(Dish::getType)
                .distinct()
                .collect(Collectors.toList());
    }

    public List<List<String>> dishesMenuButtons(Order order) {
        List<List<String>> orderMenuRows = new ArrayList<>();
        List<String> typeDishes = typeDishesInCafe(order);
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

    public List<List<String>> paymentButtons() {
        List<List<String>> orderPayment = new ArrayList<>();
        orderPayment.add(List.of(cash));
        orderPayment.add(List.of(cards));
        orderPayment.add(List.of(back));
        return orderPayment;
    }

    public List<List<String>> confirmButtons() {
        List<List<String>> orderConfirm = new ArrayList<>();
        orderConfirm.add(List.of(confirm));
        orderConfirm.add(List.of(back));
        return orderConfirm;
    }

    public List<List<String>> getCityListButtons(Set<String> cafeCits) {
        List<List<String>> buttonNames = new ArrayList<>();
        for (String cafeCity : cafeCits) {
            buttonNames.add(List.of(cafeCity));
        }
        return buttonNames;
    }

    public List<String> clientMainMenuButtons(){
        List<String> buttonsName = new ArrayList<>();
        buttonsName.add(createOrder);
        buttonsName.add(checkOrder);
        buttonsName.add(clientInfo);
        buttonsName.add(help);
        return buttonsName;
    }

    public List<String> employeeMainMenuButtons(EmployeeEnum employeeStateEnum) {
        List<String> buttons = new ArrayList<>();
        switch (employeeStateEnum) {
            case ADMINISTRATOR:
                buttons.add(exit);
                break;
            case OPERATOR:
                buttons.add(exit);
                break;
            case DEVELOPER:
                buttons.add(exit);
                break;
            case COURIER:
                buttons.add(exit);
                break;
        }
        return buttons;
    }

    public List<String> clientHelpButtons(){
        return List.of(back);
    }

    public List<String> checkOrderButtons(){
        return List.of(back);
    }

    public List<String> profileInfoButtons(){
        return List.of(back);
    }

    public List<String> choiceDepartmentButtons(List<String> departmentList) {
        List<String> buttons = new ArrayList<>();
        buttons.add(client);
        buttons.addAll(departmentList);
        return buttons;
    }

}
