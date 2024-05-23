package ch.matias.m295_lb.repositories;

import ch.matias.m295_lb.models.Game;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface IGameRepository extends JpaRepository<Game, Integer> {
}
