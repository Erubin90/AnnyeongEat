package tech.erubin.annyeong_eat.dateBase.entity;

import lombok.*;

import javax.persistence.*;

@Entity
@Table(name = "employees")
@Setter
@Getter
@ToString
@NoArgsConstructor
public class Employee {
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

    @Column(name = "role")
    private String role;

    @Column(name = "status")
    private String status;

    public Employee(String name, String surname, String role) {
        this.name = name;
        this.surname = surname;
        this.role = role;
        this.status = "работает";
    }
}
