package com.chipotlebanditos.spacebanditos.model.systems;

import com.chipotlebanditos.spacebanditos.R;
import com.chipotlebanditos.spacebanditos.model.GameEvent;
import com.chipotlebanditos.spacebanditos.model.Ship;

public class ShieldsSystem extends ShipSystem {
    
    private static final long serialVersionUID = 7180312143894720628L;
    
    public int shields = 0;
    public float rechargeMillisFraction = 0f;
    
    public static final long BASE_TOTAL_RECHARGE_MILLIS = 1000L;
    
    public ShieldsSystem(int upgradeLevel, int powerLevel, int damageLevel) {
        super(upgradeLevel, powerLevel, damageLevel, new SystemUpgradeSequence(
                0, 50, 10, 160, 240) {
            
            @Override
            public String getUpgradeDescription(int level) {
                return String.format("%s layers of shield", level);
            }
            
        });
    }
    
    @Override
    public String getName() {
        return "SHIELDS";
    }
    
    @Override
    public int getIconResource() {
        return R.drawable.shields_icon;
    }
    
    public int getMaxShields() {
        return powerLevel;
    }
    
    public int takeShieldDamage(int damage) {
        if (shields > 0) {
            rechargeMillisFraction = 0f;
            if (damage > shields) {
                int excessDamage = damage - shields;
                shields = 0;
                return excessDamage;
            } else {
                shields -= damage;
                return 0;
            }
        } else {
            return damage;
        }
    }
    
    public long getTotalRechargeMillis(Ship ship) {
        return (long) Math.floor(BASE_TOTAL_RECHARGE_MILLIS
                / (1f + (beingRepaired ? 0f : .0005f * ship.crew)));
    }
    
    @Override
    public void update(int delta, Ship ship, GameEvent event) {
        super.update(delta, ship, event);
        if (ship == event.playerShip && !event.isDangerous()) {
            shields = getMaxShields();
        } else {
            rechargeMillisFraction += (float) delta
                    / getTotalRechargeMillis(ship);
            while (shields < getMaxShields() && rechargeMillisFraction >= 1f) {
                rechargeMillisFraction -= 1f;
                shields++;
            }
            shields = Math.min(shields, getMaxShields());
        }
        if (shields == getMaxShields()) {
            rechargeMillisFraction = 0f;
        }
    }
}
