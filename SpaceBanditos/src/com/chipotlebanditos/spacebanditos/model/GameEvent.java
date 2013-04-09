package com.chipotlebanditos.spacebanditos.model;

import java.io.Serializable;

public class GameEvent implements Serializable {
    
    private static final long serialVersionUID = 7423264560778377426L;
    
    public Ship playerShip = null;
    public ShipWithAI enemyShip;
    public StoreInventory store;
    public DialogTree dialog;
    
    // TODO: store any other event-constrained state, such as weapons fire, here
    
    public GameEvent(ShipWithAI enemyShip, StoreInventory store,
            DialogTree dialog) {
        this.enemyShip = enemyShip;
        this.store = store;
        this.dialog = dialog;
    }
    
    public boolean isDangerous() {
        return enemyShip != null && enemyShip.isHostile;
    }
    
    public Ship getOpposingShip(Ship ship) {
        if (ship == playerShip) {
            return enemyShip;
        } else if (ship == enemyShip) {
            return playerShip;
        } else {
            return null;
        }
    }
    
    public void update(int delta, Game game) {
        assert playerShip != null;
        playerShip.update(delta, this);
        if (enemyShip != null) {
            enemyShip.update(delta, this);
        }
    }
}
