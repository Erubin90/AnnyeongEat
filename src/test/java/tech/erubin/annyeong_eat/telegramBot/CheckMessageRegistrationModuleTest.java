package tech.erubin.annyeong_eat.telegramBot;

import org.junit.Assert;
import org.junit.jupiter.api.Test;
import org.springframework.data.domain.Example;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import tech.erubin.annyeong_eat.telegramBot.entity.Cafe;
import tech.erubin.annyeong_eat.telegramBot.messages.TextMessages;
import tech.erubin.annyeong_eat.telegramBot.module.registration.CheckMessageRegistrationModule;
import tech.erubin.annyeong_eat.telegramBot.repository.CafeRepository;
import tech.erubin.annyeong_eat.telegramBot.service.entityServises.CafeServiceImpl;

import java.util.List;
import java.util.Optional;

class CheckMessageRegistrationModuleTest {

    CheckMessageRegistrationModule checkMessage =
            new CheckMessageRegistrationModule(new TextMessages(), new CafeServiceImpl(new CafeRepository() {
                @Override
                public List<Cafe> findAllByCity(String city) {
                    return null;
                }

                @Override
                public List<Cafe> findAll() {
                    return null;
                }

                @Override
                public List<Cafe> findAll(Sort sort) {
                    return null;
                }

                @Override
                public List<Cafe> findAllById(Iterable<Integer> iterable) {
                    return null;
                }

                @Override
                public <S extends Cafe> List<S> saveAll(Iterable<S> iterable) {
                    return null;
                }

                @Override
                public void flush() {

                }

                @Override
                public <S extends Cafe> S saveAndFlush(S s) {
                    return null;
                }

                @Override
                public <S extends Cafe> List<S> saveAllAndFlush(Iterable<S> iterable) {
                    return null;
                }

                @Override
                public void deleteAllInBatch(Iterable<Cafe> iterable) {

                }

                @Override
                public void deleteAllByIdInBatch(Iterable<Integer> iterable) {

                }

                @Override
                public void deleteAllInBatch() {

                }

                @Override
                public Cafe getOne(Integer integer) {
                    return null;
                }

                @Override
                public Cafe getById(Integer integer) {
                    return null;
                }

                @Override
                public <S extends Cafe> List<S> findAll(Example<S> example) {
                    return null;
                }

                @Override
                public <S extends Cafe> List<S> findAll(Example<S> example, Sort sort) {
                    return null;
                }

                @Override
                public Page<Cafe> findAll(Pageable pageable) {
                    return null;
                }

                @Override
                public <S extends Cafe> S save(S s) {
                    return null;
                }

                @Override
                public Optional<Cafe> findById(Integer integer) {
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
                public void delete(Cafe cafe) {

                }

                @Override
                public void deleteAllById(Iterable<? extends Integer> iterable) {

                }

                @Override
                public void deleteAll(Iterable<? extends Cafe> iterable) {

                }

                @Override
                public void deleteAll() {

                }

                @Override
                public <S extends Cafe> Optional<S> findOne(Example<S> example) {
                    return Optional.empty();
                }

                @Override
                public <S extends Cafe> Page<S> findAll(Example<S> example, Pageable pageable) {
                    return null;
                }

                @Override
                public <S extends Cafe> long count(Example<S> example) {
                    return 0;
                }

                @Override
                public <S extends Cafe> boolean exists(Example<S> example) {
                    return false;
                }
            }));

    @Test
    void checkName() {
        String[] testNames = {
                "Магомед",
                "Магомед-Амин",
                "Магомед Амин",
                "Магомед-Ам ин",
                "Мaгомед",
                "М@г0мeд",
                "Магомедawewd2ws sdsd22 ssss sdfsdfs sdfs dsdfssdsfdsfsfsfwwe32323e2edsddefsdfgdfgdfdfg3dfsd sdfsdf sdf ",
                "М"
        };
        String[] actualise = {
                "ok",
                "ok",
                "ok",
                "NoRussianChars ",
                "NoRussianChars ",
                "Number NoCorrectChar NoRussianChars ",
                "Number BigLength 20 NoRussianChars ",
                "LittleLength 2 NoRussianChars "
        };

        for (int i = 0; i < testNames.length; i++) {
            String expected = checkMessage.checkName(testNames[i]);
            String actual = actualise[i];
            System.out.println(testNames[i]);
            Assert.assertEquals(expected, actual);
        }

    }

    @Test
    void checkSurname() {
        String[] testSurnames = {
                "Магомед",
                "Магомед-Амин",
                "Магомед Амин",
                "Магомед-Ам ин",
                "Мaгомед",
                "М@г0мeд",
                "Магомедawewd2ws sdsd22 ssss sdfsdfs sdfs dsdfssdsfdsfsfsfwwe32323e2edsddefsdfgdfgdfdfg3dfsd sdfsdf sdf ",
                "М"
        };
        String[] actualise = {
                "ok",
                "ok",
                "ok",
                "NoRussianChars ",
                "NoRussianChars ",
                "Number NoCorrectChar NoRussianChars ",
                "Number BigLength 32 NoRussianChars ",
                "LittleLength 2 NoRussianChars "
        };

        for (int i = 0; i < testSurnames.length; i++) {
            String expected = checkMessage.checkSurname(testSurnames[i]);
            String actual = actualise[i];
            System.out.println(testSurnames[i]);
            Assert.assertEquals(expected, actual);
        }
    }

    @Test
    void checkPhoneNumber() {
        String[] testPhoneNumbers = {
                "+79288352140",
                "+74288352140",
                "+742883521",
                "+54288352140",
                "+7428835214e2",
                "aefkfsdkIII#@DSDKALD",
        };
        String[] actualise = {
                "ok",
                "ok",
                "LittleLength 12 FormatPhoneNumber ",
                "FormatPhoneNumber ",
                "BigLength 12 NoCorrectChar FormatPhoneNumber ",
                "BigLength 12 NoCorrectChar FormatPhoneNumber ",
        };

        for (int i = 0; i < testPhoneNumbers.length; i++) {
            String expected = checkMessage.checkPhoneNumber(testPhoneNumbers[i]);
            String actual = actualise[i];
            System.out.println(testPhoneNumbers[i]);
            Assert.assertEquals(expected, actual);
        }
    }

    @Test
    void checkCity(){

    }
}