package com.codeoftheweb.salvo.Controller;

import com.codeoftheweb.salvo.Model.GamePlayer;
import com.codeoftheweb.salvo.Model.Player;
import com.codeoftheweb.salvo.Model.Ship;
import com.codeoftheweb.salvo.Repositories.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AnonymousAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;

import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api")
public class SalvoController {

    @Autowired
    GameRepository gameRepository;

    @Autowired
    GamePlayerRepository gamePlayerRepository;

    @Autowired
    PlayerRepository playerRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Autowired
    private Authentication authentication;

    @RequestMapping("/games")
    private boolean Guest(Authentication authentication) {
        return authentication == null || authentication instanceof AnonymousAuthenticationToken;
    }
    public Map<String, Object> getGamesList() {
        Map<String, Object> dto = new LinkedHashMap<>();
        if (Guest(authentication)) {
            Map<String, Object> guest = new LinkedHashMap<>();
            guest.put("user", "guest");
            dto.put("players", guest);
        } else {
            Player player = playerRepository.findByUserName(authentication.getName());
            dto.put("players", player);
            dto.put("games", gameRepository
                    .findAll()
                    .stream()
                    .map(game -> game.toDTO())
                    .collect(Collectors.toList()));
        }
        return dto;
    } 
    public List<Object> getGamesPlayersList(Set<GamePlayer> gamePlayers) {
        return gamePlayers
                .stream()
                .map(gamePlayer -> gamePlayer.toDTO())
                .collect(Collectors.toList());
    }

    public List<Object> getShipsList(Set<Ship> ships) {
        return ships
                .stream()
                .map(ship -> ship.toDTO())
                .collect(Collectors.toList());
    }


    @RequestMapping("/game_view/{id}")
    public Map<String, Object> getGameView(@PathVariable Long id) {
        return gameViewDTO(gamePlayerRepository.getOne(id));
    }

    private Map<String, Object> gameViewDTO(GamePlayer gamePlayer) {
        Map<String, Object> dto = gamePlayer.getGame().toDTO();
        dto.put("ships", getShipsList(gamePlayer.getShips()));
        dto.put("salvoes", gamePlayer.getGame().getGamePlayers().stream()
                .flatMap(gp -> gp.getSalvoes()
                        .stream()
                        .map(salvo -> salvo.toDTO())
                )
                .collect(Collectors.toList())
        );
        return dto;
    }

    @RequestMapping("/leaderboard")
    public List<Map<String, Object>> getLeaderboard() {
        return playerRepository.findAll()
                .stream()
                .map(player -> player.LeaderboardDTO())
                .collect(Collectors.toList());
    }

    @RequestMapping(path = "/players", method = RequestMethod.POST)
    public ResponseEntity<Object> register(
            @RequestParam String userName, @RequestParam String password) {

        if (userName.isEmpty() || password.isEmpty()) {
            return new ResponseEntity<>("Missing data", HttpStatus.FORBIDDEN);
        }

        if (playerRepository.findByUserName(userName) !=  null) {
            return new ResponseEntity<>("Name already in use", HttpStatus.FORBIDDEN);
        }

        playerRepository.save(new Player(userName, passwordEncoder.encode(password)));
        return new ResponseEntity<>(HttpStatus.CREATED);
    }
}