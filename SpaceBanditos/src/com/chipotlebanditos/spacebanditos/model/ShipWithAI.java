package com.chipotlebanditos.spacebanditos.model;

import com.chipotlebanditos.spacebanditos.model.systems.ShipSystem;

public abstract class ShipWithAI extends Ship {
    
    private static final long serialVersionUID = 4797480908313015294L;
    
    public boolean isHostile;
    
    public ShipWithAI(boolean isHostile, ShipLayout layout, int hull,
            int maxHull, int crew, float atmosphere, int totalPower,
            Equipment[] inventory, ShipSystem... systems) {
        super(layout, hull, maxHull, crew, atmosphere, totalPower, inventory,
                systems);
        this.isHostile = isHostile;
    }
    
    public abstract void AI(GameEvent event);
    
    public abstract int getReward();
    
    @Override
    public void update(int delta, GameEvent event) {
        super.update(delta, event);
        AI(event);
    }
    
    @Override
    public void takeDamage(int damage, ShipSystem system, GameEvent event) {
        super.takeDamage(damage, system, event);
        if (hasBeenDestroyed() && this == event.enemyShip) {
            event.enemyShip = null;
            event.game.playerCash += getReward();
        }
    }
}
