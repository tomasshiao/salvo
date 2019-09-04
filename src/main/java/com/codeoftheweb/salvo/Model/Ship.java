package com.codeoftheweb.salvo.Model;

import org.hibernate.annotations.GenericGenerator;

import javax.persistence.*;
import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;

@Entity
public class Ship {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO, generator = "native")
    @GenericGenerator(name="native", strategy="native")

    private Long id;
    @OneToMany(mappedBy = "ship", fetch = FetchType.EAGER)
    private Set<GamePlayer> gamePlayers;

    @ElementCollection
    @Column(name="shipType")
    private List<String> shipType = new ArrayList<>();

    public List<String> getShipType() {
        return shipType;
    }

    public Set<GamePlayer> getGamePlayers() {
        return gamePlayers;
    }

    public Long getId() {
        return id;
    }
    public Ship(Set<Ship>){

    }
}
