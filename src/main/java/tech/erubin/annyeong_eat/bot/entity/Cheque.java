package tech.erubin.annyeong_eat.bot.entity;

import lombok.*;

import javax.persistence.*;
import javax.persistence.Entity;

@Entity
@Table(name = "cheque")
@Setter
@Getter
@ToString
@NoArgsConstructor
public class Cheque extends AbstractEntity {

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

    @Column(name = "dish_opt1")
    private int dishOpt1;

    @Column(name = "count_dish_opt1")
    private int countDishOpt1;

    @Column(name = "dish_opt2")
    private int dishOpt2;

    @Column(name = "count_dish_opt2")
    private int countDishOpt2;

    @Column(name = "dish_opt3")
    private int dishOpt3;

    @Column(name = "count_dish_opt3")
    private int countDishOpt3;

    public Cheque(Order order, Dish dish, int countDishes) {
        this.orderId = order;
        this.dishId = dish;
        this.countDishes = countDishes;
    }
}
