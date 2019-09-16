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

    private Double wins;
    private Double losses;
    private Double ties;

    public Score(){
    }
    public Score(Double wins, Double losses, Double ties, GamePlayer gamePlayer){
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

    public Double getWins() {
        return wins;
    }

    public Double getLosses() {
        return losses;
    }

    public Double getTies() {
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
