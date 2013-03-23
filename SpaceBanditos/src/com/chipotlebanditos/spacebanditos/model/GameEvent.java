package com.chipotlebanditos.spacebanditos.model;

import java.io.Serializable;

public class GameEvent implements Serializable {
    
    private static final long serialVersionUID = 7423264560778377426L;
    
    public ShipWithAI enemyShip;
    public StoreInventory store;
    public DialogTree dialog;
    
    public GameEvent(ShipWithAI enemyShip, StoreInventory store,
            DialogTree dialog) {
        this.enemyShip = enemyShip;
        this.store = store;
        this.dialog = dialog;
    }
}
