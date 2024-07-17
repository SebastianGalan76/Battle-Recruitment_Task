package com.task.battle.database.model.unit;

import com.task.battle.data.CommandTypeEnum;
import com.task.battle.data.Position;
import jakarta.persistence.DiscriminatorValue;
import jakarta.persistence.Entity;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

@Entity
@Data
@AllArgsConstructor
@NoArgsConstructor
@EqualsAndHashCode(callSuper = true)
@DiscriminatorValue("ARCHER")
public class Archer extends Unit {
    @Override
    public boolean validateDestination(Position destination, CommandTypeEnum commandTypeEnum){
        if(commandTypeEnum == CommandTypeEnum.MOVE){
            int dx = Math.abs(position.getX() - destination.getX());
            int dy = Math.abs(position.getY() - destination.getY());
            return (dx == 1 && dy == 0) || (dx == 0 && dy == 1);
        }
        if(commandTypeEnum == CommandTypeEnum.SHOOT){
            int dx = Math.abs(position.getX() - destination.getX());
            int dy = Math.abs(position.getY() - destination.getY());
            return (dx == 0 && dy > 0) || (dy == 0 && dx > 0);
        }

        return false;
    }

    @Override
    public int getCommandCooldown(CommandTypeEnum commandType){
        if (commandType == CommandTypeEnum.MOVE) {
            return 5;
        }
        if (commandType == CommandTypeEnum.SHOOT){
            return 10;
        }
        return 0;
    }
}
