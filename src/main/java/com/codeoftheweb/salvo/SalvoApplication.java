package com.codeoftheweb.salvo;

import com.codeoftheweb.salvo.Model.*;
import com.codeoftheweb.salvo.Repositories.*;
import org.apache.catalina.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.Arrays;
import java.util.Date;
import java.util.HashSet;
import java.util.Set;

@SpringBootApplication
public class SalvoApplication {

	public static void main(String[] args) {
		SpringApplication.run(SalvoApplication.class, args);
	}

	//@Bean
	//public PasswordEncoder passwordEncoder(){
	// return PasswordEncoderFactories.createDelegatingPasswordEncoders();
	// }
	@Bean
	public CommandLineRunner initData(PlayerRepository playerRepository,
									  GameRepository gameRepository,
									  GamePlayerRepository gamePlayerRepository,
									  ShipRepository shipRepository,
									  SalvoRepository salvoRepository,
									  ScoreRepository scoreRepository){
		return(arg) -> {

			Player player1 = new Player("j.bauer@ctu.gov", "24");
			Player player2 = new Player("c.obrian@ctu.gov", "42");
			Player player3 = new Player("kim_bauer@gmail.com", "kb");
			Player player4 = new Player("t.almeida@ctu.gov", "mole");
			playerRepository.save(player1);
			playerRepository.save(player2);
			playerRepository.save(player3);
			playerRepository.save(player4);


			Date date1 = new Date();
			Date date2 = Date.from(date1.toInstant().plusSeconds(3600));
			Date date3 = Date.from(date2.toInstant().plusSeconds(3600));
			Game game1 = new Game(date1);
			Game game2 = new Game(date2);
			Game game3 = new Game(date3);
			gameRepository.save(game1);
			gameRepository.save(game2);
			gameRepository.save(game3);

			GamePlayer gamePlayer1 = new GamePlayer(player1, game1);
			GamePlayer gamePlayer2 = new GamePlayer(player2, game1);
			GamePlayer gamePlayer3 = new GamePlayer(player3, game2);
			GamePlayer gamePlayer4 = new GamePlayer(player4, game2);
			GamePlayer gamePlayer5 = new GamePlayer(player1, game3);
			GamePlayer gamePlayer6 = new GamePlayer(player3, game3);
			gamePlayerRepository.save(gamePlayer1);
			gamePlayerRepository.save(gamePlayer2);
			gamePlayerRepository.save(gamePlayer3);
			gamePlayerRepository.save(gamePlayer4);
			gamePlayerRepository.save(gamePlayer5);
			gamePlayerRepository.save(gamePlayer6);

			Set<String> shipL1 = new HashSet<>(Arrays.asList("H2","H3","H4"));
			Set<String> shipL2 = new HashSet<>(Arrays.asList("E1","F1","G1"));
			Set<String> shipL3 = new HashSet<>(Arrays.asList("B4","B5"));
			Set<String> shipL4 = new HashSet<>(Arrays.asList("B5","C5","D5"));
			Set<String> shipL5 = new HashSet<>(Arrays.asList("F1","F2"));
			Ship ship1 = new Ship("Destroyer",shipL1,gamePlayer1);
			Ship ship2 = new Ship("Submarine",shipL2,gamePlayer1);
			Ship ship3 = new Ship("Patrol Boat",shipL3,gamePlayer1);
			Ship ship4 = new Ship("Destroyer",shipL4,gamePlayer2);
			Ship ship5 = new Ship("Destroyer",shipL5,gamePlayer2);

			shipRepository.saveAll(Arrays.asList(ship1,ship2,ship3,ship4,ship5));

			Set<String> salvoL1 = new HashSet<>(Arrays.asList("H2","H3","H4"));
			Set<String> salvoL2 = new HashSet<>(Arrays.asList("E1","F1","G1"));
			Set<String> salvoL3 = new HashSet<>(Arrays.asList("B4","B5"));
			Set<String> salvoL4 = new HashSet<>(Arrays.asList("B5","C5","D5"));
			Set<String> salvoL5 = new HashSet<>(Arrays.asList("F1","F2"));
			Salvo salvo1 = new Salvo(gamePlayer1,1, salvoL1);
			Salvo salvo2 = new Salvo(gamePlayer1,2, salvoL2);
			Salvo salvo3 = new Salvo(gamePlayer1,3, salvoL3);
			Salvo salvo4 = new Salvo(gamePlayer2,1, salvoL4);
			Salvo salvo5 = new Salvo(gamePlayer2,2, salvoL5);

			salvoRepository.saveAll(Arrays.asList(salvo1,salvo2,salvo3,salvo4,salvo5));

			Score score1 = new Score(1, game1, player1);
			Score score2 = new Score(0, game1, player2);
			Score score3 = new Score(1, game2, player2);
			Score score4 = new Score(0, game2, player4);
			Score score5 = new Score(0.5, game3, player1);
			Score score6 = new Score(0.5, game3, player2);

			scoreRepository.save(score1);
			scoreRepository.save(score2);
			scoreRepository.save(score3);
			scoreRepository.save(score4);
			scoreRepository.save(score5);
			scoreRepository.save(score6);
		};
	}

}
/*
 @Configuration
 class WebSecurityConfiguration extends GlobalAuthenticationConfigurerAdapter{
 @Autowired
 PlayerRepository playerRepository
 @Override
 public void init(AuthenticationManagerBuilder auth) throws Exception {
 auth.userDetailsService(inputName ->{
 Player player = playerRepository.findByUserName(inputName);
 if(player != null){
 return new User(player.getUserName(), player.getPassword()),
 AuthorityUtils.createAuthorityList("USER");)
 }else{
 throw new UsernameNotFoundException("unknown user: " + inputName);
 }
 });
 }
}*/

