package tech.erubin.annyeong_eat.telegramBot.buttons;

import lombok.NoArgsConstructor;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.InlineKeyboardMarkup;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.InlineKeyboardButton;
import tech.erubin.annyeong_eat.entity.ChequeDish;
import tech.erubin.annyeong_eat.entity.Dish;
import tech.erubin.annyeong_eat.entity.Order;
import tech.erubin.annyeong_eat.telegramBot.enums.EmployeeEnum;
import tech.erubin.annyeong_eat.telegramBot.enums.OrderEnum;
import tech.erubin.annyeong_eat.telegramBot.textMessages.Buttons;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Component
@NoArgsConstructor
public class InlineButtons extends Buttons {

    public InlineKeyboardMarkup clientCheque(String tag, ChequeDish chequeDish) {
        String count;
        if (chequeDish == null) {
            count = "0";
        }
        else {
            count = String.valueOf(chequeDish.getCountDishes());
        }
        List<List<InlineKeyboardButton>> rows = new ArrayList<>();
        rows.add(getSubCountAddButtons(count, tag));
        rows.add(List.of(getInlineButtons(delete, tag + "m0")));
        return new InlineKeyboardMarkup(rows);
    }

    public InlineKeyboardMarkup typeDishesMenu(List<Dish> dishList) {
        List<List<InlineKeyboardButton>> rows = new ArrayList<>();
        if (dishList != null && !dishList.isEmpty()) {
            for (Dish dish : dishList) {
                String nameButton = dish.getName() + " " + dish.getPrice() + "₽";
                rows.add(List.of(getInlineButtons(nameButton, String.valueOf(dish.getId()))));
            }
        }
        return new InlineKeyboardMarkup(rows);
    }

    public InlineKeyboardMarkup getFullOrderButtons(Order order) {
        List<List<InlineKeyboardButton>> rows = new ArrayList<>();
        if (order.getChequeDishList() != null && !order.getChequeDishList().isEmpty()) {
            List<Dish> dishList = order.getChequeDishList().stream()
                    .map(ChequeDish::getDishId)
                    .collect(Collectors.toList());
            int count = 1;
            for (Dish dish : dishList) {
                List<InlineKeyboardButton> buttonList = new ArrayList<>();
                buttonList.add(getInlineButtons(String.valueOf(count++), String.valueOf(dish.getId())));
                buttonList.add(getInlineButtons(delete, dish.getId() + "b0"));
                buttonList.add(getInlineButtons(sub, dish.getId() + "b-"));
                buttonList.add(getInlineButtons(add, dish.getId() + "b+"));
                rows.add(buttonList);
            }
        }
        return new InlineKeyboardMarkup(rows);
    }

    public InlineKeyboardMarkup getEmployee(EmployeeEnum department, Order order) {
        InlineKeyboardMarkup inlineMarkup;
        switch (department) {
            case OPERATOR:
                inlineMarkup = getOperatorButtons(order);
                break;
            case ADMINISTRATOR:
                inlineMarkup = getAdministratorButtons();
                break;
            case DEVELOPER:
                inlineMarkup = getDeveloperButtons();
                break;
            case COURIER:
                inlineMarkup = getCourierButtons();
                break;
            default:
                inlineMarkup = new InlineKeyboardMarkup();
                break;
        }
        return inlineMarkup;
    }

    public InlineKeyboardMarkup getOperatorButtons(Order order){
        String orderState = order.getOrderStateList().get(order.getOrderStateList().size() - 1).getState();
        System.out.println(orderState);
        OrderEnum orderEnum = OrderEnum.GET.orderState(orderState);
        List<List<InlineKeyboardButton>> rows = new ArrayList<>();
        if (orderEnum == OrderEnum.ORDER_START_REGISTRATION) {
            rows.add(List.of(getInlineButtons(accept, order.getId() + "o+")));
            rows.add(List.of(getInlineButtons(cancel, order.getId() + "o-")));
        }
        else if (orderEnum == OrderEnum.ORDER_ACCEPT) {
            rows.add(List.of(getInlineButtons(good, order.getId() + tagInfo)));
        }
        else if (orderEnum == OrderEnum.ORDER_CANCEL) {
            rows.add(List.of(getInlineButtons(delete, order.getId() + tagInfo)));
        }
        return new InlineKeyboardMarkup(rows);
    }


    public InlineKeyboardMarkup getAdministratorButtons(){
        List<List<InlineKeyboardButton>> rows = new ArrayList<>();

        return new InlineKeyboardMarkup(rows);
    }

    public InlineKeyboardMarkup getDeveloperButtons(){
        List<List<InlineKeyboardButton>> rows = new ArrayList<>();

        return new InlineKeyboardMarkup(rows);
    }

    public InlineKeyboardMarkup getCourierButtons(){
        List<List<InlineKeyboardButton>> rows = new ArrayList<>();

        return new InlineKeyboardMarkup(rows);
    }

    private List<InlineKeyboardButton> getSubCountAddButtons(String count, String id) {
        InlineKeyboardButton subDish = getInlineButtons(sub, id + "m-");
        InlineKeyboardButton countDish = getInlineButtons(count, id + tagInfo);
        InlineKeyboardButton addDish = getInlineButtons(add, id + "m+");
        return List.of(subDish, countDish, addDish);
    }

    private InlineKeyboardButton getInlineButtons(String nameButton, String id){
        InlineKeyboardButton subDish = new InlineKeyboardButton(nameButton);
        subDish.setCallbackData(id);
        return subDish;
    }
}
