package tech.erubin.annyeong_eat.telegramBot.entity;

import lombok.*;

import javax.persistence.*;

@Entity
@Table(name = "employees")
@Setter
@Getter
@ToString
@NoArgsConstructor
public class Employee extends AbstractEntity{
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private int id;

    @Column(name = "name")
    private String name;

    @Column(name = "surname")
    private String surname;

    @Column(name = "telegram_user_id")
    private String telegramUserId;

    @Column(name = "phone_number")
    private String phoneNumber;

    @Column(name = "role")
    private String role;

    @Column(name = "status")
    private String status;

    @Column(name = "condition")
    private String condition;

    public Employee(String name, String surname, String telegramUserId, String phoneNumber, String role) {
        this.name = name;
        this.surname = surname;
        this.telegramUserId = telegramUserId;
        this.phoneNumber = phoneNumber;
        this.role = role;
        this.status = "работает";
    }
}