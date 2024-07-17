package com.task.battle.database.model.unit;

import com.task.battle.data.PlayerColorEnum;
import com.task.battle.data.Position;
import com.task.battle.database.model.Player;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Objects;

@Entity
@Data
@AllArgsConstructor
@NoArgsConstructor
@Inheritance(strategy = InheritanceType.SINGLE_TABLE)
@DiscriminatorColumn(name="unit_type", discriminatorType = DiscriminatorType.STRING)
public class Unit {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    Long id;

    @Embedded
    Position position;

    @Enumerated(EnumType.STRING)
    PlayerColorEnum color;

    @ManyToOne
    @JoinColumn(name = "player_id")
    Player player;

    boolean isDestroyed;
    int movesCount;
}
