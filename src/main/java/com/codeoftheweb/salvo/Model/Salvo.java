package com.codeoftheweb.salvo.Model;

import org.hibernate.annotations.GenericGenerator;

import javax.persistence.*;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Set;

@Entity
public class Salvo {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO, generator = "native")
    @GenericGenerator(name="native", strategy="native")
    private Long id;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name="gamePlayer_id")
    private GamePlayer gamePlayer;

    private int turnNumber;

    @ElementCollection
    @Column(name = "salvoLocation")
    private Set<String> salvoLocation;

    public Salvo(){};

    public Salvo(GamePlayer gamePlayer, int turnNumber, Set<String> salvoLocation){
        this.gamePlayer = gamePlayer;
        this.turnNumber = turnNumber;
        this.salvoLocation = salvoLocation;
    }

    public Long getId() {
        return id;
    }

    public GamePlayer getGamePlayer() {
        return gamePlayer;
    }

    public int getTurnNumber() {
        return turnNumber;
    }

    public Set<String> getSalvoLocation() {
        return salvoLocation;
    }

    public void setTurnNumber(int turnNumber) {
        this.turnNumber = turnNumber;
    }

    public Map<String, Object> toDTO(){
        Map<String, Object> dto = new LinkedHashMap<String, Object>();
        dto.put("turn", this.getTurnNumber());
        dto.put("player", this.getGamePlayer().getPlayer().getId());
        dto.put("locations", this.getSalvoLocation());
        return dto;
    }
}
