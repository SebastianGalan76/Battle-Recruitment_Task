package com.task.battle.database.model;

import com.task.battle.data.BoardSize;
import com.task.battle.data.PlayerColorEnum;
import com.task.battle.database.model.unit.Unit;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.List;

@Entity
@Data
@AllArgsConstructor
@NoArgsConstructor
public class Game {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    Long id;

    @Embedded
    BoardSize boardSize;

    boolean isFinished;

    @OneToMany(mappedBy = "game", cascade = CascadeType.ALL, fetch = FetchType.EAGER)
    List<Player> players = new ArrayList<>();

    @OneToMany(mappedBy = "game", cascade = CascadeType.ALL, fetch = FetchType.EAGER)
    List<Unit> units = new ArrayList<>();

    @OneToMany(mappedBy = "game", cascade = CascadeType.ALL, fetch = FetchType.EAGER)
    List<CommandHistory> commandHistory = new ArrayList<>();

    @OneToOne
    @JoinColumn(name = "winner_id")
    Player winner;

    public Player getPlayer(PlayerColorEnum color){
        for(Player player:players){
            if(player.getColor() == color){
                return player;
            }
        }

        return null;
    }
}
