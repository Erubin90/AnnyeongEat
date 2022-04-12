package tech.erubin.annyeong_eat.telegramBot.module;

import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.methods.BotApiMethod;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.CallbackQuery;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.InlineKeyboardMarkup;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.ReplyKeyboard;
import tech.erubin.annyeong_eat.entity.*;
import tech.erubin.annyeong_eat.service.*;
import tech.erubin.annyeong_eat.telegramBot.AnnyeongEatWebHook;
import tech.erubin.annyeong_eat.telegramBot.abstractClass.AbstractModule;
import tech.erubin.annyeong_eat.telegramBot.buttons.InlineButtons;
import tech.erubin.annyeong_eat.telegramBot.buttons.ReplyButtons;
import tech.erubin.annyeong_eat.telegramBot.enums.Departments;
import tech.erubin.annyeong_eat.telegramBot.enums.EmployeeStates;
import tech.erubin.annyeong_eat.telegramBot.enums.OrderStates;

import java.util.*;
import java.util.stream.Collectors;

@Component
public class OperatorModule extends AbstractModule {
    private final ReplyButtons replyButtons;
    private final InlineButtons inlineButtons;
    private final EmployeeStateServiceImpl employeeStateService;
    private final ChequeDishServiceImpl chequeDishService;
    private final DishServiceImpl dishService;

    public OperatorModule(OrderServiceImpl orderService, UserServiceImpl userService,
                          ClientStatesServiceImpl userStatesService, OrderStatesServiceImpl orderStatesService,
                          EmployeeServiceImpl employeeService, @Lazy AnnyeongEatWebHook webHook,
                          ReplyButtons replyButtons, InlineButtons inlineButtons,
                          EmployeeStateServiceImpl employeeStateService, ChequeDishServiceImpl chequeDishService,
                          DishServiceImpl dishService) {
        super(orderService, userService, userStatesService, orderStatesService, employeeService, webHook);
        this.replyButtons = replyButtons;
        this.inlineButtons = inlineButtons;
        this.employeeStateService = employeeStateService;
        this.chequeDishService = chequeDishService;
        this.dishService = dishService;
    }

    public SendMessage mainMenu(Update update, User user, String sourceText) {
        String text;
        ReplyKeyboard replyKeyboard;
        String[] textArray = sourceText.split("\n");
        if (textArray.length > 1 || textArray[0].matches("((Заказ)|З|з):.+")) {
            text = getTextEditingOrder(textArray);
            replyKeyboard = replyButtons.operatorMainMenu();
        }
        else if (sourceText.equals(replyButtons.getForm())) {
            text = getForm();
            replyKeyboard = replyButtons.operatorMainMenu();
        }
        else if (sourceText.equals(replyButtons.getCreateOrder())) {
            List<Cafe> cafeId = employeeService.getCafeByUserId(user);
            if (cafeId.size() > 1) {
                text = choosingCafe;
                List<String> cafeNames = cafeId.stream().map(Cafe::getName).collect(Collectors.toList());
                replyKeyboard = replyButtons.userOrderCafe(cafeNames);
                employeeStateService.createAndSave(user, EmployeeStates.OPERATOR_CHOOSING_CAFE.getState());
            }
            else {
                text = choosingTable;
                Cafe cafe = cafeId.get(0);
                Order order = orderService.getOrderByUserIdAndCafeId(user, cafe);
                order.setUsing(1);
                replyKeyboard = replyButtons.operatorChoosingTable(cafe);
                orderService.save(order);
                orderStatesService.createAndSave(order, OrderStates.START_REGISTRATION.getState());
                employeeStateService.createAndSave(user, EmployeeStates.CHOOSING_TABLE.getState());
            }
        }
        else {
            text = noCommand;
            replyKeyboard = replyButtons.operatorMainMenu();
        }
        return message(update, replyKeyboard, text);
    }

    private String getTextEditingOrder(String[] textArray) {
        Map<String, String> map = getMap(textArray);
        System.out.println("map\n" + map);
        Order order = orderService.getOrderByOrderName(map.get("з"));
        String text;
        if (order != null) {
            OrderState orderState = order.getOrderStateList().get(order.getOrderStateList().size() - 1);
            if (map.size() > 1) {
                if (OrderStates.isOrderEditing(orderState)) {
                    String address = map.get("а");
                    String priceDelivery = map.get("д");
                    String paymentMethod = map.get("о");
                    String comment = map.get("к");

                    StringBuilder builder = new StringBuilder();
                    builder.append("В заказе \"")
                            .append(order.getOrderName())
                            .append("\" изменен(ы):");

                    if ((address != null) && !address.isBlank()) {
                        order.setAddress(address);
                        builder.append("\n- адрес");
                    }
                    if ((priceDelivery != null) && priceDelivery.matches("\\d{1,4}")) {
                        order.setPriceDelivery(Integer.parseInt(priceDelivery));
                        builder.append("\n- сумма доставки");
                    }
                    if ((paymentMethod != null) && !paymentMethod.isBlank()) {
                        order.setPaymentMethod(paymentMethod);
                        builder.append("\n- способ оплаты");
                    }
                    if ((comment != null) && !comment.isBlank()) {
                        order.setComment(comment);
                        builder.append("\n- комментарий");
                    }
                    orderService.save(order);
                    text = builder.toString();
                }
                else {
                    text = noEditingOrder;
                }
            }
            else {
                text = "Форма редактирования заполнена не полностью";
            }
        }
        else {
            text = noOrderName;
        }
        return text;
    }

