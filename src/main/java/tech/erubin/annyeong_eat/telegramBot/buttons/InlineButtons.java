package tech.erubin.annyeong_eat.telegramBot.buttons;

import lombok.AllArgsConstructor;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.InlineKeyboardMarkup;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.InlineKeyboardButton;
import tech.erubin.annyeong_eat.entity.*;
import tech.erubin.annyeong_eat.service.EmployeeServiceImpl;
import tech.erubin.annyeong_eat.service.OrderServiceImpl;
import tech.erubin.annyeong_eat.service.UserServiceImpl;
import tech.erubin.annyeong_eat.telegramBot.abstractClass.AbstractButton;
import tech.erubin.annyeong_eat.telegramBot.enums.Departments;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Component
@AllArgsConstructor
public class InlineButtons extends AbstractButton {
    private final EmployeeServiceImpl employeeService;
    private final OrderServiceImpl orderService;
    private final UserServiceImpl userService;

    public InlineKeyboardMarkup checkOrderMainMenu(User user) {
        var orderList = orderService.getOrdersInProgress(user);
        var rows = new ArrayList<List<InlineKeyboardButton>>();
        for (Order order: orderList) {
            OrderState orderState = order.getOrderStateList().get(order.getOrderStateList().size() - 1);
            var line = new ArrayList<InlineKeyboardButton>();
            line.add(getButtonLine(order.getOrderName(), order.getId(), order.getOrderName()));
            line.add(getButtonLine(orderState.getState(), 0, tagInfo));
            rows.add(line);
        }
        rows.add(getButtonRow(restart, 1, restart));
        return new InlineKeyboardMarkup(rows);
    }

    public InlineKeyboardMarkup clientCheque(ChequeDish chequeDish) {
        String count;
        if (chequeDish == null) {
            count = "0";
        }
        else {
            count = String.valueOf(chequeDish.getCountDishes());
        }
        var rows = new ArrayList<List<InlineKeyboardButton>>();
        int orderId = chequeDish.getOrderId().getId();
        String dishName = chequeDish.getDishId().getName();
        rows.add(getSubCountAddButtons(count, orderId, dishName, "m"));
        rows.add(getButtonRow(delete, orderId, dishName, "mx"));
        return new InlineKeyboardMarkup(rows);
    }

    public InlineKeyboardMarkup typeDishesMenu(Order order, List<Dish> dishList) {
        var rows = new ArrayList<List<InlineKeyboardButton>>();
        if (dishList != null && !dishList.isEmpty()) {
            for (Dish dish : dishList) {
                var line = new ArrayList<InlineKeyboardButton>();
                String nameButton = dish.getName() + " " + dish.getPrice() + "₽";
                line.add(getButtonLine(nameButton, order.getId(), dish.getName(), String.valueOf(dish.getId())));
                rows.add(line);
            }
        }
        return new InlineKeyboardMarkup(rows);
    }

    public InlineKeyboardMarkup fullOrderButtons(Order order) {
        return new InlineKeyboardMarkup(getEditOrderButtons(order, "b"));
    }

    public InlineKeyboardMarkup employeeButtons(Departments department, Order order) {
        InlineKeyboardMarkup inlineMarkup;
        switch (department) {
            case OPERATOR:
                inlineMarkup = orderAndRegistrationButtons(order);
                break;
            default:
                inlineMarkup = new InlineKeyboardMarkup();
                break;
        }
        return inlineMarkup;
    }

    public InlineKeyboardMarkup orderAndRegistrationButtons(Order order) {
        var rows = new ArrayList<List<InlineKeyboardButton>>();
        rows.add(getButtonRow(editOrder, order.getId(), "oe"));
        rows.add(getButtonRow(restart, order.getId(), "or"));
        rows.add(getButtonRow(cancel, order.getId(), "o-"));
        return new InlineKeyboardMarkup(rows);
    }

    public InlineKeyboardMarkup orderEditButtons(Order order) {
        var rows = new ArrayList<List<InlineKeyboardButton>>();
        rows.add(getButtonRow(addDishes, order.getId(), "ead"));
        rows.add(getButtonRow(changeQuantity, order.getId(), "ec"));
        String obtaining = order.getObtainingMethod();
        if (obtaining.equals(delivery)) {
            List<User> courierList = employeeService.getCourierIsFree(order.getCafeId());
            if  (!courierList.isEmpty()) {
                rows.add(getButtonRow(courier + "ом", order.getId(), "cur"));
            }
            rows.add(getButtonRow(taxi, order.getId(), "tax"));
        }
        rows.add(getButtonRow(restart, order.getId(), "er"));
        rows.add(getButtonRow(accept, order.getId(), "o+"));
        rows.add(getButtonRow(cancel, order.getId(), "o-"));
        return new InlineKeyboardMarkup(rows);
    }

    public InlineKeyboardMarkup orderAddDishButtons(Order order) {
        var rows = new ArrayList<List<InlineKeyboardButton>>();
        var typeDishList = typeDishesInCafe(order.getCafeId());
        for (String typeDish : typeDishList) {
            rows.add(getButtonRow(typeDish, order.getId(), typeDish));
        }
        rows.add(getButtonRow(back, order.getId(), "eb"));
        return new InlineKeyboardMarkup(rows);
    }

    public InlineKeyboardMarkup orderEditCountDishButtons(Order order) {
        var rows = getEditOrderButtons(order, "r");
        rows.add(getButtonRow(back, order.getId(), "eb"));
        return new InlineKeyboardMarkup(rows);
    }

