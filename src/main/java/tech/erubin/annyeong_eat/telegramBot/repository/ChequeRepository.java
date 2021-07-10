package tech.erubin.annyeong_eat.telegramBot.repository;


import org.springframework.data.jpa.repository.JpaRepository;
import tech.erubin.annyeong_eat.telegramBot.entity.Cheque;

public interface ChequeRepository extends JpaRepository<Cheque, Integer> {
}
