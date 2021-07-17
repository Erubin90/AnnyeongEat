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
        Cheque cheque = repository.findChequeByOrderIdAndDishId(order, dish);
        if (cheque == null) {
            cheque = createCheque(order, dish);
        }
        return cheque;
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
    public void deleteAllChequesByOrder(Order order) {
        List<Cheque> chequeList = order.getChequeList();
        int i = 0;
        while (i < chequeList.size()) {
            chequeList.get(i).setOrderId(null);
            chequeList.get(i).setDishId(null);
            repository.delete(chequeList.get(i));
            i++;
        }
    }

    @Override
    public Cheque createCheque(Order order, Dish dish) {
        return new Cheque(order, dish);
    }
}
