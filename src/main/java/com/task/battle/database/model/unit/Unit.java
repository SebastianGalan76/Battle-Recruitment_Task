package com.task.battle.database.model.unit;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.task.battle.data.CommandTypeEnum;
import com.task.battle.data.PlayerColorEnum;
import com.task.battle.data.Position;
import com.task.battle.data.dto.UnitInformationDto;
import com.task.battle.database.model.Game;
import com.task.battle.database.model.Player;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

@Entity
@Data
@AllArgsConstructor
@NoArgsConstructor
@Inheritance(strategy = InheritanceType.SINGLE_TABLE)
@DiscriminatorColumn(name="unit_type", discriminatorType = DiscriminatorType.STRING)
public class Unit {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    Long id;

    @Embedded
    Position position;

    @Enumerated(EnumType.STRING)
    PlayerColorEnum color;

    @ManyToOne
    @JoinColumn(name = "player_id")
    Player player;

    @ManyToOne
    @JoinColumn(name = "game_id")
    Game game;

    boolean isDestroyed;
    int movesCount;

    @Transient
    List<Position> possibleMoves = new ArrayList<>();

    @Transient
    List<Position> possibleShots = new ArrayList<>();

    public boolean validateDestination(Position destination, CommandTypeEnum commandTypeEnum){
        return false;
    }
    public int getCommandCooldown(CommandTypeEnum commandType){
        return 0;
    }
    public void performMove(Position destination){
        position = destination;
        movesCount++;

        calculatePossibleMoves();
        calculatePossibleShots();
    }

    public void calculatePossibleMoves(){
        possibleMoves.clear();
    }
    public void calculatePossibleShots(){
        possibleShots.clear();
    }

    public UnitInformationDto convertToUnitInformationDto(){
        UnitInformationDto unitDto = new UnitInformationDto();

        unitDto.setUnitId(id);
        unitDto.setPosition(position);
        unitDto.setColor(color);
        unitDto.setMoveCount(movesCount);

        unitDto.setPossibleMoves(possibleMoves);
        unitDto.setPossibleShots(possibleShots);

        if(this instanceof Archer){
            unitDto.setType("ARCHER");
        }
        else if(this instanceof Transport){
            unitDto.setType("TRANSPORT");
        }
        else if(this instanceof Cannon){
            unitDto.setType("CANNON");
        }
        else{
            unitDto.setType("UNDEFINED");
        }

        if(isDestroyed){
            unitDto.setStatus("DESTROYED");
        }
        else{
            unitDto.setStatus("ACTIVE");
        }

        return unitDto;
    }

    @PostLoad
    void calculatePossibleDestinations(){
        calculatePossibleMoves();
        calculatePossibleShots();
    }
}
