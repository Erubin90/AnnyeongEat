package tech.erubin.annyeong_eat.telegramBot.entity;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import tech.erubin.annyeong_eat.telegramBot.states.OrderStateEnum;

import javax.persistence.*;
import java.sql.Timestamp;

@Entity
@Table(name = "order_states")
@Setter
@Getter
@NoArgsConstructor
public class OrderState {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private int id;

    @ManyToOne(cascade = {CascadeType.PERSIST, CascadeType.DETACH, CascadeType.MERGE, CascadeType.REFRESH})
    @JoinColumn(name = "order_id")
    private Order orderId;

    @Column(name = "state")
    private String state;

    @Column(name = "usage_time")
    private Timestamp usageTime;

    public OrderState(Order orderId) {
        this.orderId = orderId;
        this.state = OrderStateEnum.ORDER_START_REGISTRATION.getValue();
        this.usageTime = new Timestamp(System.currentTimeMillis());
    }
}
