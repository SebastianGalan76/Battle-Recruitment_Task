package com.task.battle.controller;

import com.task.battle.data.dto.UnitInformationDto;
import com.task.battle.database.model.unit.Archer;
import com.task.battle.database.model.unit.Cannon;
import com.task.battle.database.model.unit.Transport;
import com.task.battle.database.model.unit.Unit;
import com.task.battle.database.repository.UnitRepository;
import lombok.AllArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.ArrayList;
import java.util.List;

@AllArgsConstructor
@RestController
@RequestMapping("/api/unit")
public class UnitController {

    final UnitRepository unitRepository;

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
}
