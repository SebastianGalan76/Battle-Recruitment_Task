package com.task.battle.database.model.unit;

import com.task.battle.data.BoardSize;
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
@EqualsAndHashCode(callSuper = true)
@DiscriminatorValue("CANNON")
public class Cannon extends Unit {
    @Override
    public boolean validateDestination(Position destination, CommandTypeEnum commandTypeEnum){
        if(commandTypeEnum == CommandTypeEnum.SHOOT){
            int dx = Math.abs(position.getX() - destination.getX());
            int dy = Math.abs(position.getY() - destination.getY());
            return (dx == 0 && dy > 0) || (dy == 0 && dx > 0)
                    || (dy == dx);
        }
        return false;
    }

    @Override
    public void calculatePossibleShots() {
        possibleShots.clear();

        int x = position.getX();
        int y = position.getY();

        BoardSize boardSize = player.getGame().getBoardSize();

        //Shoot up
        for(int i = y + 1;i<boardSize.getHeight();i++){
            possibleShots.add(new Position(x, i));
        }

        //Shoot down
        for(int i = y - 1;i>=0;i--){
            possibleShots.add(new Position(x, i));
        }

        //Shoot right
        for(int i = x + 1;i<boardSize.getWidth();i++){
            possibleShots.add(new Position(i, y));
        }

        //Shoot left
        for(int i = x - 1;i>=0;i--){
            possibleShots.add(new Position(i, y));
        }

        //Shoot diagonally up-right
        for (int i = 1; x + i < boardSize.getWidth() && y + i < boardSize.getHeight(); i++) {
            possibleShots.add(new Position(x + i, y + i));
        }

        //Shoot diagonally down-right
        for (int i = 1; x + i < boardSize.getWidth() && y - i >= 0; i++) {
            possibleShots.add(new Position(x + i, y - i));
        }

        //Shoot diagonally down-left
        for (int i = 1; x - i >= 0 && y - i >= 0; i++) {
            possibleShots.add(new Position(x - i, y - i));
        }

        //Shoot diagonally up-left
        for (int i = 1; x - i >= 0 && y + i < boardSize.getHeight(); i++) {
            possibleShots.add(new Position(x - i, y + i));
        }
    }

    @Override
    public int getCommandCooldown(CommandTypeEnum commandType){
        if (commandType == CommandTypeEnum.SHOOT) {
            return 13;
        }
        return 0;
    }
}
