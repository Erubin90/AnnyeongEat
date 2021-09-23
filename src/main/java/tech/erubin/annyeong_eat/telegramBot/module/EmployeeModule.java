package tech.erubin.annyeong_eat.telegramBot.module;

import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.methods.BotApiMethod;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Update;
import tech.erubin.annyeong_eat.entity.Department;
import tech.erubin.annyeong_eat.entity.User;
import tech.erubin.annyeong_eat.entity.UserState;
import tech.erubin.annyeong_eat.service.OrderServiceImpl;
import tech.erubin.annyeong_eat.service.OrderStatesServiceImpl;
import tech.erubin.annyeong_eat.service.UserServiceImpl;
import tech.erubin.annyeong_eat.service.UserStatesServiceImpl;
import tech.erubin.annyeong_eat.telegramBot.buttons.ReplyButtons;
import tech.erubin.annyeong_eat.telegramBot.enums.EmployeeEnum;
import tech.erubin.annyeong_eat.telegramBot.enums.UserEnum;
import tech.erubin.annyeong_eat.telegramBot.textMessages.Module;

import java.util.List;
import java.util.stream.Collectors;

@Component
public class EmployeeModule extends Module {
    private final ReplyButtons replyButtons;

    public EmployeeModule(OrderServiceImpl orderService, UserServiceImpl userService,
                          UserStatesServiceImpl userStatesService, OrderStatesServiceImpl orderStatesService,
                          ReplyButtons replyButtons) {
        super(orderService, userService, userStatesService, orderStatesService);
        this.replyButtons = replyButtons;
    }

    public BotApiMethod<?> start(Update update, User user, UserEnum employeeStateEnum) {
        String chatId = update.getMessage().getChatId().toString();
        SendMessage sendMessage = new SendMessage();
        sendMessage.setChatId(chatId);
        String sourceText = update.getMessage().getText();
        String text = error;
        UserState userState = null;
        List<String> departmentList = user.getDepartmentsList()
                .stream()
                .map(Department::getName)
                .collect(Collectors.toList());

        switch (employeeStateEnum) {
            case CHOICE_DEPARTMENT:
                if (replyButtons.choiceDepartmentButtons(departmentList).contains(sourceText)) {
                    EmployeeEnum employeeState = EmployeeEnum.GET.department(sourceText);
                    if (employeeState == EmployeeEnum.CLIENT) {
                        text = nextMainMenu;
                        userState = userStatesService.create(user, UserEnum.MAIN_MENU.getValue());
                        sendMessage.setReplyMarkup(replyButtons.userMainMenu(user));
                    }
                    else if (EmployeeEnum.GET.isEmployee(employeeState)) {
                        text = nextMainMenu;
                        userState = userStatesService.create(user, employeeState.getValue());
                        sendMessage.setReplyMarkup(replyButtons.employeeMainMenu(employeeState));
                    }
                }
                else {
                    text = nextMainMenu;
                    sendMessage.setReplyMarkup(replyButtons.choiceDepartment(departmentList));
                }
                break;
            case OPERATOR:
                if (sourceText.equals(replyButtons.getExit())) {
                    text = choiceDepartment;
                    sendMessage.setReplyMarkup(replyButtons.choiceDepartment(departmentList));
                    userState = userStatesService.create(user, UserEnum.CHOICE_DEPARTMENT.getValue());
                }
                else {
                    text = putButton;
                    sendMessage.setReplyMarkup(replyButtons.employeeMainMenu(EmployeeEnum.OPERATOR));
                }
                break;
            case ADMINISTRATOR:
                if (sourceText.equals(replyButtons.getExit())) {
                    text = choiceDepartment;
                    sendMessage.setReplyMarkup(replyButtons.choiceDepartment(departmentList));
                    userState = userStatesService.create(user, UserEnum.CHOICE_DEPARTMENT.getValue());
                }
                else {
                    text = putButton;
                    sendMessage.setReplyMarkup(replyButtons.employeeMainMenu(EmployeeEnum.ADMINISTRATOR));
                }
                break;
            case COURIER:
                if (sourceText.equals(replyButtons.getExit())) {
                    text = choiceDepartment;
                    sendMessage.setReplyMarkup(replyButtons.choiceDepartment(departmentList));
                    userState = userStatesService.create(user, UserEnum.CHOICE_DEPARTMENT.getValue());
                }
                else {
                    text = putButton;
                    sendMessage.setReplyMarkup(replyButtons.employeeMainMenu(EmployeeEnum.COURIER));
                }
                break;
            case DEVELOPER:
                if (sourceText.equals(replyButtons.getExit())) {
                    text = choiceDepartment;
                    sendMessage.setReplyMarkup(replyButtons.choiceDepartment(departmentList));
                    userState = userStatesService.create(user, UserEnum.CHOICE_DEPARTMENT.getValue());
                }
                else {
                    text = putButton;
                    sendMessage.setReplyMarkup(replyButtons.employeeMainMenu(EmployeeEnum.DEVELOPER));
                }
                break;
        }
        return sendMessage(sendMessage, userState, text);
    }
}
