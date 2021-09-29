package tech.erubin.annyeong_eat.entity;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;
import java.util.List;

@Entity
@Table(name = "orders")
@Setter
@Getter
@NoArgsConstructor
public class Order {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private int id;

    @ManyToOne(cascade = {CascadeType.PERSIST, CascadeType.DETACH, CascadeType.MERGE, CascadeType.REFRESH})
    @JoinColumn(name = "user_id")
    private User userId;

    @ManyToOne(cascade = {CascadeType.PERSIST, CascadeType.DETACH, CascadeType.MERGE, CascadeType.REFRESH})
    @JoinColumn(name = "cafe_id")
    private Cafe cafeId;

    @Column(name = "order_using")
    private int using;

    @Column(name = "order_name")
    private String orderName;

    @Column(name = "address")
    private String address;

    @Column(name = "phone_number")
    private String phoneNumber;

    @Column(name = "comment")
    private String comment;

    @Column(name = "payment_method")
    private String paymentMethod;

    @Column(name = "payment_status")
    private int paymentStatus;

    @Column(name = "price_delivery")
    private int priceDelivery;

    @Column(name = "waiter_id")
    private int waiterId;

    @Column(name = "delivery_id")
    private int deliveryId;

    @OneToMany(mappedBy = "orderId",
            cascade = CascadeType.ALL)
    private List<ChequeDish> chequeDishList;

    @OneToMany(mappedBy = "orderId",
            cascade = CascadeType.ALL)
    private List<OrderState> orderStateList;

    public Order(User userId, String orderName) {
        this.userId = userId;
        this.orderName = orderName;
        this.paymentStatus = 0;
        this.priceDelivery = -1;
        this.comment = "";
    }

    public Order(User userId, Cafe cafeId, String orderName) {
        this.userId = userId;
        this.cafeId = cafeId;
        this.orderName = orderName;
        this.priceDelivery = -1;
        this.comment = "";
    }
}
