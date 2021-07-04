package tech.erubin.annyeong_eat.dateBase.entity;

import lombok.*;

import javax.persistence.*;
import java.util.List;

@Entity
@Table(name = "clients")
@Setter
@Getter
@ToString
@NoArgsConstructor
public class Client {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Integer id;

    @Column(name = "name")
    private String name;

    @Column(name = "surname")
    private String surname;

    @Column(name = "telegram_name")
    private String telegramName;

    @Column(name = "phone_number")
    private String phoneNumber;

    @Column(name = "status")
    private String status;

    @OneToMany(mappedBy = "clientId",
            cascade = {CascadeType.PERSIST, CascadeType.DETACH, CascadeType.MERGE, CascadeType.REFRESH})
    private List<Order> orderList;

    public Client(String name, String surname, String telegramName, String phoneNumber) {
        this.name = name;
        this.surname = surname;
        this.telegramName = telegramName;
        this.phoneNumber = phoneNumber;
        this.status = "активный";
    }
}