    private Map<String, String> getMap (String[] textArray) {
        Map<String, String> map = new HashMap<>();
        for (String rows : textArray) {
            String[] kayValue = rows.split(":[ ]*");
            if (kayValue.length > 1) {
                if (rows.matches("((Заказ)|З|з):.+"))
                    map.put("з", kayValue[1]);
                else if (rows.matches("((Адрес)|А|а):.+"))
                    map.put("а", kayValue[1]);
                else if (rows.matches("((Сумма доставки)|Д|д):.+"))
                    map.put("д", kayValue[1]);
                else if (rows.matches("((Способ оплаты)|О|о):.+"))
                    map.put("о", kayValue[1]);
                else if (rows.matches("((Комментарий)|К|к):.+"))
                    map.put("к", kayValue[1]);
            }
        }
        return map;
    }

    private String getForm() {
        return "Заказ: заказ \n" +
                "Адрес: \n" +
                "Сумма доставки: \n" +
                "Способ оплаты: \n" +
                "Комментарий: \n" ;
    }

    public SendMessage choosingTable(Update update, User user, String soursText) {
        String text;
        ReplyKeyboard replyKeyboard;
        Order order = orderService.getOrderByUser(user);
        Cafe cafe = order.getCafeId();

        if (soursText.matches("\\d+")) {
            int tableNum = Integer.parseInt(soursText);
            if (tableNum > 0 && tableNum < cafe.getTableQuantity()) {
                text = cafe.getName();
                replyKeyboard = replyButtons.userOrderMenu(order);
                String obtaining = "Стол " + soursText;
                order.setObtainingMethod(obtaining);
                order.setAddress(cafe.getAddress());
                order.setPriceDelivery(0);
                order.setPhoneNumber("");
                orderService.save(order);
                employeeStateService.createAndSave(user, EmployeeStates.CAFE_MENU.getState());
            }
            else {
                text = noTable;
                replyKeyboard = replyButtons.operatorChoosingTable(cafe);
            }
        }
        else if (soursText.equals(replyButtons.getPickup())) {
            String obtaining = replyButtons.getPickup();
            text = cafe.getName();
            replyKeyboard = replyButtons.userOrderMenu(order);
            order.setObtainingMethod(obtaining);
            order.setAddress(cafe.getAddress());
            order.setPriceDelivery(0);
            order.setPhoneNumber("");
            orderService.save(order);
            employeeStateService.createAndSave(user, EmployeeStates.CAFE_MENU.getState());
        }
        else if (soursText.equals(replyButtons.getBack())) {
            List<Cafe> cafeId = employeeService.getCafeByUserId(user);
            if (cafeId.size() > 1) {
                text = choosingCafe;
                List<String> cafeNames = cafeId.stream().map(Cafe::getName).collect(Collectors.toList());
                replyKeyboard = replyButtons.userOrderCafe(cafeNames);
                employeeStateService.createAndSave(user, EmployeeStates.OPERATOR_CHOOSING_CAFE.getState());
            }
            else {
                text = operatorMainMenu;
                replyKeyboard = replyButtons.operatorMainMenu();
                employeeStateService.createAndSave(user, EmployeeStates.OPERATOR_MAIN_MENU.getState());
            }
            orderService.save(order);
        }
        else {
            text = putButton;
            replyKeyboard = replyButtons.operatorChoosingTable(cafe);
        }
        return message(update, replyKeyboard, text);
    }

    public BotApiMethod<?> callbackOperatorMainMenu(CallbackQuery callback, Order order, Dish dish, String tag) {
        OrderStates orderStates = getOrderEnum(order);
        switch (orderStates) {
            case END_REGISTRATION:
                return callbackOrderAndRegistration(callback, order, tag);
            case EDITING:
                return callbackOrderEditing(callback, order, dish, tag);
            case ACCEPT:
                return callbackOrderAccept(callback, order, tag);
            case START_DELIVERY:
                return callbackOrderStartDelivery(callback, order, tag);
            case END_DELIVERY:
                return answerCallbackQuery(callback, inlineButtons.getOrderDelivered());
            case CANCEL:
                return answerCallbackQuery(callback, inlineButtons.getCancel());
            default:
                return answerCallbackQuery(callback, notWork);
        }
    }

