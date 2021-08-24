package tech.erubin.annyeong_eat.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import tech.erubin.annyeong_eat.entity.DishOptionally;

@Repository
public interface DishOptionallyRepository extends JpaRepository<DishOptionally, Integer> {
}
