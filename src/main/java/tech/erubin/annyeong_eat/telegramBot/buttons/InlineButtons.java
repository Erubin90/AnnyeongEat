package tech.erubin.annyeong_eat.telegramBot.buttons;

import lombok.NoArgsConstructor;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.InlineKeyboardMarkup;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.InlineKeyboardButton;
import tech.erubin.annyeong_eat.entity.ChequeDish;
import tech.erubin.annyeong_eat.entity.Dish;
import tech.erubin.annyeong_eat.entity.Order;
import tech.erubin.annyeong_eat.telegramBot.enums.EmployeeEnum;
import tech.erubin.annyeong_eat.telegramBot.textMessages.Buttons;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Component
@NoArgsConstructor
public class InlineButtons extends Buttons {

    public InlineKeyboardMarkup clientCheque(ChequeDish chequeDish) {
        String count;
        if (chequeDish == null) {
            count = "0";
        }
        else {
            count = String.valueOf(chequeDish.getCountDishes());
        }
        List<List<InlineKeyboardButton>> rows = new ArrayList<>();
        int orderId = chequeDish.getOrderId().getId();
        int dishId = chequeDish.getDishId().getId();
        rows.add(getSubCountAddButtons(count, orderId, dishId, "m"));
        rows.add(List.of(getInlineButtons(delete, orderId, dishId, "mx")));
        return new InlineKeyboardMarkup(rows);
    }

    public InlineKeyboardMarkup typeDishesMenu(Order order, List<Dish> dishList) {
        List<List<InlineKeyboardButton>> rows = new ArrayList<>();
        if (dishList != null && !dishList.isEmpty()) {
            for (Dish dish : dishList) {
                List<InlineKeyboardButton> buttons = new ArrayList<>();
                String nameButton = dish.getName() + " " + dish.getPrice() + "₽";
                buttons.add(getInlineButtons(nameButton, order.getId(), dish.getId(), String.valueOf(dish.getId())));
                rows.add(buttons);
            }
        }
        return new InlineKeyboardMarkup(rows);
    }

    public InlineKeyboardMarkup fullOrderButtons(Order order) {
        List<List<InlineKeyboardButton>> rows = getEditOrderButtons(order, "b");
        return new InlineKeyboardMarkup(rows);
    }

    public InlineKeyboardMarkup employeeButtons(EmployeeEnum department, Order order) {
        InlineKeyboardMarkup inlineMarkup;
        switch (department) {
            case OPERATOR:
                inlineMarkup = orderAndRegistrationButtons(order);
                break;
//            case ADMINISTRATOR:
//                inlineMarkup = getAdministratorButtons();
//                break;
//            case DEVELOPER:
//                inlineMarkup = getDeveloperButtons();
//                break;
//            case COURIER:
//                inlineMarkup = getCourierButtons();
//                break;
            default:
                inlineMarkup = new InlineKeyboardMarkup();
                break;
        }
        return inlineMarkup;
    }

    public InlineKeyboardMarkup orderAndRegistrationButtons(Order order) {
        List<List<InlineKeyboardButton>> rows = new ArrayList<>();
        rows.add(List.of(getInlineButtons(editOrder, order.getId(), 0, "oe")));
        rows.add(List.of(getInlineButtons(restart, order.getId(), 0, "or")));
        rows.add(List.of(getInlineButtons(accept, order.getId(), 0, "o+")));
        rows.add(List.of(getInlineButtons(cancel, order.getId(), 0, "o-")));
        return new InlineKeyboardMarkup(rows);
    }

    public InlineKeyboardMarkup orderEditButtons(Order order) {
        List<List<InlineKeyboardButton>> rows = new ArrayList<>();
        rows.add(List.of(getInlineButtons(addDishes, order.getId(), 0, "ead")));
        rows.add(List.of(getInlineButtons(changeQuantity, order.getId(), 0, "ec")));
        rows.add(List.of(getInlineButtons(restart, order.getId(), 0, "er")));
        rows.add(List.of(getInlineButtons(accept, order.getId(), 0, "o+")));
        rows.add(List.of(getInlineButtons(cancel, order.getId(), 0, "o-")));
        return new InlineKeyboardMarkup(rows);
    }