    private BotApiMethod<?> callbackOrderAndRegistration(CallbackQuery callback, Order order, String tag) {
        BotApiMethod<?> botApiMethod;
        OrderState orderState;
        InlineKeyboardMarkup inlineMarkup;
        switch (tag) {
            case "o-":
                orderState = orderStatesService.create(order, OrderStates.CANCEL.getState());
                orderStatesService.save(orderState);
                inlineMarkup = inlineButtons.orderCancelButtons();
                botApiMethod = editMessageReplyMarkup(callback, inlineMarkup);
                break;
            case "or":
                String text = getChequeText(order, true);
                inlineMarkup = inlineButtons.orderAndRegistrationButtons(order);
                botApiMethod = editMessageText(callback, text, inlineMarkup);
                break;
            case "oe":
                orderState = orderStatesService.create(order, OrderStates.EDITING.getState());
                orderStatesService.save(orderState);
                inlineMarkup = inlineButtons.orderEditButtons(order);
                botApiMethod = editMessageReplyMarkup(callback, inlineMarkup);
                break;
            default:
                botApiMethod = answerCallbackQuery(callback, error);
                break;
        }
        return botApiMethod;
    }

    private BotApiMethod<?> callbackOrderEditing(CallbackQuery callback, Order order, Dish dish, String tag) {
        BotApiMethod<?> botApiMethod;
        OrderState orderState;
        InlineKeyboardMarkup inlineMarkup;
        String text;
        List<String> typeDishList = replyButtons.typeDishesInCafe(order.getCafeId());
        if (tag.matches("o.")) {
            switch (tag) {
                case "o+":
                    int priceDelivery = order.getPriceDelivery();
                    String address = order.getAddress();
                    String obtainingMethod = order.getObtainingMethod();
                    boolean isCorrectAddressAndObtainingMethod = ((address.isBlank() && obtainingMethod.equals(replyButtons.getPickup())) ||
                            (!address.isBlank() && (obtainingMethod.equals(replyButtons.getTaxi()) ||
                                    obtainingMethod.equals(replyButtons.getCourier()))));
                    boolean correctFillOrder = priceDelivery > -1 && isCorrectAddressAndObtainingMethod;
                    if (correctFillOrder) {
                        orderState = orderStatesService.create(order, OrderStates.ACCEPT.getState());
                        inlineMarkup = inlineButtons.orderAcceptButtons(order);
                        sendMessageDepartment(order, Departments.COURIER);
                        botApiMethod = editMessageReplyMarkup(callback, inlineMarkup);
                        orderStatesService.save(orderState);
                    }
                    else {
                        text = getErrorText(priceDelivery, isCorrectAddressAndObtainingMethod);
                        botApiMethod = answerCallbackQuery(callback, text);
                    }
                    break;
                case "o-":
                    orderState = orderStatesService.create(order, OrderStates.CANCEL.getState());
                    inlineMarkup = inlineButtons.orderCancelButtons();
                    botApiMethod = editMessageReplyMarkup(callback, inlineMarkup);
                    orderStatesService.save(orderState);
                    break;
                case "oe":
                    inlineMarkup = inlineButtons.orderEditButtons(order);
                    botApiMethod = editMessageReplyMarkup(callback, inlineMarkup);
                    break;
                case "or":
                    inlineMarkup = inlineButtons.orderAndRegistrationButtons(order);
                    botApiMethod = editMessageReplyMarkup(callback, inlineMarkup);
                    break;
                default:
                    botApiMethod = answerCallbackQuery(callback, error);
                    break;
            }
        }
        else if (tag.matches("r.")) {
            ChequeDish chequeDish = chequeDishService.getChequeByOrderAndDish(order, dish);
            int count = chequeDish.getCountDishes();
            switch (tag) {
                case "rx":
                    count = 0;
                    chequeDishService.saveOrDeleteChequeDish(chequeDish, count);
                    text = getChequeText(order, true);
                    inlineMarkup = inlineButtons.orderEditCountDishButtons(order);
                    botApiMethod = editMessageText(callback, text, inlineMarkup);
                    break;
                case "r+":
                    count++;
                    chequeDishService.saveOrDeleteChequeDish(chequeDish, count);
                    text = getChequeText(order, true);
                    inlineMarkup = inlineButtons.orderEditCountDishButtons(order);
                    botApiMethod = editMessageText(callback, text, inlineMarkup);
                    break;
                case "r-":
                    if (count > 0) {
                        count--;
                        chequeDishService.saveOrDeleteChequeDish(chequeDish, count);
                        text = getChequeText(order, true);
                        inlineMarkup = inlineButtons.orderEditCountDishButtons(order);
                        botApiMethod = editMessageText(callback, text, inlineMarkup);
                        break;
                    }
                default:
                    botApiMethod = answerCallbackQuery(callback, error);
                    break;
            }
        }
        else if (tag.matches("e.+")) {
            switch (tag) {
                case "ead":
                    inlineMarkup = inlineButtons.orderAddDishButtons(order);
                    botApiMethod = editMessageReplyMarkup(callback, inlineMarkup);
                    break;
                case "ec":
                    inlineMarkup = inlineButtons.orderEditCountDishButtons(order);
                    botApiMethod = editMessageReplyMarkup(callback, inlineMarkup);
                    break;
                case "eb":
                    inlineMarkup = inlineButtons.orderEditButtons(order);
                    botApiMethod = editMessageReplyMarkup(callback, inlineMarkup);
                    break;
                case "er":
                    text = getChequeText(order, true);
                    inlineMarkup = inlineButtons.orderEditButtons(order);
                    botApiMethod = editMessageText(callback, text, inlineMarkup);
                    break;
                default:
                    botApiMethod = answerCallbackQuery(callback, error);
                    break;
            }
        }
        else if (tag.equals("cur")) {
            order.setObtainingMethod(replyButtons.getCourier());
            orderService.save(order);
            inlineMarkup = inlineButtons.orderEditButtons(order);
            botApiMethod = editMessageReplyMarkup(callback, inlineMarkup);
        }
        else if (tag.equals("tax")) {
            order.setObtainingMethod(replyButtons.getTaxi());
            orderService.save(order);
            inlineMarkup = inlineButtons.orderEditButtons(order);
            botApiMethod = editMessageReplyMarkup(callback, inlineMarkup);
        }
        else if (dish != null) {
            ChequeDish chequeDish = chequeDishService.getChequeByOrderAndDish(order, dish);
            chequeDish.setCountDishes(1);
            chequeDishService.save(chequeDish);
            text = getChequeText(order, true);
            List<Dish> dishList = dishService.getDishListByType(dish.getType());
            inlineMarkup = inlineButtons.orderTypeDishesButtons(order, dishList);
            botApiMethod = editMessageText(callback, text, inlineMarkup);
        }
        else if (typeDishList.contains(tag)) {
            List<Dish> dishList = dishService.getDishListByType(tag);
            inlineMarkup = inlineButtons.orderTypeDishesButtons(order, dishList);
            botApiMethod = editMessageReplyMarkup(callback, inlineMarkup);
        }
        else {
            botApiMethod = answerCallbackQuery(callback, error);
        }
        return botApiMethod;
    }

