package com.codeoftheweb.salvo.Controller;

import com.codeoftheweb.salvo.Model.Game;
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

    private boolean isGuest(Authentication authentication) {
        return authentication == null || authentication instanceof AnonymousAuthenticationToken;
    }

    @RequestMapping("/games")
    public Map<String, Object> getGamesList(Authentication authentication) {
        Map<String, Object> dto = new LinkedHashMap<>();
        if (!isGuest(authentication)) {
            Player player = playerRepository.findByUserName(authentication.getName());
            dto.put("players", player.toDTO());
        }

        dto.put("games", gameRepository
                .findAll()
                .stream()
                .map(game -> game.toDTO())
                .collect(Collectors.toList()));

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

    public Map<String,Object> MakeMap (String key, Object value){
        Map<String, Object> mapaCreado = new LinkedHashMap<>();
        mapaCreado.put (key, value);
        return mapaCreado;
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
    public ResponseEntity<String> register(
            @RequestParam String userName, @RequestParam String password) {

        if (userName.isEmpty() || password.isEmpty()) {
            return new ResponseEntity<>("There're 2 (two) inputs for you to complete. Please and thank you.", HttpStatus.FORBIDDEN);
        }

        if (playerRepository.findByUserName(userName) != null) {
            return new ResponseEntity<>("Be original! This name's taken.", HttpStatus.FORBIDDEN);
        }
        playerRepository.save(new Player(userName, passwordEncoder.encode(password)));
        return new ResponseEntity<>(HttpStatus.CREATED);
    }
    @RequestMapping(path="/games", method = RequestMethod.POST)
    public ResponseEntity<Map<String, Object>>createGame(Authentication authentication){
        if(isGuest(authentication)){
            return new ResponseEntity<>(MakeMap("error", "Hello, stranger. We seek identification, so we need you to log in or sign up, please."), HttpStatus.FORBIDDEN);
        } else {
            Game game = gameRepository.save(new Game());
            Player player = playerRepository.findByUserName(authentication.getName());
            GamePlayer gamePlayer = gamePlayerRepository.save(new GamePlayer(player, game));
            return new ResponseEntity<>(MakeMap("gpid", gamePlayer.getId()), HttpStatus.CREATED);
        }
    }
    @RequestMapping(path = "/games/{id}/players")
    public ResponseEntity<Map<String, Object>>joinGame(@RequestParam Long id, Authentication authentication){
        Game game = gameRepository.findById(id).orElse(null);
        if(game == null){
            return new ResponseEntity<>(MakeMap("error", "This game is like air resistance to physics teachers. Inexistent."), HttpStatus.FORBIDDEN);
        }
        if(isGuest(authentication)){
            return new ResponseEntity<>(MakeMap("error", "Who're you? It appears you're not logged in."), HttpStatus.FORBIDDEN);
        }
        if(game.getGamePlayers().size() == 2){
            return new ResponseEntity<>(MakeMap("error", "Too late. Be quicker next time. Game's already full."), HttpStatus.FORBIDDEN);
        }
        if(game.getGamePlayers().stream().map(gamePlayer -> gamePlayer.getPlayer().getUserName()).collect(Collectors.toList()).contains(authentication.getName())){
            return new ResponseEntity<>(MakeMap("error", "Raise above others or get razed, you can't get both unless you have another account."), HttpStatus.FORBIDDEN);
        }
        return new ResponseEntity<>(MakeMap("success", "You're in. Good Luck."), HttpStatus.CREATED);
    }
    @RequestMapping(path = "/games/{id}/players")
    public ResponseEntity<Map<String, Object>> returnToGame(@RequestParam Long id, Authentication authentication){
        Game game = gameRepository.findById(id).orElse(null);
        if (game.getGamePlayers().stream().map(gamePlayer -> gamePlayer.getPlayer().getUserName()).collect(Collectors.toList()).contains(authentication)){
            return new ResponseEntity<>(MakeMap("success", "Back on track."), HttpStatus.ACCEPTED);
        }
        return new ResponseEntity<>(MakeMap("error", "You're not playing this game, spectator. Mind your own business."), HttpStatus.FORBIDDEN);
    }
}