package tech.erubin.annyeong_eat.entity;

import lombok.*;

import javax.persistence.*;
import java.util.List;

@Entity
@Table(name = "users")
@Setter
@Getter
@NoArgsConstructor
public class User {
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

    @OneToMany(mappedBy = "userId",
            cascade = {CascadeType.PERSIST, CascadeType.DETACH, CascadeType.MERGE, CascadeType.REFRESH})
    private List<ClientState> clientStateList;

    @OneToMany(mappedBy = "userId",
            cascade = {CascadeType.PERSIST, CascadeType.DETACH, CascadeType.MERGE, CascadeType.REFRESH})
    private List<EmployeeState> employeeStateList;

    @OneToMany(mappedBy = "userId",
            cascade = {CascadeType.PERSIST, CascadeType.DETACH, CascadeType.MERGE, CascadeType.REFRESH})
    private List<Order> orderList;

    @OneToMany(mappedBy = "userId",
            cascade = {CascadeType.PERSIST, CascadeType.DETACH, CascadeType.MERGE, CascadeType.REFRESH})
    private List<Employee> employeeList;

    public User(String telegramUserId) {
        this.telegramUserId = telegramUserId;
    }
}
