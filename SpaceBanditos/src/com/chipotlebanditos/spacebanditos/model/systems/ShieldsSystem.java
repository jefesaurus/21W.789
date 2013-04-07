package com.chipotlebanditos.spacebanditos.model.systems;

import com.chipotlebanditos.spacebanditos.R;
import com.chipotlebanditos.spacebanditos.model.GameEvent;
import com.chipotlebanditos.spacebanditos.model.Ship;

public class ShieldsSystem extends ShipSystem {
    
    private static final long serialVersionUID = 7180312143894720628L;
    
    public int shields = 0;
    public long rechargeMillis = 0;
    
    public static final long TOTAL_RECHARGE_MILLIS = 1000L;
    
    public ShieldsSystem(int upgradeLevel, int powerLevel, int damageLevel) {
        super(upgradeLevel, powerLevel, damageLevel);
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
            rechargeMillis = 0;
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
    
    @Override
    public void update(int delta, Ship ship, GameEvent event) {
        super.update(delta, ship, event);
        if (ship == event.playerShip && !event.isDangerous()) {
            shields = getMaxShields();
        } else {
            rechargeMillis += delta;
            while (shields < getMaxShields()
                    && rechargeMillis >= TOTAL_RECHARGE_MILLIS) {
                rechargeMillis -= TOTAL_RECHARGE_MILLIS;
                shields++;
            }
            shields = Math.min(shields, getMaxShields());
        }
        if (shields == getMaxShields()) {
            rechargeMillis = 0;
        }
    }
}
