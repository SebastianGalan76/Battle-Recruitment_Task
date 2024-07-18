package com.task.battle.database.repository;

import com.task.battle.data.PlayerColorEnum;
import com.task.battle.database.model.unit.Unit;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface UnitRepository extends JpaRepository<Unit, Long> {
    @Query("SELECT u FROM Unit u WHERE u.game.id = :gameId")
    List<Unit> findUnitsByGameId(Long gameId);

    @Query("SELECT u FROM Unit u WHERE u.game.id = :gameId AND u.color = :color")
    List<Unit> findUnitsByGameIdAndColor(Long gameId, PlayerColorEnum color);
}
