package tech.erubin.annyeong_eat.telegramBotClient.module.order;

import lombok.Getter;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.PropertySource;
import org.springframework.stereotype.Component;
import tech.erubin.annyeong_eat.telegramBotClient.entity.Dish;
import tech.erubin.annyeong_eat.telegramBotClient.entity.Order;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Getter
@Component
@PropertySource(value = "classpath:messages.properties", encoding = "UTF-8")
public class OrderButtonNames {
    @Value("${button.back}")
    private String back;

    @Value("${button.next}")
    private String next;

    @Value("${order.button.client.cash}")
    private String cash;

    @Value("${order.button.client.cards}")
    private String cards;

    @Value("${order.button.client.confirm}")
    private String confirm;

    @Value("${order.button.client.editor}")
    private String edition;

    @Value("${order.button.client.editor.menu}")
    private String editionMenu;

    @Value("${order.button.client.editor.address}")
    private String editionAddress;

    @Value("${order.button.client.editor.phoneNumber}")
    private String editionPhoneNumber;

    @Value("${order.button.client.editor.payment}")
    private String editionPayment;

    public List<String> getPaymentMethod(){
        return List.of(cash, cards);
    }

    public List<String> getBackAndBasketAndNextButton() {
        String basket = "\uD83D\uDED2";
        return List.of(back, basket, next);
    }

    public List<String> getTypeDishesInCafe(Order order) {
        return order.getCafeId().getDishesMenu()
                .stream()
                .map(Dish::getType)
                .distinct()
                .collect(Collectors.toList());
    }

    public List<List<String>> getMenuRows(Order order) {
        List<List<String>> orderMenuRows = new ArrayList<>();
        List<String> typeDishes = getTypeDishesInCafe(order);
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
        orderMenuRows.add(getBackAndBasketAndNextButton());
        return orderMenuRows;
    }

    public List<List<String>> getPaymentRows() {
        List<List<String>> orderPayment = new ArrayList<>();
        orderPayment.add(List.of(cash));
        orderPayment.add(List.of(cards));
        orderPayment.add(List.of(back));
        return orderPayment;
    }

    public List<List<String>> getConfirmRows() {
        List<List<String>> orderConfirm = new ArrayList<>();
        orderConfirm.add(List.of(confirm));
        orderConfirm.add(List.of(back));
        return orderConfirm;
    }
}
