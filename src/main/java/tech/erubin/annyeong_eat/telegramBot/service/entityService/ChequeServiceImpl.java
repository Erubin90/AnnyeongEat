package tech.erubin.annyeong_eat.telegramBot.service.entityService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import tech.erubin.annyeong_eat.telegramBot.entity.Cheque;
import tech.erubin.annyeong_eat.telegramBot.entity.Dish;
import tech.erubin.annyeong_eat.telegramBot.entity.Order;
import tech.erubin.annyeong_eat.telegramBot.repository.ChequeRepository;
import tech.erubin.annyeong_eat.telegramBot.service.entityService.ServiceInterface.ChequeService;

import java.util.List;

@Service
public class ChequeServiceImpl implements ChequeService {

    @Autowired
    private ChequeRepository repository;

    @Override
    public List<Cheque> getAllCheque() {
        return repository.findAll();
    }

    @Override
    public Cheque getChequeById(int id) {
        return repository.getById(id);
    }

    @Override
    public void saveCheque(Cheque cheque) {
        repository.save(cheque);
    }

    @Override
    public void deleteCheque(Cheque cheque) {
        repository.delete(cheque);
    }

    @Override
    public Cheque createCheque(Order order, Dish dish, Integer countDishes) {
        return new Cheque(order, dish, countDishes);
    }
}