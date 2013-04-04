package com.chipotlebanditos.spacebanditos.model.systems;

import java.io.Serializable;

import com.chipotlebanditos.spacebanditos.model.GameEvent;
import com.chipotlebanditos.spacebanditos.model.Ship;

public abstract class ShipSystem implements Serializable {
    
    private static final long serialVersionUID = 7386731309842108725L;
    
    public int upgradeLevel;
    public int powerLevel;
    public int damageLevel;
    
    public boolean beingRepaired = false;
    public long repairMillis = 0;
    
    public static final long TOTAL_REPAIR_MILLIS = 5000L;
    
    // public double x;
    // public double y;
    
    public ShipSystem(int upgradeLevel, int powerLevel, int damageLevel) {
        this.upgradeLevel = upgradeLevel;
        this.powerLevel = powerLevel;
        this.damageLevel = damageLevel;
        // this.x = x;
        // this.y = y;
    }
    
    public abstract String getName();
    
    public abstract int getIconResource();
    
    public int getMaxPowerLevel() {
        return upgradeLevel - damageLevel;
    }
    
    // TODO: max upgrade level?
    
    public void takeDamage(int damage, Ship ship) {
        damageLevel = Math.min(damageLevel + damage, upgradeLevel);
        while (powerLevel > getMaxPowerLevel()) {
            ship.removePower(this);
        }
    }
    
    public void update(int delta, Ship ship, GameEvent event) {
        if (beingRepaired) {
            if (ship == event.playerShip && !event.isDangerous()) {
                damageLevel = 0;
            } else {
                repairMillis += delta;
                while (damageLevel > 0 && repairMillis >= TOTAL_REPAIR_MILLIS) {
                    repairMillis -= TOTAL_REPAIR_MILLIS;
                    damageLevel--;
                }
            }
        } else {
            repairMillis = 0;
        }
        if (damageLevel == 0) {
            repairMillis = 0;
            beingRepaired = false;
        }
    }
}
