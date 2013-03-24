package com.chipotlebanditos.spacebanditos.model.systems;

import java.io.Serializable;

import com.chipotlebanditos.spacebanditos.model.GameEvent;
import com.chipotlebanditos.spacebanditos.model.Ship;

public abstract class ShipSystem implements Serializable {
    
    private static final long serialVersionUID = 7386731309842108725L;
    
    public int upgradeLevel;
    public int powerLevel;
    public int damageLevel;
    
    public ShipSystem(int upgradeLevel, int powerLevel, int damageLevel) {
        this.upgradeLevel = upgradeLevel;
        this.powerLevel = powerLevel;
        this.damageLevel = damageLevel;
    }
    
    public abstract String getName();
    
    public int getMaxPowerLevel() {
        return upgradeLevel - damageLevel;
    }
    
    // TODO: max upgrade level?
    
    public void update(int delta, Ship ship, GameEvent event) {
        // do nothing by default;
    }
}
