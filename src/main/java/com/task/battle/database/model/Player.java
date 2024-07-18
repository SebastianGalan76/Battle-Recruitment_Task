package com.task.battle.database.model;

import com.task.battle.data.PlayerColorEnum;
import com.task.battle.database.model.unit.Unit;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Entity
@Data
@AllArgsConstructor
@NoArgsConstructor
public class Player {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    Long id;

    @Enumerated(EnumType.STRING)
    PlayerColorEnum color;

    @OneToMany(mappedBy = "player", cascade = CascadeType.ALL, fetch = FetchType.EAGER)
    List<Unit> units = new ArrayList<>();

    @ManyToOne
    @JoinColumn(name = "game_id")
    Game game;

    @Column(name = "next_command_timestamp")
    LocalDateTime nextCommandTimestamp;

    public void setCooldown(int delay){
        nextCommandTimestamp = LocalDateTime.now().plusSeconds(delay);
    }

    public boolean canMove(){
        return LocalDateTime.now().isAfter(nextCommandTimestamp);
    }
}
