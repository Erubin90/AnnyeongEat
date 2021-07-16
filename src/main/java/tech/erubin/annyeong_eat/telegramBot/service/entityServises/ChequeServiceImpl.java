package tech.erubin.annyeong_eat.telegramBot.service.entityServises;

import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import tech.erubin.annyeong_eat.telegramBot.entity.Cheque;
import tech.erubin.annyeong_eat.telegramBot.repository.ChequeRepository;
import tech.erubin.annyeong_eat.telegramBot.service.entityServises.serviceInterface.ChequeService;

import java.util.List;

@Service
@AllArgsConstructor
public class ChequeServiceImpl implements ChequeService {
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
    public Cheque createCheque() {
        return new Cheque();
    }
}
