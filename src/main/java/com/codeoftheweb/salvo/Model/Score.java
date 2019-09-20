package com.codeoftheweb.salvo.Model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import org.hibernate.annotations.GenericGenerator;

import javax.persistence.*;
import java.util.Date;

@Entity
public class Score {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO, generator = "native")
    @GenericGenerator(name="native", strategy="native")
    private long id;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name="game_id")
    private Game game;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name="player_id")
    private Player player;

    private Date finishedDate = new Date();

    private double score;

    public Score(){
    }
    public Score(double score, Game game, Player player){
        this.score = score;
        this.game = game;
        this.player = player;
    }

    public long getId() {
        return id;
    }

    @JsonIgnore
    public Game getGame() {
        return game;
    }

    @JsonIgnore
    public Player getPlayer() {
        return player;
    }

    public Date getFinishedDate() {
        return finishedDate;
    }

    public double getScore() {
        return score;
    }

}
