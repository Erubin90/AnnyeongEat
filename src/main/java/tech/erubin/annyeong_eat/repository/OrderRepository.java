package tech.erubin.annyeong_eat.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import tech.erubin.annyeong_eat.entity.Cafe;
import tech.erubin.annyeong_eat.entity.User;
import tech.erubin.annyeong_eat.entity.Order;

import java.util.List;

@Repository
public interface OrderRepository extends JpaRepository<Order, Integer> {

    @Query("SELECT id " +
            "FROM Order " +
            "WHERE using = 1 AND cafeId = :cafe and userId = :user")
    List<Integer> getOrderByUserIdAndCafeId(@Param("cafe")Cafe cafe, @Param("user") User user);
}
