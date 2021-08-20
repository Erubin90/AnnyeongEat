package tech.erubin.annyeong_eat.telegramBotClient.entity;

import lombok.*;

import javax.persistence.*;
import java.util.List;

@Entity
@Table(name = "dish_optionally")
@Setter
@Getter
@NoArgsConstructor
public class DishOptionally {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private int id;

    @Column(name = "name")
    private String name;

    @ManyToOne(cascade = {CascadeType.PERSIST, CascadeType.DETACH, CascadeType.MERGE, CascadeType.REFRESH})
    @JoinColumn(name = "dish_id")
    private Dish dishesId;

    @Column(name = "cost_price")
    private double costPrice;

    @Column(name = "price")
    private double price;

    @Column(name = "grams")
    private int grams;

    @OneToMany(mappedBy = "dishOptionallyId",
            cascade = {CascadeType.PERSIST, CascadeType.DETACH, CascadeType.MERGE, CascadeType.REFRESH})
    private List<ChequeDishOptionally> chequeDishOptionallyList;
}
