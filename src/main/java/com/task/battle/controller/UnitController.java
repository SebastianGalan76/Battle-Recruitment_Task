package com.task.battle.controller;

import com.task.battle.data.PlayerColorEnum;
import com.task.battle.data.dto.*;
import com.task.battle.database.model.Game;
import com.task.battle.database.model.unit.Archer;
import com.task.battle.database.model.unit.Cannon;
import com.task.battle.database.model.unit.Transport;
import com.task.battle.database.model.unit.Unit;
import com.task.battle.database.repository.UnitRepository;
import com.task.battle.exception.UnitActionException;
import com.task.battle.service.GameService;
import com.task.battle.service.UnitService;
import lombok.AllArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;

@AllArgsConstructor
@RestController
@RequestMapping("/api/unit")
public class UnitController {
    final UnitRepository unitRepository;
    final UnitService unitService;

    final GameService gameService;

    @PostMapping("/list")
    public ResponseEntity<List<UnitInformationDto>> getUnitList(@RequestBody ColorDto colorDto) {
        Game game = gameService.getCurrentGame();
        if(game == null){
            return ResponseEntity.ok(null);
        }

        List<UnitInformationDto> units = new ArrayList<>();
        for(Unit unit:unitRepository.findUnitsByGameIdAndColor(game.getId(), colorDto.getColor())){
            units.add(unit.convertToUnitInformationDto());
        }

        return ResponseEntity.ok(units);
    }
    @GetMapping("/listAll")
    public ResponseEntity<List<UnitInformationDto>> getAllUnitList() {
        Game game = gameService.getCurrentGame();
        if(game == null){
            return ResponseEntity.ok(null);
        }

        List<UnitInformationDto> units = new ArrayList<>();
        for(Unit unit:unitRepository.findUnitsByGameId(game.getId())){
            units.add(unit.convertToUnitInformationDto());
        }

        return ResponseEntity.ok(units);
    }

    @PostMapping("/move")
    public ResponseEntity<String> moveUnit(@RequestBody MoveDto moveDto){
        try{
            Unit unit = unitRepository.findById(moveDto.getUnitId()).orElseThrow(() -> new UnitActionException("There is no unit with given id in our database!"));
            unitService.moveUnit(unit, moveDto.getDestination(), false);
            return ResponseEntity.ok("SUCCESS");
        } catch (UnitActionException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    @PostMapping("/shoot")
    public ResponseEntity<String> shootUnit(@RequestBody ShootDto shootDto){
        try{
            Unit unit = unitRepository.findById(shootDto.getUnitId()).orElseThrow(() -> new UnitActionException("There is no unit with given id in our database!"));
            unitService.shoot(unit, shootDto.getDestination(), false);
            return ResponseEntity.ok("SUCCESS");
        } catch (UnitActionException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    @GetMapping("/info/{id}")
    public ResponseEntity<UnitInformationDto> getUnitInformation(@PathVariable("id") Long id){
        Unit unit = unitRepository.findById(id).orElse(null);
        if(unit!=null){
            return ResponseEntity.ok(unit.convertToUnitInformationDto());
        }
        return ResponseEntity.badRequest().body(null);
    }

    @PostMapping("/randomAction")
    public ResponseEntity<String> performRandomAction(@RequestBody RandomActionDto randomActionDto) {
        try{
            String actionDetail = unitService.performRandomAction(randomActionDto.getColor(), randomActionDto.isIgnoreCooldown());
            return ResponseEntity.ok("SUCCESS \nAction: "+actionDetail);
        } catch (UnitActionException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }
}
