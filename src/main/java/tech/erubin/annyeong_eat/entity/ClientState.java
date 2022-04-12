package tech.erubin.annyeong_eat.entity;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;
import java.sql.Timestamp;

@Entity
@Table(name = "client_states")
@Getter
@Setter
@NoArgsConstructor
public class ClientState {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private int id;

    @ManyToOne(cascade = {CascadeType.PERSIST, CascadeType.DETACH, CascadeType.MERGE, CascadeType.REFRESH})
    @JoinColumn(name = "user_id")
    private User userId;

    @Column(name = "state")
    private String state;

    @Column(name = "usage_time")
    private Timestamp usageTime;

    public ClientState(User userId, String state) {
        this.userId = userId;
        this.state = state;
        this.usageTime = new Timestamp(System.currentTimeMillis());
    }
}
