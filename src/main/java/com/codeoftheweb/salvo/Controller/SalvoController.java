package com.codeoftheweb.salvo.Controller;

import com.codeoftheweb.salvo.Model.*;
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
    private GameRepository gameRepository;

    @Autowired
    private GamePlayerRepository gamePlayerRepository;

    @Autowired
    private PlayerRepository playerRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Autowired
    private ShipRepository shipRepository;

    @Autowired
    private SalvoRepository salvoRepository;

    @RequestMapping("/games")
    public Map<String, Object> getGamesList(Authentication authentication) {
        Map<String, Object> dto = new LinkedHashMap<>();
        if (!isGuest(authentication)) {
            Player player = playerRepository.findByUserName(authentication.getName());
            dto.put("player", player.toDTO());
        } else {
            dto.put("player", "Guest");
        }

        dto.put("games", gameRepository
                .findAll()
                .stream()
                .map(game -> game.toDTO())
                .collect(Collectors.toList()));

        return dto;
    }

    private List<Object> getGamesPlayersList(Set<GamePlayer> gamePlayers) {
        return gamePlayers
                .stream()
                .map(gamePlayer -> gamePlayer.toDTO())
                .collect(Collectors.toList());
    }

    private List<Object> getShipsList(Set<Ship> ships) {
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
    public ResponseEntity<String> register(
            @RequestParam String userName, @RequestParam String password) {

        if (userName.isEmpty() || password.isEmpty()) {
            return new ResponseEntity<>("Well, well, well, it appears there're 2 (TWO) inputs to fill in. Please and thank you.", HttpStatus.FORBIDDEN);
        }

        if (playerRepository.findByUserName(userName) != null) {
            return new ResponseEntity<>("Be (even more) original! This username's already taken!", HttpStatus.FORBIDDEN);
        }
        playerRepository.save(new Player(userName, passwordEncoder.encode(password)));
        return new ResponseEntity<>(HttpStatus.CREATED);
    }
    @RequestMapping(path="/games", method = RequestMethod.POST)
    public ResponseEntity<Map<String, Object>>createGame(Authentication authentication){
        if(isGuest(authentication)){
            return new ResponseEntity<>(MakeMap("error", "Hey, who's this? We need to identify you to pair you up."), HttpStatus.FORBIDDEN);
        } else {
            Game game = gameRepository.save(new Game());
            Player player = playerRepository.findByUserName(authentication.getName());
            GamePlayer gamePlayer = gamePlayerRepository.save(new GamePlayer(player, game));
            return new ResponseEntity<>(MakeMap("gpid", gamePlayer.getId()), HttpStatus.CREATED);
        }
    }
    @RequestMapping(path = "/games/{id}/players", method = RequestMethod.POST)
    public ResponseEntity<Map<String, Object>>joinGame(@PathVariable Long id, Authentication authentication){
        Game game = gameRepository.findById(id).orElse(null);
        Player player = playerRepository.findByUserName(authentication.getName());
        if(game == null){
            return new ResponseEntity<>(MakeMap("error", "This game is like air resistance to physics teachers. Inexistent."), HttpStatus.FORBIDDEN);
        }
        if(isGuest(authentication)){
            return new ResponseEntity<>(MakeMap("error", "Who're you? We'll need your ID to let you in."), HttpStatus.FORBIDDEN);
        }
        if(game.getGamePlayers().size() == 2){
            return new ResponseEntity<>(MakeMap("error", "Too late. A few moments too late. Only two can play this game."), HttpStatus.FORBIDDEN);
        }
        if(game.getGamePlayers().stream().map(gamePlayer -> gamePlayer.getPlayer().getUserName()).collect(Collectors.toList()).contains(authentication.getName())){
            return new ResponseEntity<>(MakeMap("error", "Either raise or get razed. You'll need to use another account if you want both in the same game."), HttpStatus.FORBIDDEN);
        }
        GamePlayer gamePlayer = new GamePlayer(player, game);
        gamePlayerRepository.save(gamePlayer);

        return new ResponseEntity<>(MakeMap("gpid", gamePlayer.getId()), HttpStatus.CREATED);
    }
    @RequestMapping(path = "/games/players/{gpid}/ships", method = RequestMethod.POST)
    public ResponseEntity<Map<String, Object>> placedShips(@PathVariable Long gpid, Authentication authentication, @RequestBody List<Ship> ships){
        GamePlayer gamePlayer = gamePlayerRepository.findById(gpid).orElse(null);
        Player player = playerRepository.findByUserName(authentication.getName());
        if(isGuest(authentication)){
            return new ResponseEntity<>(MakeMap("error", "Access denied. Please log in. "), HttpStatus.UNAUTHORIZED);
        }
        if(gamePlayer == null) {
            return new ResponseEntity<>(MakeMap("error", "There's no game player."), HttpStatus.UNAUTHORIZED);
        }
        if(player.getId() != gamePlayer.getPlayer().getId()){
            return new ResponseEntity<>(MakeMap("error", "You're not this one, get outta'ere!"), HttpStatus.UNAUTHORIZED);
        }
        if(gamePlayer.getShips().size() > 0){
            return new ResponseEntity<>(MakeMap("error", "Ships already placed."), HttpStatus.FORBIDDEN);
        }
        ships.stream().map(ship ->
            shipRepository.save(new Ship (ship.getShipType(), ship.getShipLocation(), gamePlayer))
        ).collect(Collectors.toList());
        return new ResponseEntity<>(MakeMap("success", "Ships placed. The dies are thrown."), HttpStatus.CREATED);
    }
    @RequestMapping(path = "/games/players/{gpid}/salvoes", method = RequestMethod.POST)
    public ResponseEntity<Map<String, Object>> firedSalvoes(@PathVariable Long gpid, Authentication authentication, @RequestBody Salvo salvo ){
        GamePlayer gamePlayer = gamePlayerRepository.findById(gpid).orElse(null);
        Player player = playerRepository.findByUserName(authentication.getName());
        GamePlayer opponent = gamePlayer.getGame().getGamePlayers().stream().filter(gp -> gp.getId() != gpid).findFirst().get();
        if(isGuest(authentication)){
            return new ResponseEntity<>(MakeMap("error", "Who TF are you, identify yourself"), HttpStatus.UNAUTHORIZED);
        }
        if(player.getId() != gamePlayer.getPlayer().getId()){
            return new ResponseEntity<>(MakeMap("error", "Mind your own business!"), HttpStatus.FORBIDDEN);
        }
        if(gamePlayer.getId() == null){
            return new ResponseEntity<>(MakeMap("error", "Intruder alert!"), HttpStatus.FORBIDDEN);
        }
        if(salvo.getSalvoLocation().size() < 5) {
        return new ResponseEntity<>(MakeMap("error", "Quis eris?"), HttpStatus.FORBIDDEN);
        } else if(!isThisPlayersTurn(salvo, gamePlayer.getSalvoes())){
            salvo.setTurnNumber(gamePlayer.getSalvoes().size() + 1);
            salvo.setGamePlayer(gamePlayer);
            salvoRepository.save(salvo);
            return new ResponseEntity<>(MakeMap("success", "FIREEEEEEEE!"), HttpStatus.CREATED);
        } else {
            return new ResponseEntity<>(MakeMap("error", "Wait for your turn, you humanised impatience!"), HttpStatus.FORBIDDEN);
        }
    }

    private boolean isGuest(Authentication authentication) {
        return authentication == null || authentication instanceof AnonymousAuthenticationToken;
    }

    private Map<String,Object> MakeMap (String key, Object value){
        Map<String, Object> mapaCreado = new LinkedHashMap<>();
        mapaCreado.put (key, value);
        return mapaCreado;
    }

    private boolean isThisPlayersTurn(Salvo newSalvo, Set<Salvo> playersSalvoes){
        boolean isPlayersTurn = false;
        for(Salvo salvo: playersSalvoes){
            if(salvo.getTurnNumber() == newSalvo.getTurnNumber()){
                isPlayersTurn = true;
            }
        }
        return isPlayersTurn;
    }
}