package tech.erubin.annyeong_eat.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import tech.erubin.annyeong_eat.entity.ClientState;

@Repository
public interface ClientStatesRepository extends JpaRepository<ClientState, Integer> {
}
