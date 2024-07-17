package com.task.battle.controller;

import com.task.battle.data.dto.MoveDto;
import com.task.battle.data.dto.ShootDto;
import com.task.battle.data.dto.UnitInformationDto;
import com.task.battle.database.model.unit.Archer;
import com.task.battle.database.model.unit.Cannon;
import com.task.battle.database.model.unit.Transport;
import com.task.battle.database.model.unit.Unit;
import com.task.battle.database.repository.UnitRepository;
import com.task.battle.exception.UnitActionException;
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

    @GetMapping("/list")
    public ResponseEntity<List<UnitInformationDto>> getUnitList() {
        List<UnitInformationDto> units = new ArrayList<>();

        for(Unit unit:unitRepository.findAll()){
            UnitInformationDto unitDto = new UnitInformationDto();

            unitDto.setUnitId(unit.getId());
            unitDto.setPosition(unit.getPosition());
            unitDto.setColor(unit.getColor());
            unit.setMovesCount(unitDto.getMoveCount());

            if(unit instanceof Archer){
                unitDto.setType("ARCHER");
            }
            else if(unit instanceof Transport){
                unitDto.setType("TRANSPORT");
            }
            else if(unit instanceof Cannon){
                unitDto.setType("CANNON");
            }
            else{
                unitDto.setType("UNDEFINED");
            }

            if(unit.isDestroyed()){
                unitDto.setStatus("DESTROYED");
            }
            else{
                unitDto.setStatus("ACTIVE");
            }

            units.add(unitDto);
        }

        return ResponseEntity.ok(units);
    }

    @PostMapping("/move")
    public ResponseEntity<String> moveUnit(@RequestBody MoveDto moveDto){
        try{
            unitService.moveUnit(moveDto.getUnitId(), moveDto.getDestination());
            return ResponseEntity.ok("SUCCESS");
        } catch (UnitActionException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    @PostMapping("/shoot")
    public ResponseEntity<String> shootUnit(@RequestBody ShootDto shootDto){
        try{
            unitService.shoot(shootDto.getUnitId(), shootDto.getDestination());
            return ResponseEntity.ok("SUCCESS");
        } catch (UnitActionException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }
}
