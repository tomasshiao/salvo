package com.codeoftheweb.salvo;


import org.hibernate.annotations.GenericGenerator;

import javax.persistence.*;
import java.util.Set;

@Entity
public class Player {
   @Id
   @GeneratedValue(strategy = GenerationType.AUTO, generator = "native")
   @GenericGenerator(name="native", strategy="native")

    private Long id;
    private String userName;

    @OneToMany(mappedBy = "game", fetch = FetchType.EAGER)
    @JoinColumn(name = "gamePlayer_id")
    private Set<GamePlayer> gamePlayers;

    public Player() {
    }

    public Player(String userName) {
        this.userName = userName;
    }

    public Long getId() {
        return id;
    }

    public String getUserName() {

        return userName;
    }

}
