package tech.erubin.annyeong_eat.dateBase.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import tech.erubin.annyeong_eat.dateBase.entity.Client;

public interface ClientRepository extends JpaRepository<Client, Integer>{
}
