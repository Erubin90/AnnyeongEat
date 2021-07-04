package tech.erubin.annyeong_eat.dateBase.entity;

import lombok.*;

import javax.persistence.*;
import java.util.List;

@Entity
@Table(name = "dishes")
@Setter
@Getter
@ToString
@NoArgsConstructor
public class Dish{

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Integer id;

    @Column(name = "name")
    private String name;

    @Column(name = "type")
    private String type;

    @Column(name = "cost_price")
    private Double costPrice;

    @Column(name = "price")
    private Double price;

    @Column(name = "grams")
    private Integer grams;

    @OneToMany(mappedBy = "dishId",
            cascade = {CascadeType.PERSIST, CascadeType.DETACH, CascadeType.MERGE, CascadeType.REFRESH})
    private List<Cheque> chequeList;

    public Dish(String name, String type, Double price) {
        this.name = name;
        this.type = type;
        this.costPrice = 0.00;
        this.price = price;
        this.grams = 0;
    }
}
