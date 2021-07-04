package tech.erubin.annyeong_eat.bot.entity;

import lombok.*;

import javax.persistence.*;
import java.util.List;

@Entity
@Table(name = "dishes")
@Setter
@Getter
@ToString
@NoArgsConstructor
public class Dish extends AbstractEntity{

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private int id;

    @Column(name = "name")
    private String name;

    @Column(name = "type")
    private String type;

    @Column(name = "cost_price")
    private double costPrice;

    @Column(name = "price")
    private double price;

    @Column(name = "grams")
    private int grams;

    @OneToMany(mappedBy = "dishId",
            cascade = {CascadeType.PERSIST, CascadeType.DETACH, CascadeType.MERGE, CascadeType.REFRESH})
    private List<Cheque> chequeList;

    public Dish(String name, String type, double price) {
        this.name = name;
        this.type = type;
        this.costPrice = 0.00;
        this.price = price;
        this.grams = 0;
    }
}
