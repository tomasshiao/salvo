package com.codeoftheweb.salvo;

import com.fasterxml.jackson.annotation.JsonIgnore;
import org.hibernate.annotations.GenericGenerator;

import javax.persistence.*;
import java.util.*;

@Entity
public class Game {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO, generator = "native")
    @GenericGenerator(name = "native", strategy = "native")

    private Long id;
    private Date creationDate;

    @OneToMany(mappedBy = "game", fetch = FetchType.EAGER)
    private Set<GamePlayer> gamePlayers;

    public Game() {
    }

    public Game(Date creationDate) {
        this.creationDate = creationDate;
    }

    public Long getId() {
        return id;
    }

    public Date getCreationDate() {
        return creationDate;
    }

    public Set<GamePlayer> getGamePlayers() {
        return  gamePlayers;
    }
    public Map<String, Object> toDTO(){
        Map<String, Object> dto = new LinkedHashMap<String, Object>();
        dto.put("id", this.getId());
        dto.put("creationDate",this.getCreationDate());
        dto.put("gamePlayers", this.getGamePlayers().stream().map(gamePlayer -> gamePlayer.toDTO()));
        return dto;
    }

}
