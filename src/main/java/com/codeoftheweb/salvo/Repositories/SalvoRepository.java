package com.codeoftheweb.salvo.Repositories;

import com.codeoftheweb.salvo.Model.Game;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.rest.core.annotation.RepositoryRestResource;

@RepositoryRestResource
public interface SalvoRepository extends JpaRepository<Game, Long> {
}
