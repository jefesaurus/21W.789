package com.chipotlebanditos.spacebanditos.model.systems;

import com.chipotlebanditos.spacebanditos.model.ShipSystem;

public class LifeSupportSystem extends ShipSystem {
    
    private static final long serialVersionUID = 109496607657578658L;
    
    public LifeSupportSystem(int upgradeLevel, int powerLevel, int damageLevel) {
        super(upgradeLevel, powerLevel, damageLevel);
    }
    
    @Override
    public String getName() {
        return "LIFE SUPPORT";
    }
    
    public float getAtmosphereProducedPerMilli() {
        return powerLevel * 0.001f;
    }
    
    public float getMaxAtmosphere() {
        return powerLevel * 100f;
    }
}
