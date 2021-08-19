package tech.erubin.annyeong_eat.telegramBot.module.order;

import lombok.Getter;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.PropertySource;
import org.springframework.stereotype.Component;
import tech.erubin.annyeong_eat.telegramBot.entity.ChequeDish;
import tech.erubin.annyeong_eat.telegramBot.entity.ChequeDishOptionally;
import tech.erubin.annyeong_eat.telegramBot.entity.Dish;
import tech.erubin.annyeong_eat.telegramBot.entity.Order;

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

    public List<String> getBackAndNextButton() {
        return List.of(back, next);
    }

    public List<String> getPaymentMethod(){
        return List.of(cash, cards);
    }

    public List<String> getBackAndBasketAndNextButton(Order order) {
        List<ChequeDish> chequeDishes = order.getChequeDishList();
        String basket = "\uD83D\uDED2 %s₽";
        if (chequeDishes == null) {
            basket = String.format(basket, 0.0);
        }
        else {
            double sum = 0.0;
            for (ChequeDish chequeDish : chequeDishes) {
                sum += chequeDish.getDishId().getPrice() * chequeDish.getCountDishes();
                if (chequeDish.getChequeDishOptionallyList() != null) {
                    List<ChequeDishOptionally> dishOptionallyList = chequeDish.getChequeDishOptionallyList();
                    for (ChequeDishOptionally chequeDishOptionally : dishOptionallyList) {
                        sum += chequeDishOptionally.getDishOptionallyId().getPrice() * chequeDishOptionally.getCount();
                    }
                }
            }
            basket = String.format(basket, sum);
        }
        return List.of(back, basket, next);
    }

    public List<String> getTypeDishesInCafe(Order order) {
        return order.getCafeId().getDishesMenu()
                .stream()
                .map(Dish::getType)
                .distinct()
                .collect(Collectors.toList());
    }

    public List<String> getTagDishesInCafe(Order order) {
        return order.getCafeId().getDishesMenu()
                .stream()
                .map(Dish::getTag)
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
        orderMenuRows.add(getBackAndBasketAndNextButton(order));
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

    public List<List<String>> getFixOrderRows() {
        List<List<String>> orderEdition = new ArrayList<>();
        orderEdition.add(List.of(editionMenu, editionAddress));
        orderEdition.add(List.of(editionPhoneNumber, editionPayment));
        orderEdition.add(List.of(back));
        orderEdition.add(List.of(confirm));
        return orderEdition;
    }
}
