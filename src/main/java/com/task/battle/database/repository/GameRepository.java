package com.task.battle.database.repository;

import com.task.battle.database.model.Game;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

@Repository
public interface GameRepository extends JpaRepository<Game, Long> {

    @Query(value = "SELECT * FROM Game g WHERE g.is_finished = false ORDER BY g.id DESC LIMIT 1", nativeQuery = true)
    Game findActiveGame();
}
