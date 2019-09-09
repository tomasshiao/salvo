package com.codeoftheweb.salvo.Controller;

import com.codeoftheweb.salvo.Model.GamePlayer;
import com.codeoftheweb.salvo.Model.Ship;
import com.codeoftheweb.salvo.Repositories.GamePlayerRepository;
import com.codeoftheweb.salvo.Repositories.GameRepository;
import com.codeoftheweb.salvo.Repositories.ShipRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

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
    ShipRepository shipRepository;

    @RequestMapping("/games")
    public List<Object> getGamesList() {
        return gameRepository
                .findAll()
                .stream()
                .map(game -> game.toDTO())
                .collect(Collectors.toList());
    }

    public List<Object> getGamesPlayersList(Set<GamePlayer> gamePlayers) {
        return gamePlayerRepository
                .findAll()
                .stream()
                .map(gamePlayer -> gamePlayer.toDTO())
                .collect(Collectors.toList());
    }


    @RequestMapping("/game_view/{id}")
    public Map<String, Object> getGameView(@PathVariable Long id) {
        return gameViewDTO(gamePlayerRepository.getOne(id));
    }

    private Map<String, Object> gameViewDTO(GamePlayer gamePlayer) {
        Map<String, Object> dto = new LinkedHashMap<>();

        dto.put("id", gamePlayer.getGame().getId());
        dto.put("creationDate", gamePlayer.getGame().getCreationDate());
        dto.put("gamePlayers", getGamesPlayersList(gamePlayer.getGame().getGamePlayers()));
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

    public List<Object> getShipsList(Set<Ship> ships) {
        return shipRepository
                .findAll()
                .stream()
                .map(ship -> ship.toDTO())
                .collect(Collectors.toList());
    }

}