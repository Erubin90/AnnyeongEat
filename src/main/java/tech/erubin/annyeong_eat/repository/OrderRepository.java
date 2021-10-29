package tech.erubin.annyeong_eat.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import tech.erubin.annyeong_eat.entity.Cafe;
import tech.erubin.annyeong_eat.entity.Order;
import tech.erubin.annyeong_eat.entity.User;

import java.util.List;

@Repository
public interface OrderRepository extends JpaRepository<Order, Integer> {

    @Query("FROM Order " +
            "WHERE using = 1 AND cafeId = :cafe and userId = :user")
    Order getOrderByUserIdAndCafeId(@Param("cafe")Cafe cafe, @Param("user") User user);

    Order getOrderByOrderName(String orderName);

    @Query("FROM Order WHERE userId = :user and id not in (SELECT orderId FROM OrderState WHERE state in (:list))")
    List<Order> getAllOrdersInProgress(@Param("user") User user, @Param("list") String... filter);
}
