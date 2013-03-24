package com.chipotlebanditos.spacebanditos.model;

import com.chipotlebanditos.spacebanditos.model.systems.ShipSystem;

public abstract class ShipWithAI extends Ship {
    
    private static final long serialVersionUID = 4797480908313015294L;
    
    public boolean isHostile;
    
    public ShipWithAI(boolean isHostile, int hull, int maxHull, int crew,
            float atmosphere, int reservePower, int powerUpgradeLevel,
            Equipment[] inventory, ShipSystem... systems) {
        super(hull, maxHull, crew, atmosphere, reservePower, powerUpgradeLevel,
                inventory, systems);
        this.isHostile = isHostile;
    }
    
    public abstract void AI(GameEvent event);
    
    @Override
    public void update(int delta, GameEvent event) {
        super.update(delta, event);
        AI(event);
    }
}
