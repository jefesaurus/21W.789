package com.chipotlebanditos.spacebanditos.model.systems;

import com.chipotlebanditos.spacebanditos.R;
import com.chipotlebanditos.spacebanditos.model.GameEvent;
import com.chipotlebanditos.spacebanditos.model.Ship;

public class LifeSupportSystem extends ShipSystem {
    
    private static final long serialVersionUID = 109496607657578658L;
    
    public LifeSupportSystem(int upgradeLevel, int powerLevel, int damageLevel) {
        super(upgradeLevel, powerLevel, damageLevel);
    }
    
    @Override
    public String getName() {
        return "LIFE SUPPORT";
    }
    
    @Override
    public int getIconResource() {
        return R.drawable.life_support_icon;
    }
    
    public float getAtmosphereProducedPerMilli() {
        return powerLevel * 0.001f + powerLevel > 0 ? Ship.ATMOSPHERE_LOSS_PER_MILLI
                : 0;
    }
    
    public float getMaxAtmosphere() {
        return powerLevel * 100f;
    }
    
    @Override
    public void update(int delta, Ship ship, GameEvent event) {
        super.update(delta, ship, event);
        if (ship.atmosphere < getMaxAtmosphere()) {
            ship.atmosphere = Math.min(ship.atmosphere
                    + getAtmosphereProducedPerMilli(), getMaxAtmosphere());
        }
    }
}
