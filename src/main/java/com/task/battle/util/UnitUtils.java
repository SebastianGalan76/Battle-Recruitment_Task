package com.task.battle.util;

import com.task.battle.data.BoardSize;
import com.task.battle.data.GameConfiguration;
import com.task.battle.data.Position;
import com.task.battle.database.model.Game;
import com.task.battle.database.model.Player;
import com.task.battle.database.model.unit.Archer;
import com.task.battle.database.model.unit.Cannon;
import com.task.battle.database.model.unit.Transport;
import com.task.battle.database.model.unit.Unit;

import java.util.HashSet;
import java.util.Random;
import java.util.Set;

public class UnitUtils {

    public static void generateUnitsForPlayer(GameConfiguration gameConfiguration, Player player, Game game){
        Set<Position> usedPositions = new HashSet<>();

        for(int i=0;i<gameConfiguration.getArcherAmount();i++){
            Archer unit = new Archer();

            unit.setColor(player.getColor());
            unit.setPlayer(player);
            unit.setGame(game);
            unit.setPosition(generateUniquePosition(gameConfiguration.getBoardSize(), usedPositions));
            player.getUnits().add(unit);
        }
        for(int i=0;i<gameConfiguration.getTransportAmount();i++){
            Transport unit = new Transport();

            unit.setColor(player.getColor());
            unit.setPlayer(player);
            unit.setGame(game);
            unit.setPosition(generateUniquePosition(gameConfiguration.getBoardSize(), usedPositions));
            player.getUnits().add(unit);
        }
        for(int i=0;i<gameConfiguration.getCannonAmount();i++){
            Cannon unit = new Cannon();

            unit.setColor(player.getColor());
            unit.setPlayer(player);
            unit.setGame(game);
            unit.setPosition(generateUniquePosition(gameConfiguration.getBoardSize(), usedPositions));
            player.getUnits().add(unit);
        }
    }

    public static Position generateUniquePosition(BoardSize boardSize, Set<Position> usedPositions) {
        while (true) {
            Position randomPosition = generateRandomPosition(boardSize);
            if (!usedPositions.contains(randomPosition)) {
                usedPositions.add(randomPosition);
                return randomPosition;
            }
        }
    }

    private static Position generateRandomPosition(BoardSize boardSize) {
        Random random = new Random();
        int x = random.nextInt(0, boardSize.getWidth());
        int y = random.nextInt(0, boardSize.getHeight());
        return new Position(x, y);
    }
}
