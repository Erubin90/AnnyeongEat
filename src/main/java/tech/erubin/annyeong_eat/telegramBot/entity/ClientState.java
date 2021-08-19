package tech.erubin.annyeong_eat.telegramBot.entity;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import tech.erubin.annyeong_eat.telegramBot.states.ClientStateEnum;

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
    @JoinColumn(name = "client_id")
    private Client clientId;

    @Column(name = "state")
    private String state;

    @Column(name = "usage_time")
    private Timestamp usageTime;

    public ClientState(Client clientId) {
        this.clientId = clientId;
        this.state = ClientStateEnum.REGISTRATION_START.getValue();
        this.usageTime = new Timestamp(System.currentTimeMillis());
    }
}
