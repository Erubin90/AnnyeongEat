package tech.erubin.annyeong_eat.telegramBot.module;

import org.junit.Assert;
import org.junit.jupiter.api.Test;
import org.springframework.data.domain.Example;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import tech.erubin.annyeong_eat.telegramBot.entity.Client;
import tech.erubin.annyeong_eat.telegramBot.entity.Dish;
import tech.erubin.annyeong_eat.telegramBot.entity.DishOptionally;
import tech.erubin.annyeong_eat.telegramBot.entity.Order;
import tech.erubin.annyeong_eat.telegramBot.module.order.OrderTextMessage;
import tech.erubin.annyeong_eat.telegramBot.module.registration.RegistrationTextMessage;
import tech.erubin.annyeong_eat.telegramBot.repository.DishOptionallyRepository;
import tech.erubin.annyeong_eat.telegramBot.repository.DishRepository;
import tech.erubin.annyeong_eat.telegramBot.repository.OrderRepository;
import tech.erubin.annyeong_eat.telegramBot.service.entityServises.DishOptionallyServiceImpl;
import tech.erubin.annyeong_eat.telegramBot.service.entityServises.DishServiceImpl;
import tech.erubin.annyeong_eat.telegramBot.service.entityServises.OrderServiceImpl;

import java.util.List;
import java.util.Optional;

class CheckMessageTest {

    @Test
    void checkName() {
        CheckMessage checkMessage = getCheckMessage();
        String[] sourceText = {
                "Магомедамин",
                "Магомед Амин",
                "Магомед-амин",
                "Маго-мед амин",
                "Магомед- амин",
                "М-а-г-о-м-е-д- а-м-и-н",
                "М@г0мед@м1н",
                "Мaгoмeдaмин",
                "➡️"};
        String[] actual = {
                "ok",
                "ok",
                "ok",
                "ok",
                "error NoRussianChars ",
                "error BigLength 20 error NoRussianChars ",
                "error Number error NoCorrectChar error NoRussianChars ",
                "error NoRussianChars ",
                "error NoRussianChars "};
        for (int i = 0; i < actual.length; i++) {
            String expected = checkMessage.checkName(sourceText[i]);
            System.out.println(sourceText[i]);
            Assert.assertEquals(expected, actual[i]);
        }
    }

    @Test
    void checkSurname() {
        CheckMessage checkMessage = getCheckMessage();
        String[] sourceText = {
                "Магомедамин",
                "Магомед Амин",
                "Магомед-амин",
                "Маго-мед амин",
                "Магомед- амин",
                "М-а-г-о-м-е-д- а-м-и-н",
                "М@г0мед@м1н",
                "Мaгoмeдaмин",
                "➡️"};
        String[] actual = {
                "ok",
                "ok",
                "ok",
                "ok",
                "error NoRussianChars ",
                "error BigLength 20 error NoRussianChars ",
                "error Number error NoCorrectChar error NoRussianChars ",
                "error NoRussianChars ",
                "error NoRussianChars "};
        for (int i = 0; i < actual.length; i++) {
            String expected = checkMessage.checkName(sourceText[i]);
            System.out.println(sourceText[i]);
            Assert.assertEquals(expected, actual[i]);
        }
    }

    @Test
    void checkPhoneNumber() {
        CheckMessage checkMessage = getCheckMessage();
        String[] sourceText = {
                "+79234223423",
                "89224234343",
                "32322323232",
                "82323232322",
                "+72323232322",
                "-72342342342",
                "-23423423243423422",
                "-3232323",
                "➡️",
                "8988647558"};
        String[] actual = {
                "ok",
                "ok",
                "error FormatPhoneNumber ",
                "error FormatPhoneNumber ",
                "error FormatPhoneNumber ",
                "error NoCorrectChar error FormatPhoneNumber ",
                "error BigLength 12 error NoCorrectChar error FormatPhoneNumber ",
                "error LittleLength 11 error NoCorrectChar error FormatPhoneNumber ",
                "error LittleLength 11 error NoCorrectChar error FormatPhoneNumber ",
                "error LittleLength 11 error FormatPhoneNumber "};
        for (int i = 0; i < actual.length; i++) {
            String expected = checkMessage.checkPhoneNumber(sourceText[i]);
            System.out.println(sourceText[i]);
            Assert.assertEquals(expected, actual[i]);
        }
    }

