package com.codeoftheweb.salvo.Model;

import org.hibernate.annotations.GenericGenerator;

import javax.persistence.*;
import java.util.*;
import java.util.stream.Collectors;

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

    public void setGamePlayer(GamePlayer gamePlayer) {
        this.gamePlayer = gamePlayer;
    }

    private List<String> getHits(Set<String> myShots, Set<Ship> opponentsShips) {
        List<String> allEnemysShipLoc =  new ArrayList<>();
        opponentsShips.forEach(ship -> allEnemysShipLoc.addAll(ship.getShipLocation()));
        return myShots.stream().filter(shot -> allEnemysShipLoc.stream().anyMatch(loc -> loc.equals(shot))).collect(Collectors.toList());
    }

    private List<Ship> getSunkenShips (Set<Salvo> mySalvoes, Set<Ship> opponentsShips){
        List<String> allShots = new ArrayList<>();
        mySalvoes.forEach(salvo -> allShots.addAll(salvo.getSalvoLocation()));
        return opponentsShips.stream().filter(ship -> allShots.containsAll(ship.getShipLocation())).collect(Collectors.toList());
    }
    public Map<String, Object> toDTO(){
        GamePlayer opponent = this.getGamePlayer().getOpponent();
        Map<String, Object> dto = new LinkedHashMap<String, Object>();
        dto.put("turn", this.getTurnNumber());
        dto.put("player", this.getGamePlayer().getPlayer().getId());
        dto.put("locations", this.getSalvoLocation());
        if(opponent != null){
            Set<Ship> enemysShips = opponent.getShips();
            dto.put("hits", getHits(this.getSalvoLocation(), enemysShips));
            Set<Salvo> mySalvoes = this.getGamePlayer().getSalvoes().stream().filter(salvo -> salvo.getTurnNumber() <= this.getTurnNumber()).collect(Collectors.toSet());
            dto.put("sunkenShips", getSunkenShips(mySalvoes, enemysShips).stream().map(Ship::toDTO));
        }
        return dto;
    }
}
