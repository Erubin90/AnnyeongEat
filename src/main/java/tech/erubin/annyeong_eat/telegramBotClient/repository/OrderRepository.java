package tech.erubin.annyeong_eat.telegramBotClient.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import tech.erubin.annyeong_eat.telegramBotClient.entity.Cafe;
import tech.erubin.annyeong_eat.telegramBotClient.entity.Client;
import tech.erubin.annyeong_eat.telegramBotClient.entity.Order;

import java.util.List;

@Repository
public interface OrderRepository extends JpaRepository<Order, Integer> {

    @Query("SELECT o.id " +
            "FROM Order o JOIN OrderState os ON o = os.orderId " +
            "WHERE os.state LIKE :orderState AND o.cafeId = :cafe and o.clientId = :client")
    List<Integer> getOrderByClientIdAndCafeIdAndOrderState(@Param("orderState")String orderState,
                                                              @Param("cafe")Cafe cafe,
                                                              @Param("client")Client client);
}
