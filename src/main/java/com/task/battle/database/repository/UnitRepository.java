package com.task.battle.database.repository;

import com.task.battle.database.model.unit.Unit;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UnitRepository extends JpaRepository<Unit, Long> {
}
