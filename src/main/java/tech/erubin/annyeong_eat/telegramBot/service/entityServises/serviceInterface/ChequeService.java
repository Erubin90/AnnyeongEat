package tech.erubin.annyeong_eat.telegramBot.service.entityServises.serviceInterface;

import tech.erubin.annyeong_eat.telegramBot.entity.Cheque;

import java.util.List;

public interface ChequeService {

    List<Cheque> getAllCheque();

    Cheque getChequeById(int id);

    void saveCheque(Cheque cheque);

    void deleteCheque(Cheque cheque);

    Cheque createCheque();
}
