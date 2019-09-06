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
    private GamePlayer gamePlayers;

    private String shipType;
    @Transient
    private List<String> shipTypes = new LinkedList<String>(Arrays.asList("Submarine", "Destroyer", "Patrol Boat"));

    @ElementCollection
    @Column(name = "shipLocation")
    private Set<String> shipLocation;

    public Ship(){

    }

    public Long getId() {
        return id;
    }

    public GamePlayer getGamePlayers() {
        return gamePlayers;
    }

    public List<String> getShipTypes() {
        return shipTypes;
    }

    public Set<String> getShipLocation() {
        return shipLocation;
    }

    public Ship(String shipType, Set<String> shipLocation, GamePlayer gamePlayers){
        this.shipType = shipType;
        this.shipLocation = shipLocation;
        this.gamePlayers = gamePlayers;

    }

    public Map<String, Object> toDTO(){
        Map<String, Object> dto = new LinkedHashMap<String, Object>();
        dto.put("type", this.getShipTypes());
        dto.put("locations", this.getShipLocation());
        return dto;
    }
}
