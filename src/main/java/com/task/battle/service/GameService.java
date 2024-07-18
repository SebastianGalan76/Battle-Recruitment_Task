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
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;

@Service
@AllArgsConstructor
public class GameService {
    final GameRepository gameRepository;

    @Transactional
    public synchronized Game createNewGame(GameConfiguration gameConfiguration) throws GameConfigurationException {
        checkGameBoard(gameConfiguration);
        checkUnitAmount(gameConfiguration);

        Game game = new Game();
        game.setBoardSize(gameConfiguration.getBoardSize());

        //Created players
        createPlayer(PlayerColorEnum.WHITE, gameConfiguration, game);
        createPlayer(PlayerColorEnum.BLACK, gameConfiguration, game);

        for(Game oldGame:gameRepository.findAll()){
            gameRepository.delete(oldGame);
        }

        return gameRepository.save(game);
    }

    public void checkGameStatus(Game game){
        Player whitePlayer = game.getPlayer(PlayerColorEnum.WHITE);
        if(!whitePlayer.hasUnit()){
            game.setFinished(true);
        }

        Player blackPlayer = game.getPlayer(PlayerColorEnum.BLACK);
        if(!blackPlayer.hasUnit()){
            game.setFinished(true);
        }
    }

    private void createPlayer(PlayerColorEnum color, GameConfiguration gameConfiguration, Game game){
        Player player = new Player();
        player.setGame(game);
        player.setColor(color);

        UnitUtils.generateUnitsForPlayer(gameConfiguration, player);
        player.setNextCommandTimestamp(LocalDateTime.now());

        game.getPlayers().add(player);
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
