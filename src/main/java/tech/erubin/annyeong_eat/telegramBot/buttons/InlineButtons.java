package tech.erubin.annyeong_eat.telegramBot.buttons;

import lombok.AllArgsConstructor;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.InlineKeyboardMarkup;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.InlineKeyboardButton;
import tech.erubin.annyeong_eat.entity.*;
import tech.erubin.annyeong_eat.service.EmployeeServiceImpl;
import tech.erubin.annyeong_eat.telegramBot.abstractClass.AbstractButton;
import tech.erubin.annyeong_eat.telegramBot.enums.DepartmentEnum;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Component
@AllArgsConstructor
public class InlineButtons extends AbstractButton {
    private final EmployeeServiceImpl employeeService;

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
        String dishName = chequeDish.getDishId().getName();
        rows.add(getSubCountAddButtons(count, orderId, dishName, "m"));
        rows.add(List.of(getInlineButtons(delete, orderId, dishName, "mx")));
        return new InlineKeyboardMarkup(rows);
    }

    public InlineKeyboardMarkup typeDishesMenu(Order order, List<Dish> dishList) {
        List<List<InlineKeyboardButton>> rows = new ArrayList<>();
        if (dishList != null && !dishList.isEmpty()) {
            for (Dish dish : dishList) {
                List<InlineKeyboardButton> buttons = new ArrayList<>();
                String nameButton = dish.getName() + " " + dish.getPrice() + "₽";
                buttons.add(getInlineButtons(nameButton, order.getId(), dish.getName(), String.valueOf(dish.getId())));
                rows.add(buttons);
            }
        }
        return new InlineKeyboardMarkup(rows);
    }

    public InlineKeyboardMarkup fullOrderButtons(Order order) {
        List<List<InlineKeyboardButton>> rows = getEditOrderButtons(order, "b");
        return new InlineKeyboardMarkup(rows);
    }

    public InlineKeyboardMarkup employeeButtons(DepartmentEnum department, Order order) {
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
        rows.add(List.of(getInlineButtons(editOrder, order.getId(), "oe")));
        rows.add(List.of(getInlineButtons(restart, order.getId(), "or")));
        rows.add(List.of(getInlineButtons(accept, order.getId(), "o+")));
        rows.add(List.of(getInlineButtons(cancel, order.getId(), "o-")));
        return new InlineKeyboardMarkup(rows);
    }

    public InlineKeyboardMarkup orderEditButtons(Order order) {
        List<List<InlineKeyboardButton>> rows = new ArrayList<>();
        rows.add(List.of(getInlineButtons(addDishes, order.getId(), "ead")));
        rows.add(List.of(getInlineButtons(changeQuantity, order.getId(), "ec")));
        String obtaining = order.getObtainingMethod();
        if (obtaining.equals(delivery)) {
            List<User> courierList = employeeService.getCourierIsFree(order.getCafeId());
            if  (!courierList.isEmpty()) {
                rows.add(List.of(getInlineButtons(courier + "ом", order.getId(), "cur")));
            }
            rows.add(List.of(getInlineButtons(taxi, order.getId(), "tax")));
        }
        rows.add(List.of(getInlineButtons(restart, order.getId(), "er")));
        rows.add(List.of(getInlineButtons(accept, order.getId(), "o+")));
        rows.add(List.of(getInlineButtons(cancel, order.getId(), "o-")));
        return new InlineKeyboardMarkup(rows);
    }

    public InlineKeyboardMarkup orderAddDishButtons(Order order) {
        List<List<InlineKeyboardButton>> rows = new ArrayList<>();
        List<String> typeDishList = typeDishesInCafe(order.getCafeId());
        for (String typeDish : typeDishList) {
            rows.add(List.of(getInlineButtons(typeDish, order.getId(), typeDish)));
        }
        rows.add(List.of(getInlineButtons(back, order.getId(), "eb")));
        return new InlineKeyboardMarkup(rows);
    }

    public InlineKeyboardMarkup orderEditCountDishButtons(Order order) {
        List<List<InlineKeyboardButton>> rows = getEditOrderButtons(order, "r");
        rows.add(List.of(getInlineButtons(back, order.getId(), "eb")));
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
            buttons.add(getInlineButtons(nameButton, order.getId(), dish.getName(), String.valueOf(dish.getId())));
            rows.add(buttons);
        }
        rows.add(List.of(getInlineButtons(back, order.getId(), "ead")));
        return new InlineKeyboardMarkup(rows);
    }

    public InlineKeyboardMarkup orderAcceptButtons(Order order) {
        List<List<InlineKeyboardButton>> buttonNames = new ArrayList<>();
        buttonNames.add(List.of(getInlineButtons(acceptState, 0, tagInfo)));
        if (DepartmentEnum.GET.isCourier(order.getObtainingMethod())) {
            List<User> userList = employeeService.getCourierIsFree(order.getCafeId());
            for (User user : userList) {
                buttonNames.add(List.of(getInlineButtons(user.getName(), order.getId(), Integer.toString(user.getId()))));
            }
        }
        return new InlineKeyboardMarkup(buttonNames);
    }

    public InlineKeyboardMarkup orderCancelButtons() {
        return new InlineKeyboardMarkup(List.of(List.of(getInlineButtons(cancelState, 0, tagInfo))));
    }

    private List<List<InlineKeyboardButton>> getEditOrderButtons(Order order, String tag) {
        List<List<InlineKeyboardButton>> rows = new ArrayList<>();
        if (order.getChequeDishList() != null && !order.getChequeDishList().isEmpty()) {
            int count = 1;
            for (ChequeDish chequeDish : order.getChequeDishList()) {
                List<InlineKeyboardButton> buttonList = new ArrayList<>();
                Dish dish = chequeDish.getDishId();
                buttonList.add(getInlineButtons(String.valueOf(count++), order.getId(), dish.getName(), String.valueOf(dish.getId())));
                buttonList.add(getInlineButtons(delete, order.getId(), dish.getName(), tag + "x"));
                buttonList.addAll(getSubCountAddButtons(String.valueOf(chequeDish.getCountDishes()), order.getId(), dish.getName(), tag));
                rows.add(buttonList);
            }
        }
        return rows;
    }

    private List<InlineKeyboardButton> getSubCountAddButtons(String count, int orderId, String dishName, String tag) {
        InlineKeyboardButton subDish = getInlineButtons(sub, orderId, dishName,tag + "-");
        InlineKeyboardButton countDish = getInlineButtons(count, orderId, dishName, tagInfo);
        InlineKeyboardButton addDish = getInlineButtons(add, orderId, dishName, tag + "+");
        return List.of(subDish, countDish, addDish);
    }

    private InlineKeyboardButton getInlineButtons(String nameButton, int orderId, String dishName, String tag){
        InlineKeyboardButton subDish = new InlineKeyboardButton(nameButton);
        subDish.setCallbackData(genTag(orderId, dishName, tag));
        return subDish;
    }

    private InlineKeyboardButton getInlineButtons(String nameButton, int orderId, String tag){
        InlineKeyboardButton subDish = new InlineKeyboardButton(nameButton);
        subDish.setCallbackData(genTag(orderId, tag));
        return subDish;
    }

    private String genTag(int orderId, String dishName, String tag) {
        return orderId + "/" + dishName + "/" + tag;
    }

    private String genTag(int orderId, String tag) {
        return orderId + "//" + tag;
    }
}
