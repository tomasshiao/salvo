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
    private GamePlayer gamePlayers;

    private Integer turnNumber;

    @ElementCollection
    @Column(name = "salvoLocation")
    private Set<String> salvoLocation;

    public Salvo(GamePlayer gamePlayers, Integer turnNumber, Set<String> salvoLocation){
        this.gamePlayers = gamePlayers;
        this.turnNumber = turnNumber;
        this.salvoLocation = salvoLocation;
    }

    public Long getId() {
        return id;
    }

    public GamePlayer getGamePlayers() {
        return gamePlayers;
    }

    public Integer getTurnNumber() {
        return turnNumber;
    }

    public Set<String> getSalvoLocation() {
        return salvoLocation;
    }

    public Map<String, Object> toDTO(){
        Map<String, Object> dto = new LinkedHashMap<String, Object>();
        dto.put("turn", this.getTurnNumber());
        dto.put("player", this.getGamePlayers().getPlayer().getId());
        dto.put("locations", this.getSalvoLocation());
        return dto;
    }
}
