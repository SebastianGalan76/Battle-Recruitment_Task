package com.task.battle.controller;

import com.task.battle.data.GameConfiguration;
import com.task.battle.database.model.Game;
import com.task.battle.exception.GameConfigurationException;
import com.task.battle.service.GameService;
import lombok.AllArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;


@AllArgsConstructor
@RestController
@RequestMapping("/api/game")
public class GameController {
    final GameService gameService;

    @PostMapping("/new")
    public ResponseEntity<String> createNewGame(@RequestBody GameConfiguration gameConfiguration) {
        try{
            Game game = gameService.createNewGame(gameConfiguration);
            return ResponseEntity.ok("Created a new game! ID: "+ game.getId());
        }catch (GameConfigurationException e){
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }
}
