package com.codeoftheweb.salvo.Model;

import org.hibernate.annotations.GenericGenerator;

import javax.persistence.*;
import java.util.LinkedHashMap;
import java.util.Map;

public class Score {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO, generator = "native")
    @GenericGenerator(name="native", strategy="native")
    private Long id;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name="gamePlayer_id")
    private GamePlayer gamePlayer;

    private String wins;
    private String losses;
    private String ties;

    public Score(){
    }
    public Score(String wins, String losses, String ties, GamePlayer gamePlayer){
        this.wins = wins;
        this.losses = losses;
        this.ties = ties;
        this.gamePlayer = gamePlayer;
    }

    public Long getId() {
        return id;
    }

    public GamePlayer getGamePlayer() {
        return gamePlayer;
    }

    public String getWins() {
        return wins;
    }

    public String getLosses() {
        return losses;
    }

    public String getTies() {
        return ties;
    }

    public Map<String,Object> toDTO(){
        Map<String, Object> dto = new LinkedHashMap<>();
        dto.put("wins", this.getWins());
        dto.put("losses", this.getLosses());
        dto.put("ties", this.getTies());
        return dto;
    }
}
