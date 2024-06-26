package ch.matias.m295_lb.repositories;

import ch.matias.m295_lb.models.Game;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

@Repository
public interface IGameRepository extends JpaRepository<Game, Integer> {
    Optional<Game> findGameByName(String name);
    List<Game> findGamesByReleaseDate(LocalDate releaseDate);
}
