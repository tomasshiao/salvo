package com.codeoftheweb.salvo.Model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import org.hibernate.annotations.GenericGenerator;

import javax.persistence.*;
import java.lang.reflect.Array;
import java.util.*;
import java.util.stream.Collectors;

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
    private Set<Ship> ships = new HashSet<>();

    @OneToMany(mappedBy = "gamePlayer", fetch = FetchType.EAGER)
    private Set<Salvo> salvoes = new HashSet<>();

    @JsonIgnore
    public Game getGame(){ return game; }
    @JsonIgnore
    public Player getPlayer() {
        return player;
    }
    @JsonIgnore
    public Set<Ship> getShips(){
        return ships;
    }
    @JsonIgnore
    public Set<Salvo> getSalvoes(){
        return salvoes;
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

    public Date getJoinTime(){
        return joinTime;
    }

    public Map<String, Object> toDTO(){
        GamePlayer opponent = this.getGame().getGamePlayers().stream().filter(oponente -> this.getId() != oponente.getId()).findFirst().orElse(null);
        Map<String, Object> dto = new LinkedHashMap<String, Object>();
        dto.put("gpid", this.getId());
        dto.put("player", this.getPlayer().toDTO());
        dto.put("joinTime", this.getJoinTime());
        dto.put("hasShips", this.hasShips());
        //dto.put("hits", hitsDTO(this, opponent));
        return dto;
    }

   private Map<String, Object> hitsDTO(GamePlayer self, GamePlayer opponent){
        Map<String, Object> dto = new LinkedHashMap<>();
        dto.put("gotHit", getHits(self, opponent));
        dto.put("myHits", getHits(self, opponent));
        return dto;
    }

    private List<Map> getHits(GamePlayer self, GamePlayer opponent) {
        List<Map> dto = new ArrayList<>();

        int carrierDamage = 0;
        int destroyerDamage = 0;
        int patrolboatDamage = 0;
        int submarineDamage = 0;
        int battleshipDamage = 0;
        List<String> carrierLocations = new ArrayList<>();
        List<String> destroyerLocations = new ArrayList<>();
        List<String> submarineLocations = new ArrayList<>();
        List<String> patrolboatLocations = new ArrayList<>();
        List<String> battleshipLocations = new ArrayList<>();

        
        return dto;
    }
   private String hasShips(){
       if(this.getShips().size() > 0){
           return "YES";
       }else{
           return "NO";
       }
   }
}