    private BotApiMethod<?> callbackOrderAccept(CallbackQuery callback, Order order, String tag) {
        BotApiMethod<?> botApiMethod;
        InlineKeyboardMarkup inlineMarkup;
        if (tag.matches("c\\d+")) {
            System.out.println(tag.substring(1));
            order.setDeliveryId(Integer.parseInt(tag.substring(1)));
            orderService.save(order);
            inlineMarkup = inlineButtons.orderStartDelivery(order);
            botApiMethod = editMessageReplyMarkup(callback, inlineMarkup);
        }
        else if (tag.equals("tds")) {
            inlineMarkup = inlineButtons.orderStartDelivery(order);
            botApiMethod = editMessageReplyMarkup(callback, inlineMarkup);
            orderStatesService.createAndSave(order ,OrderStates.START_DELIVERY.getState());
        }
        else {
            botApiMethod = answerCallbackQuery(callback, putButton);
        }
        return  botApiMethod;
    }

    private BotApiMethod<?> callbackOrderStartDelivery(CallbackQuery callback, Order order, String tag) {
        BotApiMethod<?> botApiMethod;
        InlineKeyboardMarkup inlineMarkup;
        if (tag.equals("end")) {
            inlineMarkup = inlineButtons.orderEndDelivery();
            botApiMethod = editMessageReplyMarkup(callback, inlineMarkup);
            orderStatesService.createAndSave(order, OrderStates.END_DELIVERY.getState());
        }
        else {
            botApiMethod = answerCallbackQuery(callback, putButton);
        }
        return botApiMethod;
    }

    private String getErrorText(int pD, boolean b) {
        List<String> textError = new ArrayList<>();
        if (!(pD > -1)) {
            textError.add("не указана стоимость доставки");
        }
        if (b) {
            textError.add("не правильный адрес или статус заказа");
        }
        ListIterator<String> listIterator = textError.listIterator();
        StringBuilder stringBuilder = new StringBuilder();
        while (listIterator.hasNext()) {
            String s = listIterator.next();
            stringBuilder.append(s);
            if  (listIterator.hasNext()) {
                stringBuilder.append(", ");
            }
        }
        return stringBuilder.toString();
    }
}