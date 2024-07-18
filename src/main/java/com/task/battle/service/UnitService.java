package com.task.battle.service;

import com.task.battle.data.BoardSize;
import com.task.battle.data.CommandTypeEnum;
import com.task.battle.data.PlayerColorEnum;
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
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Random;

@Service
@AllArgsConstructor
public class UnitService {
    final UnitRepository unitRepository;
    final CommandHistoryRepository commandHistoryRepository;

    final GameService gameService;

    @Transactional
    public synchronized String moveUnit(Unit unit, Position destination) throws UnitActionException {
        if(unit.isDestroyed()){
            throw new UnitActionException("The unit is already destroyed!");
        }

        Player player = unit.getPlayer();
        if(!player.canMove()){
            throw new UnitActionException("You cannot move yet. Wait a few seconds!");
        }

        Game game = player.getGame();
        if(game.isFinished()){
            throw new UnitActionException("You cannot move. Game is over!");
        }

        checkMove(unit, destination, game);

        String commandDetail = "Player ("+player.getId()+") move unit ("+unit.getId()+") from ("+unit.getPosition().getX()+", "+unit.getPosition().getY()+ ") to (" +destination.getX()+", "+destination.getY() +")";
        saveCommandHistory(game, unit, "MOVE", commandDetail);

        unit.performMove(destination);
        player.setCooldown(unit.getCommandCooldown(CommandTypeEnum.MOVE));

        unitRepository.save(unit);
        gameService.checkGameStatus(game);

        return commandDetail;
    }

    @Transactional
    public synchronized String shoot(Unit unit, Position destination) throws UnitActionException {
        if(unit.isDestroyed()){
            throw new UnitActionException("The unit is already destroyed!");
        }

        Player player = unit.getPlayer();
        if(!player.canMove()){
            throw new UnitActionException("You cannot shoot yet. Wait a few seconds!");
        }

        Game game = player.getGame();
        if(game.isFinished()){
            throw new UnitActionException("You cannot shoot. Game is over!");
        }

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
        player.setCooldown(unit.getCommandCooldown(CommandTypeEnum.SHOOT));

        gameService.checkGameStatus(game);

        String commandDetail = "Player ("+player.getId()+") shoot using unit ("+unit.getId()+") at the field ("+destination.getX()+", "+destination.getY()+")";
        saveCommandHistory(game, unit, "SHOT", commandDetail);

        return commandDetail;
    }

    public synchronized String performRandomAction(PlayerColorEnum color) throws UnitActionException {
        Game game = gameService.getCurrentGame();
        if(game == null || game.isFinished()){
            throw new UnitActionException("There is no active game!");
        }

        Player player = game.getPlayer(color);
        if(player == null){
            throw new UnitActionException("Something went wrong. There is no player of the specified color!");
        }

        Random random = new Random();

        while (true){
            Unit unit = getRandomActiveUnit(player.getUnits());
            if(random.nextInt(2)==0){
                List<Position> positions = unit.getPossibleMoves();

                if(!positions.isEmpty()){
                    Position position = positions.get(random.nextInt(positions.size()));
                    return moveUnit(unit, position);
                }
            }
            else{
                List<Position> positions = unit.getPossibleShots();

                if(!positions.isEmpty()){
                    Position position = positions.get(random.nextInt(positions.size()));
                    return shoot(unit, position);
                }
            }
        }
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

    private Unit getRandomActiveUnit(List<Unit> units){
        Random random = new Random();
        int size = units.size();

        while (true){
            Unit unit = units.get(random.nextInt(size));
            if(!unit.isDestroyed()){
                return unit;
            }
        }
    }
}
