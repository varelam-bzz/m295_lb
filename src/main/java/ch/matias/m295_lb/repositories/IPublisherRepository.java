package ch.matias.m295_lb.repositories;

import ch.matias.m295_lb.models.Publisher;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface IPublisherRepository extends JpaRepository<Publisher, Integer> {
    Optional<Publisher> findPublisherByName(String name);
}
