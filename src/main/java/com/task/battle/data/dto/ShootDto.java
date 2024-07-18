package com.task.battle.data.dto;

import com.task.battle.data.PlayerColorEnum;
import com.task.battle.data.Position;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ShootDto extends ColorDto{
    Long unitId;
    Position destination;
}
