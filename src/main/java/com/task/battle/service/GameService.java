package com.task.battle.service;

import com.task.battle.data.BoardSize;
import com.task.battle.data.GameConfiguration;
import com.task.battle.data.PlayerColorEnum;
import com.task.battle.database.model.Game;
import com.task.battle.database.model.Player;
import com.task.battle.database.repository.GameRepository;
import com.task.battle.exception.GameConfigurationException;
import com.task.battle.util.UnitUtils;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;

@Service
@AllArgsConstructor
public class GameService {
    final GameRepository gameRepository;

    public void createNewGame(GameConfiguration gameConfiguration) throws GameConfigurationException {
        checkGameBoard(gameConfiguration);
        checkUnitAmount(gameConfiguration);

        Game game = new Game();
        game.setActive(true);
        game.setBoardSize(gameConfiguration.getBoardSize());

        //Created players
        Player playerWhite = new Player();
        playerWhite.setColor(PlayerColorEnum.WHITE);
        UnitUtils.generateUnitsForPlayer(gameConfiguration, playerWhite);
        playerWhite.setGame(game);
        playerWhite.setNextCommandTimestamp(LocalDateTime.now());
        game.getPlayers().add(playerWhite);

        Player playerBlack = new Player();
        playerBlack.setColor(PlayerColorEnum.BLACK);
        UnitUtils.generateUnitsForPlayer(gameConfiguration, playerBlack);
        playerBlack.setGame(game);
        playerBlack.setNextCommandTimestamp(LocalDateTime.now());
        game.getPlayers().add(playerBlack);

        gameRepository.save(game);
    }

    private void checkGameBoard(GameConfiguration gameConfiguration) throws GameConfigurationException {
        BoardSize boardSize = gameConfiguration.getBoardSize();
        int height = boardSize.getHeight();
        if(height<=0){
            throw new GameConfigurationException("The board height cannot be less than or equal to 0!");
        }

        int width = boardSize.getWidth();
        if(width<=0){
            throw new GameConfigurationException("The board width cannot be less than or equal to 0!");
        }

        int spaceAmount = height * width;
        int requiredSpaceForSinglePlayer = gameConfiguration.getArcherAmount() + gameConfiguration.getCannonAmount() + gameConfiguration.getTransportAmount();
        if(spaceAmount<=requiredSpaceForSinglePlayer * 2){
            throw new GameConfigurationException("Not enough space for two players and their units!");
        }
    }

    private void checkUnitAmount(GameConfiguration gameConfiguration) throws GameConfigurationException {
        int archer = gameConfiguration.getArcherAmount();
        if(archer < 0){
            throw new GameConfigurationException("Amount of archers cannot be less than 0!");
        }
        int transport = gameConfiguration.getTransportAmount();
        if(transport < 0){
            throw new GameConfigurationException("Amount of transports cannot be less than 0!");
        }
        int cannon = gameConfiguration.getCannonAmount();
        if(cannon < 0){
            throw new GameConfigurationException("Amount of cannons cannot be less than 0!");
        }

        if(archer + transport + cannon == 0){
            throw new GameConfigurationException("Playing without units is soooo boring!");
        }
    }
}