    public InlineKeyboardMarkup orderTypeDishesButtons(Order order, List<Dish> dishList) {
        var rows = new ArrayList<List<InlineKeyboardButton>>();
        List<Dish> remove = order.getChequeDishList()
                .stream()
                .map(ChequeDish::getDishId)
                .collect(Collectors.toList());
        dishList.removeAll(remove);
        for (Dish dish : dishList) {
            var line = new ArrayList<InlineKeyboardButton>();
            String nameButton = dish.getName();
            line.add(getButtonLine(nameButton, order.getId(), dish.getName(), String.valueOf(dish.getId())));
            rows.add(line);
        }
        rows.add(getButtonRow(back, order.getId(), "ead"));
        return new InlineKeyboardMarkup(rows);
    }

    public InlineKeyboardMarkup orderAcceptButtons(Order order) {
        var rows = new ArrayList<List<InlineKeyboardButton>>();
        rows.add(getButtonRow(acceptState, 0, tagInfo));
        var departments = Departments.department(order.getObtainingMethod());
        switch (departments) {
            case COURIER:
                var userList = employeeService.getCourierIsFree(order.getCafeId());
                for (User user : userList) {
                    rows.add(getButtonRow(transferOrderCourier + user.getName(), order.getId(), "c" + user.getId()));
                }
                break;
            case TAXI:
                rows.add(getButtonRow( transferOrderTaxi, order.getId(), "tds"));
                break;
        }
        return new InlineKeyboardMarkup(rows);
    }

    public InlineKeyboardMarkup orderStartDelivery(Order order) {
        int courierId = order.getDeliveryId();
        var rows = new ArrayList<List<InlineKeyboardButton>>();
        rows.add(getButtonRow(acceptState, 0, tagInfo));
        if (courierId == 0) {
            rows.add(getButtonRow(taxiDeliveryOrder, order.getId(), "end"));
        }
        else {
            String buttonName = courierDeliveryOrder + userService.getUser(courierId).getName();
            rows.add(getButtonRow(buttonName, 0, tagInfo));
        }
        return new InlineKeyboardMarkup(rows);
    }

    public InlineKeyboardMarkup orderEndDelivery() {
        return new InlineKeyboardMarkup(List.of(getButtonRow(orderDelivered, 0, tagInfo)));
    }

    public InlineKeyboardMarkup orderCancelButtons() {
        return new InlineKeyboardMarkup(List.of(getButtonRow(cancelState, 0, tagInfo)));
    }

    public InlineKeyboardMarkup mainMenuWaiter() {
        var rows = new ArrayList<List<InlineKeyboardButton>>();
        rows.add(getButtonRow("Оформить заказ", 0,"new"));
        rows.add(getButtonRow("Дополнить заказ",0, "add"));
        rows.add(getButtonRow(back, 0, "back"));
        return new InlineKeyboardMarkup(rows);
    }

    public InlineKeyboardMarkup choiceFoodTableCreateOrder(User user) {
        return null;
    }

    private List<List<InlineKeyboardButton>> getEditOrderButtons(Order order, String tag) {
        var rows = new ArrayList<List<InlineKeyboardButton>>();
        if (order.getChequeDishList() != null && !order.getChequeDishList().isEmpty()) {
            int count = 1;
            for (ChequeDish chequeDish : order.getChequeDishList()) {
                var line = new ArrayList<InlineKeyboardButton>();
                Dish dish = chequeDish.getDishId();
                line.add(getButtonLine(String.valueOf(count++), order.getId(), dish.getName(), String.valueOf(dish.getId())));
                line.add(getButtonLine(delete, order.getId(), dish.getName(), tag + "x"));
                line.addAll(getSubCountAddButtons(String.valueOf(chequeDish.getCountDishes()), order.getId(), dish.getName(), tag));
                rows.add(line);
            }
        }
        return rows;
    }

    private List<InlineKeyboardButton> getSubCountAddButtons(String count, int orderId, String dishName, String tag) {
        InlineKeyboardButton subDish = getButtonLine(sub, orderId, dishName,tag + "-");
        InlineKeyboardButton countDish = getButtonLine(count, orderId, dishName, tagInfo);
        InlineKeyboardButton addDish = getButtonLine(add, orderId, dishName, tag + "+");
        return List.of(subDish, countDish, addDish);
    }

    private List<InlineKeyboardButton> getButtonRow(String nameButton, int orderId, String dishName, String tag) {
        return List.of(getButtonLine(nameButton, orderId, dishName, tag));
    }

    private List<InlineKeyboardButton> getButtonRow(String nameButton, int orderId, String tag) {
        return List.of(getButtonLine(nameButton, orderId, tag));
    }

    private InlineKeyboardButton getButtonLine(String nameButton, int orderId, String dishName, String tag){
        var button = new InlineKeyboardButton(nameButton);
        button.setCallbackData(getTag(orderId, dishName, tag));
        return button;
    }

    private InlineKeyboardButton getButtonLine(String nameButton, int orderId, String tag){
        var button = new InlineKeyboardButton(nameButton);
        button.setCallbackData(getTag(orderId, tag));
        return button;
    }

    private String getTag(int orderId, String dishName, String tag) {
        return orderId + "/" + dishName + "/" + tag;
    }

    private String getTag(int orderId, String tag) {
        return orderId + "//" + tag;
    }
}
