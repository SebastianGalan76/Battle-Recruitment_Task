package com.task.battle.data.dto;

import com.task.battle.data.PlayerColorEnum;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ColorDto {
    PlayerColorEnum color;
}
