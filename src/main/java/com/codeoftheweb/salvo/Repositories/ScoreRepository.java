package com.codeoftheweb.salvo.Repositories;

import com.codeoftheweb.salvo.Model.GamePlayer;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.rest.core.annotation.RepositoryRestResource;

@RepositoryRestResource
public interface ScoreRepository extends JpaRepository<GamePlayer, Long> {
}
