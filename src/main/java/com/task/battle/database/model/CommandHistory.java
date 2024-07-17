package com.task.battle.database.model;

import com.task.battle.database.model.unit.Unit;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Entity
@Data
@AllArgsConstructor
@NoArgsConstructor
public class CommandHistory {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    Long id;

    @ManyToOne
    @JoinColumn(name = "game_id")
    Game game;

    @ManyToOne
    @JoinColumn(name = "unit_id")
    Unit unit;

    String command;
    String details;

    LocalDateTime date;
}
