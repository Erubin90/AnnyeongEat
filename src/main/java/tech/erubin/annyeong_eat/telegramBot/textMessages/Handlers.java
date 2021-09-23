package tech.erubin.annyeong_eat.telegramBot.textMessages;

import lombok.Getter;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.PropertySource;
import org.springframework.stereotype.Component;
import tech.erubin.annyeong_eat.entity.Dish;
import tech.erubin.annyeong_eat.service.*;

@Getter
@Component
@PropertySource(value = "classpath:messages.properties", encoding = "UTF-8")
public abstract class Handlers {

    @Value("${handler.message.error}")
    protected String error;

    @Value("${handler.message.addDish}")
    protected String addDish;

    @Value("${handler.message.subDish}")
    protected String subDish;

    @Value("${handler.message.emptyDish}")
    protected String emptyDish;

    @Value("${handler.message.notWork}")
    protected String notWork;

    @Value("${regular.errorTrigger}")
    protected String errorTrigger;

    //Error Messages
    @Value("${message.error.obsceneWord}")
    protected String errorObsceneWord;

    @Value("${message.error.falseTextForm}")
    protected String errorFalseTextForm;

    @Value("${message.error.number}")
    protected String errorNumber;

    @Value("${message.error.noCorrectChar}")
    protected String errorNoCorrectChar;

    @Value("${message.error.bigLength}")
    protected String errorBigLength;

    @Value("${message.error.littleLength}")
    protected String errorLittleLength;

    @Value("${message.error.formatPhoneNumber}")
    protected String errorFormatPhoneNumber;

    //noError Message's
    @Value("${registration.message.name}")
    protected String nameNoError;

    @Value("${registration.message.surname}")
    protected String surnameNoError;

    @Value("${registration.message.phoneNumber}")
    protected String phoneNumberNoError;

    @Value("${registration.message.city}")
    protected String cityNoError;

    @Value("${address.noError}")
    protected String addressNoError;

    @Value("${button.tag.info}")
    protected String tagInfo;

    @Value("${message.tag.info}")
    protected String messageInfo;

    protected final UserServiceImpl clientService;
    protected final OrderServiceImpl orderService;
    protected final OrderStatesServiceImpl orderStatesService;
    protected final DishServiceImpl dishService;
    protected final ChequeDishServiceImpl chequeService;
    protected final UserStatesServiceImpl stateService;
    protected final CafeServiceImpl cafeService;

    public Handlers(UserServiceImpl clientService, OrderServiceImpl orderService,
                    OrderStatesServiceImpl orderStatesService, DishServiceImpl dishService,
                    ChequeDishServiceImpl chequeService, UserStatesServiceImpl stateService,
                    CafeServiceImpl cafeService) {
        this.clientService = clientService;
        this.orderService = orderService;
        this.orderStatesService = orderStatesService;
        this.dishService = dishService;
        this.chequeService = chequeService;
        this.stateService = stateService;
        this.cafeService = cafeService;
    }

    public String getTextDish(Dish dish) {
        String dishName = dish.getName();
        double dishPrice = dish.getPrice();
        String dishComment = dish.getComment();
        return String.format("%s %s₽\n%s", dishName, dishPrice, dishComment);
    }
}
