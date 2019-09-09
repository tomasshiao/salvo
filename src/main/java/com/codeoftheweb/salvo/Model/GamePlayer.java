package com.codeoftheweb.salvo.Model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import org.hibernate.annotations.GenericGenerator;

import javax.persistence.*;
import java.util.Date;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Set;

@Entity
public class GamePlayer {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO, generator = "native")
    @GenericGenerator(name = "native", strategy = "native")

    private Long id;
    private Date joinTime;
    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name="game_id")
    private Game game;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name="player_id")
    private Player player;


    @OneToMany(mappedBy = "gamePlayer", fetch = FetchType.EAGER)
    private Set<Ship> ships;

    @OneToMany(mappedBy = "gamePlayer", fetch = FetchType.EAGER)
    private Set<Salvo> salvoes;

    @JsonIgnore
    public Set<Ship> getShips(){
        return ships;
    }
    @JsonIgnore
    public Player getPlayer() {
        return player;
    }

    @JsonIgnore
    public Game getGame(){
        return game;
    }

    public GamePlayer() { }

    public GamePlayer (Player player, Game game) {
        this.player = player;
        this.game = game;
        this.joinTime = new Date();
    }
    public Long getId(){
        return id;
    }


    @JsonIgnore
    public Set<Salvo> getSalvoes(){
        return salvoes;
    }

    public Date getJoinTime(){
        return joinTime;
    }



    public Map<String, Object> toDTO(){
        Map<String, Object> dto = new LinkedHashMap<String, Object>();
        dto.put("id", this.getId());
        dto.put("player", this.getPlayer().toDTO());
        return dto;
    }


}
