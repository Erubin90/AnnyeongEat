package tech.erubin.annyeong_eat.telegramBot.service.entityServises;

import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import tech.erubin.annyeong_eat.telegramBot.entity.ChequeDish;
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
    public List<ChequeDish> getAllCheque() {
        return repository.findAll();
    }

    @Override
    public ChequeDish getChequeById(int id) {
        return repository.getById(id);
    }

    @Override
    public ChequeDish getChequeByOrderAndDish(Order order, Dish dish) {
        ChequeDish chequeDish = repository.findChequeByOrderIdAndDishId(order, dish);
        if (chequeDish == null) {
            chequeDish = createCheque(order, dish);
        }
        return chequeDish;
    }

    @Override
    public void saveCheque(ChequeDish chequeDish) {
        repository.save(chequeDish);
    }

    @Override
    public void deleteCheque(ChequeDish chequeDish) {
        repository.delete(chequeDish);
    }

    @Override
    public void deleteAllChequesByOrder(Order order) {
        List<ChequeDish> chequeDishList = order.getChequeDishList();
        int i = 0;
        while (i < chequeDishList.size()) {
            chequeDishList.get(i).setOrderId(null);
            chequeDishList.get(i).setDishId(null);
            repository.delete(chequeDishList.get(i));
            i++;
        }
    }

    @Override
    public ChequeDish createCheque(Order order, Dish dish) {
        return new ChequeDish(order, dish);
    }
}
