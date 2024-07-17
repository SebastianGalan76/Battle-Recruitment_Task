package com.task.battle.service;

import com.task.battle.data.BoardSize;
import com.task.battle.data.Position;
import com.task.battle.database.model.Game;
import com.task.battle.database.model.Player;
import com.task.battle.database.model.unit.Archer;
import com.task.battle.database.model.unit.Cannon;
import com.task.battle.database.model.unit.Transport;
import com.task.battle.database.model.unit.Unit;
import com.task.battle.database.repository.PlayerRepository;
import com.task.battle.database.repository.UnitRepository;
import com.task.battle.exception.MoveUnitException;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.Objects;

@Service
@AllArgsConstructor
public class UnitService {
    final PlayerRepository playerRepository;
    final UnitRepository unitRepository;

    public void moveUnit(Long playerId, Long unitId, Position destination) throws MoveUnitException {
        Player player = playerRepository.findById(playerId).orElseThrow(() -> new MoveUnitException("There is no player with given id in our database!"));
        if(!player.canMove()){
            throw new MoveUnitException("You cannot move yet. Wait a few seconds!");
        }

        Unit unit = findPlayerUnit(unitId, player);
        if(unit.isDestroyed()){
            throw new MoveUnitException("The unit is already destroyed!");
        }

        if(!isValidMove(unit, destination, player.getGame())){
            throw new MoveUnitException("Destination position is incorrect!");
        }

        unit.setPosition(destination);
        unit.setMovesCount(unit.getMovesCount() + 1);
        unitRepository.save(unit);
    }

    private Unit findPlayerUnit(Long unitId, Player player) throws MoveUnitException {
        for(Unit unit:player.getUnits()){
            if(Objects.equals(unit.getId(), unitId)){
                return unit;
            }
        }

        throw new MoveUnitException("Player does not have a unit with the given ID!");
    }

    private boolean isValidMove(Unit unit, Position destination, Game game) {
        BoardSize boardSize = game.getBoardSize();

        if(destination.getX() < 0 || destination.getX()>boardSize.getWidth()
                || destination.getY() < 0 || destination.getY()>boardSize.getHeight()){
            return false;
        }

        if (unit instanceof Archer || unit instanceof  Transport) {
            if(!unit.validateMovement(destination)){
                return false;
            }
        } else if (unit instanceof Cannon) {
            //Cannon cannot move
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
}
