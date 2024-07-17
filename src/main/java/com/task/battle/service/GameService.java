package com.task.battle.service;

import com.task.battle.data.BoardSize;
import com.task.battle.data.GameConfiguration;
import com.task.battle.data.PlayerColorEnum;
import com.task.battle.database.model.Game;
import com.task.battle.database.model.Player;
import com.task.battle.database.repository.GameRepository;
import com.task.battle.exception.BoardSizeException;
import com.task.battle.util.UnitUtils;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@AllArgsConstructor
public class GameService {
    final GameRepository gameRepository;

    public Game createNewGame(GameConfiguration gameConfiguration) throws BoardSizeException{
        checkGameBoard(gameConfiguration);

        Game game = new Game();
        game.setActive(true);
        game.setBoardSize(gameConfiguration.getBoardSize());

        //Created players
        Player playerWhite = new Player();
        playerWhite.setColor(PlayerColorEnum.WHITE);
        UnitUtils.generateUnitsForPlayer(gameConfiguration, playerWhite);
        playerWhite.setGame(game);
        game.getPlayers().add(playerWhite);

        Player playerBlack = new Player();
        playerBlack.setColor(PlayerColorEnum.BLACK);
        UnitUtils.generateUnitsForPlayer(gameConfiguration, playerBlack);
        playerBlack.setGame(game);
        game.getPlayers().add(playerBlack);

        return gameRepository.save(game);
    }

    private void checkGameBoard(GameConfiguration gameConfiguration) throws BoardSizeException {
        BoardSize boardSize = gameConfiguration.getBoardSize();
        int height = boardSize.getHeight();
        if(height<=0){
            throw new BoardSizeException("The board height cannot be less than or equal to 0!");
        }

        int width = boardSize.getWidth();
        if(width<=0){
            throw new BoardSizeException("The board width cannot be less than or equal to 0!");
        }

        int spaceAmount = height * width;
        int requiredSpaceForSinglePlayer = gameConfiguration.getArcherAmount() + gameConfiguration.getCannonAmount() + gameConfiguration.getTransportAmount();
        if(spaceAmount<=requiredSpaceForSinglePlayer * 2){
            throw new BoardSizeException("Not enough space for two players and their units!");
        }
    }
}
