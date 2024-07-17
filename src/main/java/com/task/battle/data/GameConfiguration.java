package com.task.battle.data;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class GameConfiguration {
    BoardSize boardSize;

    int archerAmount;
    int transportAmount;
    int cannonAmount;
}
