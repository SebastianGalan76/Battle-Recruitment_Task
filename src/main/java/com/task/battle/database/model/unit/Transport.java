package com.task.battle.database.model.unit;

import com.task.battle.data.CommandTypeEnum;
import com.task.battle.data.Position;
import jakarta.persistence.DiscriminatorValue;
import jakarta.persistence.Entity;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

import java.util.Objects;

@Entity
@Data
@EqualsAndHashCode(callSuper = true)
@DiscriminatorValue("TRANSPORT")
public class Transport extends Unit {

    @Override
    public boolean validateDestination(Position destination, CommandTypeEnum commandTypeEnum){
       if(commandTypeEnum == CommandTypeEnum.MOVE){
           int dx = Math.abs(position.getX() - destination.getX());
           int dy = Math.abs(position.getY() - destination.getY());
           return (dx == 0 && (dy == 1 || dy == 2 || dy == 3)) || (dy == 0 && (dx == 1 || dx == 2 || dx == 3));
       }

       return false;
    }

    @Override
    public int getCommandCooldown(CommandTypeEnum commandType){
        if (commandType == CommandTypeEnum.MOVE) {
            return 7;
        }
        return 0;
    }
}