    @Test
    void checkAddress() {
        CheckMessage checkMessage = getCheckMessage();
        String[] sourceText = {
                "Ленина 121",
                "Казбекова 2342",
                "ул. Казбекова, дом 2/2",
                "М-а-г-о-м-е-д- а-м-и-н",
                "М@г0м",
                "Мaгoмeдaмин",
                "➡️"};
        String[] actual = {
                "ok",
                "ok",
                "ok",
                "ok",
                "error NoRussianChars error NoCorrectChar ",
                "error NoRussianChars ",
                "error NoRussianChars error LittleLength 5 "};
        for (int i = 0; i < actual.length; i++) {
            String expected = checkMessage.checkAddress(sourceText[i]);
            System.out.println(sourceText[i]);
            Assert.assertEquals(expected, actual[i]);
        }
    }

    private CheckMessage getCheckMessage() {
        return new CheckMessage(
                new RegistrationTextMessage(),
                new OrderTextMessage(
                        new DishOptionallyServiceImpl(new DishOptionallyRepository() {
            @Override
            public List<DishOptionally> findAll() {
                return null;
            }

            @Override
            public List<DishOptionally> findAll(Sort sort) {
                return null;
            }

            @Override
            public List<DishOptionally> findAllById(Iterable<Integer> iterable) {
                return null;
            }

            @Override
            public <S extends DishOptionally> List<S> saveAll(Iterable<S> iterable) {
                return null;
            }

            @Override
            public void flush() {

            }

            @Override
            public <S extends DishOptionally> S saveAndFlush(S s) {
                return null;
            }

            @Override
            public <S extends DishOptionally> List<S> saveAllAndFlush(Iterable<S> iterable) {
                return null;
            }

            @Override
            public void deleteAllInBatch(Iterable<DishOptionally> iterable) {

            }

            @Override
            public void deleteAllByIdInBatch(Iterable<Integer> iterable) {

            }

            @Override
            public void deleteAllInBatch() {

            }

            @Override
            public DishOptionally getOne(Integer integer) {
                return null;
            }

            @Override
            public DishOptionally getById(Integer integer) {
                return null;
            }

            @Override
            public <S extends DishOptionally> List<S> findAll(Example<S> example) {
                return null;
            }

            @Override
            public <S extends DishOptionally> List<S> findAll(Example<S> example, Sort sort) {
                return null;
            }

            @Override
            public Page<DishOptionally> findAll(Pageable pageable) {
                return null;
            }

            @Override
            public <S extends DishOptionally> S save(S s) {
                return null;
            }

            @Override
            public Optional<DishOptionally> findById(Integer integer) {
                return Optional.empty();
            }

            @Override
            public boolean existsById(Integer integer) {
                return false;
            }

            @Override
            public long count() {
                return 0;
            }

            @Override
            public void deleteById(Integer integer) {

            }

            @Override
            public void delete(DishOptionally dishOptionally) {

            }

            @Override
            public void deleteAllById(Iterable<? extends Integer> iterable) {

            }

            @Override
            public void deleteAll(Iterable<? extends DishOptionally> iterable) {

            }

            @Override
            public void deleteAll() {

            }

            @Override
            public <S extends DishOptionally> Optional<S> findOne(Example<S> example) {
                return Optional.empty();
            }

            @Override
            public <S extends DishOptionally> Page<S> findAll(Example<S> example, Pageable pageable) {
                return null;
            }

            @Override
            public <S extends DishOptionally> long count(Example<S> example) {
                return 0;
            }

            @Override
            public <S extends DishOptionally> boolean exists(Example<S> example) {
                return false;
            }
        }),
                        new DishServiceImpl(new DishRepository() {
            @Override
            public List<Dish> findDishByType(String type) {
                return null;
            }

            @Override
            public Dish findDishByTag(String tag) {
                return null;
            }

            @Override
            public List<Dish> findAll() {
                return null;
            }

            @Override
            public List<Dish> findAll(Sort sort) {
                return null;
            }

            @Override
            public List<Dish> findAllById(Iterable<Integer> iterable) {
                return null;
            }

            @Override
            public <S extends Dish> List<S> saveAll(Iterable<S> iterable) {
                return null;
            }

            @Override
            public void flush() {

            }

            @Override
            public <S extends Dish> S saveAndFlush(S s) {
                return null;
            }

            @Override
            public <S extends Dish> List<S> saveAllAndFlush(Iterable<S> iterable) {
                return null;
            }

            @Override
            public void deleteAllInBatch(Iterable<Dish> iterable) {

            }

            @Override
            public void deleteAllByIdInBatch(Iterable<Integer> iterable) {

            }

            @Override
            public void deleteAllInBatch() {

            }

            @Override
            public Dish getOne(Integer integer) {
                return null;
            }

            @Override
            public Dish getById(Integer integer) {
                return null;
            }

            @Override
            public <S extends Dish> List<S> findAll(Example<S> example) {
                return null;
            }

            @Override
            public <S extends Dish> List<S> findAll(Example<S> example, Sort sort) {
                return null;
            }

            @Override
            public Page<Dish> findAll(Pageable pageable) {
                return null;
            }

            @Override
            public <S extends Dish> S save(S s) {
                return null;
            }

            @Override
            public Optional<Dish> findById(Integer integer) {
                return Optional.empty();
            }

            @Override
            public boolean existsById(Integer integer) {
                return false;
            }

            @Override
            public long count() {
                return 0;
            }

            @Override
            public void deleteById(Integer integer) {

            }

            @Override
            public void delete(Dish dish) {

            }

            @Override
            public void deleteAllById(Iterable<? extends Integer> iterable) {

            }

            @Override
            public void deleteAll(Iterable<? extends Dish> iterable) {

            }

            @Override
            public void deleteAll() {

            }

            @Override
            public <S extends Dish> Optional<S> findOne(Example<S> example) {
                return Optional.empty();
            }

            @Override
            public <S extends Dish> Page<S> findAll(Example<S> example, Pageable pageable) {
                return null;
            }

            @Override
            public <S extends Dish> long count(Example<S> example) {
                return 0;
            }

            @Override
            public <S extends Dish> boolean exists(Example<S> example) {
                return false;
            }
        }),
                        new OrderServiceImpl(new OrderRepository() {
            @Override
            public List<Order> findOrderByOrderStatusAndClientId(String orderStatus, Client client) {
                return null;
            }

                            @Override
            public List<Order> findAll() {
                return null;
            }

            @Override
            public List<Order> findAll(Sort sort) {
                return null;
            }

            @Override
            public List<Order> findAllById(Iterable<Integer> iterable) {
                return null;
            }

            @Override
            public <S extends Order> List<S> saveAll(Iterable<S> iterable) {
                return null;
            }

            @Override
            public void flush() {

            }

            @Override
            public <S extends Order> S saveAndFlush(S s) {
                return null;
            }

            @Override
            public <S extends Order> List<S> saveAllAndFlush(Iterable<S> iterable) {
                return null;
            }

            @Override
            public void deleteAllInBatch(Iterable<Order> iterable) {

            }

            @Override
            public void deleteAllByIdInBatch(Iterable<Integer> iterable) {

            }

            @Override
            public void deleteAllInBatch() {

            }

            @Override
            public Order getOne(Integer integer) {
                return null;
            }

            @Override
            public Order getById(Integer integer) {
                return null;
            }

            @Override
            public <S extends Order> List<S> findAll(Example<S> example) {
                return null;
            }

            @Override
            public <S extends Order> List<S> findAll(Example<S> example, Sort sort) {
                return null;
            }

            @Override
            public Page<Order> findAll(Pageable pageable) {
                return null;
            }

            @Override
            public <S extends Order> S save(S s) {
                return null;
            }

            @Override
            public Optional<Order> findById(Integer integer) {
                return Optional.empty();
            }

            @Override
            public boolean existsById(Integer integer) {
                return false;
            }

            @Override
            public long count() {
                return 0;
            }

            @Override
            public void deleteById(Integer integer) {

            }

            @Override
            public void delete(Order order) {

            }

            @Override
            public void deleteAllById(Iterable<? extends Integer> iterable) {

            }

            @Override
            public void deleteAll(Iterable<? extends Order> iterable) {

            }

            @Override
            public void deleteAll() {

            }

            @Override
            public <S extends Order> Optional<S> findOne(Example<S> example) {
                return Optional.empty();
            }

            @Override
            public <S extends Order> Page<S> findAll(Example<S> example, Pageable pageable) {
                return null;
            }

            @Override
            public <S extends Order> long count(Example<S> example) {
                return 0;
            }

            @Override
            public <S extends Order> boolean exists(Example<S> example) {
                return false;
            }
        })));
    }

}