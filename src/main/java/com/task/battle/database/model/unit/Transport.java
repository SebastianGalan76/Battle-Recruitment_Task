package com.task.battle.database.model.unit;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.task.battle.data.CommandTypeEnum;
import com.task.battle.data.Position;
import jakarta.persistence.DiscriminatorValue;
import jakarta.persistence.Entity;
import jakarta.persistence.Transient;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.List;
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
        calculatePossibleMoves();
       return false;
    }

    @Override
    public int getCommandCooldown(CommandTypeEnum commandType){
        if (commandType == CommandTypeEnum.MOVE) {
            return 7;
        }
        return 0;
    }

    public void calculatePossibleMoves() {
        possibleMoves.clear();

        int x = position.getX();
        int y = position.getY();

        //Move in the range 1-3 in each direction
        for (int i = 1; i <= 3; i++) {
            //Move left
            if (x - i >= 0) {
                possibleMoves.add(new Position(x - i, y));
            }

            //Move right
            if (x + i < player.getGame().getBoardSize().getWidth()) {
                possibleMoves.add(new Position(x + i, y));
            }

            //Move up
            if (y - i >= 0) {
                possibleMoves.add(new Position(x, y - i));
            }

            //Move down
            if (y + i < player.getGame().getBoardSize().getHeight()) {
                possibleMoves.add(new Position(x, y + i));
            }
        }
    }
}
