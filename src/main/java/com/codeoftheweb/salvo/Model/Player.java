package com.codeoftheweb.salvo.Model;


import org.hibernate.annotations.GenericGenerator;
import javax.persistence.*;
import java.util.LinkedHashMap;
import java.util.LinkedHashSet;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

@Entity
public class Player {
   @Id
   @GeneratedValue(strategy = GenerationType.AUTO, generator = "native")
   @GenericGenerator(name="native", strategy="native")

    private Long id;
    private String userName;

    @OneToMany(mappedBy = "player", fetch = FetchType.EAGER)
    private Set<GamePlayer> gamePlayers = new LinkedHashSet<>();

    @OneToMany(mappedBy = "player", fetch = FetchType.EAGER)
    private Set<Score> scores = new LinkedHashSet<>();


    //private String password


    public Player() {
    }

    public Player(String userName) {
        this.userName = userName;
        //this.password = password
    }

    public Long getId() {
        return id;
    }

    public String getUserName() {
        return userName;
    }

    //public String getPassword(){return password;}

    public Set<GamePlayer> getGamePlayers() {
        return gamePlayers;
    }

    public Set<Score> getScores() {
        return scores;
    }

    public Set<Score> getWins() {
        return getScores().stream().filter(score -> score.getScore() == 1).collect(Collectors.toSet());
    }

    public Set<Score> getTies() {
        return getScores().stream().filter(score -> score.getScore() == 0.5).collect(Collectors.toSet());
    }

    public Set<Score> getLosses() {
        return getScores().stream().filter(score -> score.getScore() == 0).collect(Collectors.toSet());
    }

    public double getTotalScore() {
        return 0.5 * getTies().size() + getWins().size();
    }

    public Map<String, Object> toDTO(){
        Map<String, Object> dto = new LinkedHashMap<String, Object>();
        dto.put("id", this.getId());
        dto.put("email",this.getUserName());
        return dto;
    }
    public Map<String, Object> LeaderboardDTO(){
        Map<String, Object> dto = new LinkedHashMap<>();
        dto.put("Id", getId());
        dto.put("Username", getUserName());
        dto.put("Games", getScores().size());
        dto.put("Total", getTotalScore());
        dto.put("Wins", getWins().size());
        dto.put("Ties", getTies().size());
        dto.put("Losses", getLosses().size());
        return dto;
    }
}
