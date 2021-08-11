package tech.erubin.annyeong_eat.telegramBot.entity;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;
import java.util.List;

@Entity
@Table(name = "cheque_dishes")
@Setter
@Getter
@NoArgsConstructor
public class ChequeDish {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private int id;

    @ManyToOne(cascade = {CascadeType.PERSIST, CascadeType.DETACH, CascadeType.MERGE, CascadeType.REFRESH})
    @JoinColumn(name = "order_id")
    private Order orderId;

    @ManyToOne(cascade = {CascadeType.PERSIST, CascadeType.DETACH, CascadeType.MERGE, CascadeType.REFRESH})
    @JoinColumn(name = "dish_id")
    private Dish dishId;

    @Column(name = "count_dishes")
    private int countDishes;

    @OneToMany(mappedBy = "chequeDishId",
            cascade = {CascadeType.PERSIST, CascadeType.DETACH, CascadeType.MERGE, CascadeType.REFRESH})
    private List<ChequeDishOptionally> chequeDishOptionallyList;

    public ChequeDish(Order order, Dish dish) {
        orderId = order;
        dishId = dish;
        countDishes = 0;
    }
}
