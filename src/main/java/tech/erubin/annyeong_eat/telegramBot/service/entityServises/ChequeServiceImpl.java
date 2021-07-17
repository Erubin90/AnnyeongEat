package tech.erubin.annyeong_eat.telegramBot.service.entityServises;

import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import tech.erubin.annyeong_eat.telegramBot.entity.Cheque;
import tech.erubin.annyeong_eat.telegramBot.entity.Dish;
import tech.erubin.annyeong_eat.telegramBot.entity.Order;
import tech.erubin.annyeong_eat.telegramBot.repository.ChequeRepository;
import tech.erubin.annyeong_eat.telegramBot.service.entityServises.serviceInterface.ChequeService;

import java.util.List;

@Service
@AllArgsConstructor
public class ChequeServiceImpl implements ChequeService {
    private final ChequeRepository repository;

    @Override
    public List<Cheque> getAllCheque() {
        return repository.findAll();
    }

    @Override
    public Cheque getChequeById(int id) {
        return repository.getById(id);
    }

    @Override
    public Cheque getChequeByOrderAndDish(Order order, Dish dish) {
        return repository.findChequeByOrderIdAndDishId(order, dish);
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
    public Cheque createCheque() {
        return new Cheque();
    }
}