    public InlineKeyboardMarkup orderAddDishButtons(Order order) {
        List<List<InlineKeyboardButton>> rows = new ArrayList<>();
        List<String> typeDishList = typeDishesInCafe(order);
        for (String c : typeDishList) {
            rows.add(List.of(getInlineButtons(c, order.getId(), 0, c)));
        }
        rows.add(List.of(getInlineButtons(back, order.getId(), 0, "eb")));
        return new InlineKeyboardMarkup(rows);
    }

    public InlineKeyboardMarkup orderEditCountDishButtons(Order order) {
        List<List<InlineKeyboardButton>> rows = getEditOrderButtons(order, "r");
        rows.add(List.of(getInlineButtons(back, order.getId(), 0, "eb")));
        return new InlineKeyboardMarkup(rows);
    }

    public InlineKeyboardMarkup orderTypeDishesButtons(Order order, List<Dish> dishList) {
        List<List<InlineKeyboardButton>> rows = new ArrayList<>();
        List<Dish> remove = order.getChequeDishList()
                .stream()
                .map(ChequeDish::getDishId)
                .collect(Collectors.toList());
        dishList.removeAll(remove);
        for (Dish dish : dishList) {
            List<InlineKeyboardButton> buttons = new ArrayList<>();
            String nameButton = dish.getName();
            buttons.add(getInlineButtons(nameButton, order.getId(), dish.getId(), String.valueOf(dish.getId())));
            rows.add(buttons);
        }
        rows.add(List.of(getInlineButtons(back, order.getId(), 0, "ead")));
        return new InlineKeyboardMarkup(rows);
    }

    public InlineKeyboardMarkup orderAcceptButtons() {
        return new InlineKeyboardMarkup(List.of(List.of(getInlineButtons(acceptState, 0, 0, tagInfo))));
    }

    public InlineKeyboardMarkup orderCancelButtons() {
        return new InlineKeyboardMarkup(List.of(List.of(getInlineButtons(cancelState, 0, 0, tagInfo))));
    }

    private List<List<InlineKeyboardButton>> getEditOrderButtons(Order order, String tag) {
        List<List<InlineKeyboardButton>> rows = new ArrayList<>();
        if (order.getChequeDishList() != null && !order.getChequeDishList().isEmpty()) {
            int count = 1;
            for (ChequeDish chequeDish : order.getChequeDishList()) {
                List<InlineKeyboardButton> buttonList = new ArrayList<>();
                Dish dish = chequeDish.getDishId();
                buttonList.add(getInlineButtons(String.valueOf(count++), order.getId(), dish.getId(), String.valueOf(dish.getId())));
                buttonList.add(getInlineButtons(delete, order.getId(), dish.getId(), tag + "x"));
                buttonList.addAll(getSubCountAddButtons(String.valueOf(chequeDish.getCountDishes()), order.getId(), dish.getId(), tag));
                rows.add(buttonList);
            }
        }
        return rows;
    }

    private List<InlineKeyboardButton> getSubCountAddButtons(String count, int orderId, int dishId, String tag) {
        InlineKeyboardButton subDish = getInlineButtons(sub, orderId, dishId,tag + "-");
        InlineKeyboardButton countDish = getInlineButtons(count, orderId, dishId, tagInfo);
        InlineKeyboardButton addDish = getInlineButtons(add, orderId, dishId, tag + "+");
        return List.of(subDish, countDish, addDish);
    }

    private InlineKeyboardButton getInlineButtons(String nameButton, int orderId, int dishId, String tag){
        InlineKeyboardButton subDish = new InlineKeyboardButton(nameButton);
        subDish.setCallbackData(genTag(orderId, dishId, tag));
        return subDish;
    }

    private String genTag(int orderId, int dishId, String tag) {
        return orderId + "/" + dishId + "/" + tag;
    }
}
