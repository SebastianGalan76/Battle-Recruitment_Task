package com.task.battle.data.dto;

import com.task.battle.data.PlayerColorEnum;
import com.task.battle.data.Position;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class UnitInformationDto {
    Long unitId;
    Position position;

    String type; //Unit type e.g. Cannon, Archer, Transport
    PlayerColorEnum color;
    String status; //Active or Destroyed
    int moveCount;

}
