package com.task.battle.database.repository;

import com.task.battle.database.model.CommandHistory;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface CommandHistoryRepository extends JpaRepository<CommandHistory, Long> {
}
