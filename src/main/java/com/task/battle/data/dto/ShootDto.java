package com.task.battle.data.dto;

import com.task.battle.data.Position;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ShootDto {
    Long unitId;
    Position destination;
}
