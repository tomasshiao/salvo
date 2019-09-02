package com.codeoftheweb.salvo;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api")
public class SalvoController {

    @Autowired
    GameRepository gameRepository;

    @RequestMapping("/games")
    public List<Object> getGamesList() {
        return gameRepository
                .findAll()
                .stream()
                .map(game -> makeGameDTO(game))
                .collect(Collectors.toList());
    }
    private Map<String, Object> makeGameDTO(Game game){
        Map<String, Object> dto = new LinkedHashMap<String, Object>();
        dto.put("id", game.getId());
        dto.put("creationDate",game.getCreationDate());
        dto.put("gamePlayers", getAllGamePlayers(game.getGamePlayers()));
        return dto;
    }
    private List<Object> getPlayerList(List<GamePlayer> gamePlayers){
        return gamePlayers
                .stream()
                .map(gamePlayer -> makeGameDTO(gamePlayer))
                .collect(Collectors.toList());
    }
}