package Pbo.GameStore.services;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import Pbo.GameStore.Models.Game;

public interface GameRepository extends JpaRepository<Game, Integer> {
    List<Game> findByGenre(String genre);
}

