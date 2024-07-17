package com.task.battle.data.dto;

import com.task.battle.data.Position;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class MoveDto {
    Long unitId;
    Position destination;
}
