package com.task.battle.database.model.unit;

import com.task.battle.data.BoardSize;
import com.task.battle.data.CommandTypeEnum;
import com.task.battle.data.Position;
import jakarta.persistence.DiscriminatorValue;
import jakarta.persistence.Entity;
import lombok.Data;
import lombok.EqualsAndHashCode;

@Entity
@Data
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

    @Override
    public void calculatePossibleMoves() {
        possibleMoves.clear();

        int x = position.getX();
        int y = position.getY();

        //Move left
        if (x - 1 >= 0) {
            possibleMoves.add(new Position(x - 1, y));
        }

        //Move right
        if (x + 1 < player.getGame().getBoardSize().getWidth()) {
            possibleMoves.add(new Position(x + 1, y));
        }

        //Move up
        if (y - 1 >= 0) {
            possibleMoves.add(new Position(x, y - 1));
        }

        //Move down
        if (y + 1 < player.getGame().getBoardSize().getHeight()) {
            possibleMoves.add(new Position(x, y + 1));
        }
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
    }
}
