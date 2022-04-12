package tech.erubin.annyeong_eat.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import tech.erubin.annyeong_eat.entity.Cafe;

import java.util.List;


public interface CafeRepository extends JpaRepository<Cafe, Integer> {
    List<Cafe> findAllByCity(String city);

    Cafe findCafeByName(String name);

    @Query("SELECT c.address FROM Cafe c GROUP BY c.address")
    List<String> findAllAddressCafe();
}
