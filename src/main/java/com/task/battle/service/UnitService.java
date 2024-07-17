package com.task.battle.service;

import com.task.battle.data.BoardSize;
import com.task.battle.data.Position;
import com.task.battle.database.model.Game;
import com.task.battle.database.model.Player;
import com.task.battle.database.model.unit.Archer;
import com.task.battle.database.model.unit.Cannon;
import com.task.battle.database.model.unit.Transport;
import com.task.battle.database.model.unit.Unit;
import com.task.battle.database.repository.UnitRepository;
import com.task.battle.exception.UnitActionException;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@AllArgsConstructor
public class UnitService {
    final UnitRepository unitRepository;

    public void moveUnit(Long unitId, Position destination) throws UnitActionException {
        Unit unit = unitRepository.findById(unitId).orElseThrow(() -> new UnitActionException("There is no unit with given id in our database!"));
        if(unit.isDestroyed()){
            throw new UnitActionException("The unit is already destroyed!");
        }

        Player player = unit.getPlayer();
        if(!player.canMove()){
            throw new UnitActionException("You cannot move yet. Wait a few seconds!");
        }

        if(!isValidMove(unit, destination, player.getGame())){
            throw new UnitActionException("Destination position is incorrect!");
        }

        unit.setPosition(destination);
        unit.setMovesCount(unit.getMovesCount() + 1);
        unitRepository.save(unit);
    }

    public void shoot(Long unitId, Position destination) throws UnitActionException {
        Unit unit = unitRepository.findById(unitId).orElseThrow(() -> new UnitActionException("There is no unit with given id in our database!"));
        if(unit.isDestroyed()){
            throw new UnitActionException("The unit is already destroyed!");
        }

        Player player = unit.getPlayer();
        if(!player.canMove()){
            throw new UnitActionException("You cannot shoot yet. Wait a few seconds!");
        }

        Game game = player.getGame();
        if(!isValidShoot(unit, destination, game)){
            throw new UnitActionException("Destination position is incorrect!");
        }

        // Check for hitting other units
        for(Player otherPlayer:game.getPlayers()){
            for (Unit otherUnit : otherPlayer.getUnits()) {
                if(otherUnit.isDestroyed()){
                    continue;
                }

                if (otherUnit.getPosition().equals(destination)) {
                    otherUnit.setDestroyed(true);
                    unitRepository.save(otherUnit);
                }
            }
        }
    }

    private boolean isValidShoot(Unit unit, Position destination, Game game) {
        if(!isDestinationOnBoard(game.getBoardSize(), destination)){
            return false;
        }

        return unit.validateShooting(destination);
    }

    private boolean isValidMove(Unit unit, Position destination, Game game) {
        if(!isDestinationOnBoard(game.getBoardSize(), destination)){
            return false;
        }

        if(!unit.validateMovement(destination)){
            return false;
        }

        // Check for collisions with other units
        for(Player player:game.getPlayers()){
            for (Unit otherUnit : player.getUnits()) {
                if(otherUnit.isDestroyed()){
                    continue;
                }

                if (otherUnit.getPosition().equals(destination)) {
                    if (otherUnit.getColor().equals(unit.getColor())) {
                        return false;
                    } else {
                        //Destroy an enemy unit if it collides with our transport unit
                        if(unit instanceof Transport){
                            otherUnit.setDestroyed(true);
                            unitRepository.save(otherUnit);
                        }
                    }
                }
            }
        }

        return true;
    }

    private boolean isDestinationOnBoard(BoardSize boardSize, Position destination){
        return destination.getX() >= 0 && destination.getX() <= boardSize.getWidth()
                && destination.getY() >= 0 && destination.getY() <= boardSize.getHeight();
    }
}
