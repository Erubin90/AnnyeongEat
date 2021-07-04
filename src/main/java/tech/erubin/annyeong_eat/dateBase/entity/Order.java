package tech.erubin.annyeong_eat.dateBase.entity;

import lombok.*;

import javax.persistence.*;
import java.sql.Timestamp;
import java.util.List;

@Entity
@Table(name = "orders")
@Setter
@Getter
@ToString
@NoArgsConstructor
public class Order{

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Integer id;

    @ManyToOne(cascade = {CascadeType.PERSIST, CascadeType.DETACH, CascadeType.MERGE, CascadeType.REFRESH})
    @JoinColumn(name = "client_id")
    private Client clientId;

    @Column(name = "order_name")
    private String orderName;

    @Column(name = "address")
    private String address;

    @Column(name = "comment")
    private String comment;

    @Column(name = "payment_method")
    private String paymentMethod;

    @Column(name = "payment_status")
    private Integer paymentStatus;

    @Column(name = "order_status")
    private String orderStatus;

    @Column(name = "waiter_id")
    private Integer waiterId;

    @Column(name = "delivery_id")
    private Integer deliveryId;

    @Column(name = "time_accept")
    private Timestamp timeAccept;

    @Column(name = "time_start_cook")
    private Timestamp timeStartCook;

    @Column(name = "time_end_cook")
    private Timestamp timeEndCook;

    @Column(name = "time_start_delivery")
    private Timestamp timeStartDelivery;

    @Column(name = "time_end_delivery")
    private Timestamp timeEndDelivery;

    @OneToMany(mappedBy = "orderId",
            cascade = {CascadeType.PERSIST, CascadeType.DETACH, CascadeType.MERGE, CascadeType.REFRESH})
    private List<Cheque> chequeList;

    public Order(Client clientId, String orderName, String address, String comment, String paymentMethod) {
        this.clientId = clientId;
        this.orderName = orderName;
        this.address = address;
        this.comment = comment;
        this.paymentMethod = paymentMethod;
    }
}
