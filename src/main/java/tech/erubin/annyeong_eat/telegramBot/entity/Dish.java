package tech.erubin.annyeong_eat.telegramBot.entity;

import lombok.*;

import javax.persistence.*;
import java.util.List;

@Entity
@Table(name = "dishes")
@Setter
@Getter
@ToString
@NoArgsConstructor
public class Dish {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private int id;

    @Column(name = "name")
    private String name;

    @Column(name = "tag")
    private String tag;

    @Column(name = "comment")
    private String comment;

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

    @OneToMany(mappedBy = "dishesId",
            cascade = {CascadeType.PERSIST, CascadeType.DETACH, CascadeType.MERGE, CascadeType.REFRESH})
    private List<DishOptionally> dishOptionallyList;

    @ManyToOne(cascade = {CascadeType.PERSIST, CascadeType.DETACH, CascadeType.MERGE, CascadeType.REFRESH})
    @JoinColumn(name = "cafe_id")
    private Cafe cafeId;
}
