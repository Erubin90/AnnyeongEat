package tech.erubin.annyeong_eat.telegramBot.entity;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;

@Entity
@Table(name = "cheque_dish_optionally")
@Setter
@Getter
@NoArgsConstructor
public class ChequeDishOptionally {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private int id;

    @ManyToOne(cascade = {CascadeType.PERSIST, CascadeType.DETACH, CascadeType.MERGE, CascadeType.REFRESH})
    @JoinColumn(name = "cheque_dishes_id")
    private ChequeDish chequeDishId;

    @ManyToOne(cascade = {CascadeType.PERSIST, CascadeType.DETACH, CascadeType.MERGE, CascadeType.REFRESH})
    @JoinColumn(name = "dish_optionally_id")
    private DishOptionally dishOptionallyId;

    @Column(name = "count")
    private int count;
}
