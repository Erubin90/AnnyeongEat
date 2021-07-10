package tech.erubin.annyeong_eat.telegramBot.entity;

import lombok.*;

import javax.persistence.*;
import java.util.List;

@Entity
@Table(name = "clients")
@Setter
@Getter
@ToString
@NoArgsConstructor
public class Client extends AbstractEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private int id;

    @Column(name = "name")
    private String name;

    @Column(name = "surname")
    private String surname;

    @Column(name = "city")
    private String city;

    @Column(name = "telegram_user_id")
    private String telegramUserId;

    @Column(name = "phone_number")
    private String phoneNumber;

    @Column(name = "status")
    private String status;

    @Column(name = "state")
    private String state;

    @OneToMany(mappedBy = "clientId",
            cascade = {CascadeType.PERSIST, CascadeType.DETACH, CascadeType.MERGE, CascadeType.REFRESH})
    private List<Order> orderList;

    public Client(String name, String surname, String telegramUserId, String phoneNumber) {
        this.name = name;
        this.surname = surname;
        this.telegramUserId = telegramUserId;
        this.phoneNumber = phoneNumber;
        this.status = "регистрация";
        this.state = "не зарегестрирован";
    }

    public Client(String telegramUserId) {
        this.telegramUserId = telegramUserId;
        this.status = "регистрация";
        this.state = "не зарегестрирован";
    }
}
