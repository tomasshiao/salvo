package com.codeoftheweb.salvo.Model;

import org.hibernate.annotations.GenericGenerator;

import javax.persistence.*;
import java.util.*;

@Entity
public class Ship {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO, generator = "native")
    @GenericGenerator(name="native", strategy="native")
    private Long id;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name="gamePlayer_id")
    private GamePlayer gamePlayer;

    private String shipType;

    @ElementCollection
    @Column(name = "shipLocation")
    private Set<String> shipLocation;

    public Ship(){

    }

    public Long getId() {
        return id;
    }

    public GamePlayer getGamePlayer() {
        return gamePlayer;
    }

    public String getShipType() {
        return shipType;
    }

    public Set<String> getShipLocation() {
        return shipLocation;
    }

    public void setGamePlayer(GamePlayer gamePlayer) {
        this.gamePlayer = gamePlayer;
    }

    public Ship(String shipType, Set<String> shipLocation, GamePlayer gamePlayer){
        this.shipType = shipType;
        this.shipLocation = shipLocation;
        this.gamePlayer = gamePlayer;

    }

    public Map<String, Object> toDTO(){
        Map<String, Object> dto = new LinkedHashMap<String, Object>();
        dto.put("type", this.getShipType());
        dto.put("locations", this.getShipLocation());
        return dto;
    }
}
