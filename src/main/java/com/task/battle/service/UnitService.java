package com.task.battle.service;

import com.task.battle.data.BoardSize;
import com.task.battle.data.CommandTypeEnum;
import com.task.battle.data.Position;
import com.task.battle.database.model.CommandHistory;
import com.task.battle.database.model.Game;
import com.task.battle.database.model.Player;
import com.task.battle.database.model.unit.Transport;
import com.task.battle.database.model.unit.Unit;
import com.task.battle.database.repository.CommandHistoryRepository;
import com.task.battle.database.repository.UnitRepository;
import com.task.battle.exception.UnitActionException;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;

@Service
@AllArgsConstructor
public class UnitService {
    final UnitRepository unitRepository;
    final CommandHistoryRepository commandHistoryRepository;

    public void moveUnit(Long unitId, Position destination) throws UnitActionException {
        Unit unit = unitRepository.findById(unitId).orElseThrow(() -> new UnitActionException("There is no unit with given id in our database!"));
        if(unit.isDestroyed()){
            throw new UnitActionException("The unit is already destroyed!");
        }

        Player player = unit.getPlayer();
        if(!player.canMove()){
            throw new UnitActionException("You cannot move yet. Wait a few seconds!");
        }

        Game game = player.getGame();
        checkMove(unit, destination, game);

        saveCommandHistory(game, unit, "MOVE", "Player ("+player.getId()+") move unit ("+unit.getId()+") from "+unit.getPosition().getX()+", y: "+unit.getPosition().getY()+ "to (x: " +destination.getX()+", y: "+destination.getY());

        unit.setPosition(destination);
        unit.setMovesCount(unit.getMovesCount() + 1);
        player.setNextCommandTimestamp(LocalDateTime.now().plusSeconds(unit.getCommandCooldown(CommandTypeEnum.MOVE)));

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
        checkShoot(unit, destination, game);

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
        player.setNextCommandTimestamp(LocalDateTime.now().plusSeconds(unit.getCommandCooldown(CommandTypeEnum.SHOOT)));

        saveCommandHistory(game, unit, "SHOT", "Player ("+player.getId()+") shoot using unit ("+unit.getId()+") at the field (x: "+destination.getX()+", y: "+destination.getY());
    }

    private void checkShoot(Unit unit, Position destination, Game game) throws UnitActionException {
        if(!isDestinationOnBoard(game.getBoardSize(), destination)){
            throw new UnitActionException("The specified destination is off the board!");
        }

        if(!unit.validateDestination(destination, CommandTypeEnum.SHOOT)){
            throw new UnitActionException("The specified unit cannot shoot there!");
        }
    }
    private void checkMove(Unit unit, Position destination, Game game) throws UnitActionException {
        if(!isDestinationOnBoard(game.getBoardSize(), destination)){
            throw new UnitActionException("The specified destination is off the board!");
        }

        if(!unit.validateDestination(destination, CommandTypeEnum.MOVE)){
            throw new UnitActionException("The specified unit cannot move there!");
        }

        // Check for collisions with other units
        for(Player player:game.getPlayers()){
            for (Unit otherUnit : player.getUnits()) {
                if(otherUnit.isDestroyed()){
                    continue;
                }

                if (otherUnit.getPosition().equals(destination)) {
                    if (otherUnit.getColor().equals(unit.getColor())) {
                        throw new UnitActionException("You will hit your own unit. Operation stopped!");
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
    }

    private boolean isDestinationOnBoard(BoardSize boardSize, Position destination){
        return destination.getX() >= 0 && destination.getX() <= boardSize.getWidth()
                && destination.getY() >= 0 && destination.getY() <= boardSize.getHeight();
    }
    private void saveCommandHistory(Game game, Unit unit, String command, String details) {
        CommandHistory history = new CommandHistory();
        history.setGame(game);
        history.setUnit(unit);
        history.setCommand(command);
        history.setDetails(details);
        history.setDate(LocalDateTime.now());
        commandHistoryRepository.save(history);
    }
}
