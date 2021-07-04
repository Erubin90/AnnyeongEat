package tech.erubin.annyeong_eat.dateBase.entity;

import lombok.*;

import javax.persistence.*;
import javax.persistence.Entity;

@Entity
@Table(name = "cheque")
@Setter
@Getter
@ToString
@NoArgsConstructor
public class Cheque{

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Integer id;

    @ManyToOne(cascade = {CascadeType.PERSIST, CascadeType.DETACH, CascadeType.MERGE, CascadeType.REFRESH})
    @JoinColumn(name = "order_id")
    private Order orderId;

    @ManyToOne(cascade = {CascadeType.PERSIST, CascadeType.DETACH, CascadeType.MERGE, CascadeType.REFRESH})
    @JoinColumn(name = "dish_id")
    private Dish dishId;

    @Column(name = "count_dishes")
    private Integer countDishes;

    @Column(name = "dish_opt1")
    private Integer dishOpt1;

    @Column(name = "count_dish_opt1")
    private Integer countDishOpt1;

    @Column(name = "dish_opt2")
    private Integer dishOpt2;

    @Column(name = "count_dish_opt2")
    private Integer countDishOpt2;

    @Column(name = "dish_opt3")
    private Integer dishOpt3;

    @Column(name = "count_dish_opt3")
    private Integer countDishOpt3;

    public Cheque(Order orderId, Dish dishId, Integer countDishes) {
        this.orderId = orderId;
        this.dishId = dishId;
        this.countDishes = countDishes;
    }
}
