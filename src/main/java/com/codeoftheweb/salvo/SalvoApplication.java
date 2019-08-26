package com.codeoftheweb.salvo;

import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;

import java.util.Date;

@SpringBootApplication
public class SalvoApplication {

	public static void main(String[] args) {
		SpringApplication.run(SalvoApplication.class, args);
	}

	@Bean
	public CommandLineRunner initData(PlayerRepository playerRepository, GameRepository gameRepository, GamePlayerRepository gamePlayerRepository){
		return(arg) -> {

			Player player1 = new Player("j.bauer@ctu.gov");
			playerRepository.save(player1);

			Date date1 = new Date();
			Date date2 = Date.from(date1.toInstant().plusSeconds(3600));
			Game game1 = new Game();
			gameRepository.save(game1);

			GamePlayer gamePlayer1 = new GamePlayer();
			gamePlayerRepository.save(gamePlayer1);
		};
	}

}
