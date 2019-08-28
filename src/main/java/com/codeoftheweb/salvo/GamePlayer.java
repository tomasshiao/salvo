package com.codeoftheweb.salvo;

import com.fasterxml.jackson.annotation.JsonIgnore;
import org.hibernate.annotations.GenericGenerator;

import javax.persistence.*;
import java.util.Date;

@Entity
public class GamePlayer {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO, generator = "native")
    @GenericGenerator(name = "native", strategy = "native")

    private Long id;
    private Date joinTime;
    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name="game_id")
    private Game game1;

    @JsonIgnore
    public Game getGame1() {
        return game1;
    }

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name="player_id")
    private Player player1;

    @JsonIgnore
    public Player getPlayer1() {
        return player1;
    }

    public GamePlayer() { }

    public GamePlayer (Player player1, Game game1) {
        this.player1 = player1;
        this.game1 = game1;
        this.joinTime = new Date();
    }
    public Long getId(){return id;}
    public Date getJoinTime(){return joinTime;}

}
