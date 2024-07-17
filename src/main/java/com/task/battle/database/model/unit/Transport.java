package com.task.battle.database.model.unit;

import com.task.battle.data.Position;
import jakarta.persistence.DiscriminatorValue;
import jakarta.persistence.Entity;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

@Entity
@Data
@AllArgsConstructor
@NoArgsConstructor
@EqualsAndHashCode(callSuper = true)
@DiscriminatorValue("TRANSPORT")
public class Transport extends Unit {

    @Override
    public boolean validateMovement(Position destination){
        int dx = Math.abs(position.getX() - destination.getX());
        int dy = Math.abs(position.getY() - destination.getY());
        return (dx == 0 && (dy == 1 || dy == 2 || dy == 3)) || (dy == 0 && (dx == 1 || dx == 2 || dx == 3));
    }
}
