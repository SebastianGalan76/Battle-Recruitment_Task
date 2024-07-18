package com.task.battle.controller;

import com.task.battle.data.PlayerColorEnum;
import com.task.battle.data.dto.ColorDto;
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

    @PostMapping("/list")
    public ResponseEntity<List<UnitInformationDto>> getUnitList(@RequestBody ColorDto colorDto) {
        List<UnitInformationDto> units = new ArrayList<>();

        for(Unit unit:unitRepository.findAll()){
            if(unit.getColor()!=colorDto.getColor()){
                continue;
            }

            units.add(unit.convertToUnitInformationDto());
        }

        return ResponseEntity.ok(units);
    }
    @PostMapping("/listAll")
    public ResponseEntity<List<UnitInformationDto>> getAllUnitList() {
        List<UnitInformationDto> units = new ArrayList<>();

        for(Unit unit:unitRepository.findAll()){
            units.add(unit.convertToUnitInformationDto());
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

    @GetMapping("/info/{id}")
    public ResponseEntity<UnitInformationDto> getUnitInformation(@PathVariable("id") Long id){
        Unit unit = unitRepository.findById(id).orElse(null);
        if(unit!=null){
            return ResponseEntity.ok(unit.convertToUnitInformationDto());
        }
        return ResponseEntity.badRequest().body(null);
    }
}
