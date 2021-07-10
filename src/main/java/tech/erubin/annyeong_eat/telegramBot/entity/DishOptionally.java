package tech.erubin.annyeong_eat.telegramBot.entity;

import lombok.*;

import javax.persistence.*;

@Entity
@Table(name = "dish_optionally")
@Setter
@Getter
@ToString
@NoArgsConstructor
public class DishOptionally extends AbstractEntity{

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

    public DishOptionally(String name, String type, double price) {
        this.name = name;
        this.type = type;
        this.costPrice = 0.00;
        this.price = price;
        this.grams = 0;
    }
}
