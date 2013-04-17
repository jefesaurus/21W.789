package com.chipotlebanditos.spacebanditos.model;

import java.io.Serializable;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.List;

import com.chipotlebanditos.spacebanditos.model.systems.EngineSystem;
import com.chipotlebanditos.spacebanditos.model.systems.LifeSupportSystem;
import com.chipotlebanditos.spacebanditos.model.systems.WeaponSystem;

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
    
    public static GameEvent getStartGameEvent() {
        return new GameEvent(null, null, null);
    }
    
    private static class EventPool {
        
        public static GameEvent exampleCombat() {
            return new GameEvent(new ShipWithAI(true, ShipLayout.ENEMY, 10, 10,
                    100, 100, 6, new Equipment[] {}, new LifeSupportSystem(2,
                            1, 0), new WeaponSystem(2, 2, 0,
                            Weapon.LIGHT_BLASTER), new EngineSystem(2, 1, 0)) {
                private static final long serialVersionUID = -6222368591517806993L;
                
                @Override
                public void AI(GameEvent event) {
                    for (WeaponSystem system : getSystems(WeaponSystem.class)) {
                        if (system.target == null
                                && event.getOpposingShip(this) != null) {
                            system.target = event.getOpposingShip(this).systems
                                    .get(0);
                        }
                    }
                }
            }, null, null);
        }
        
        public static final List<Method> pool = new ArrayList<Method>();
        static {
            for (Method method : EventPool.class.getMethods()) {
                if (method.getReturnType().equals(GameEvent.class)) {
                    pool.add(method);
                }
            }
        }
    }
    
    public static GameEvent getRandomEvent() {
        try {
            return (GameEvent) EventPool.pool.get(
                    (int) Math.floor(Math.random() * EventPool.pool.size()))
                    .invoke(null);
        } catch (IllegalArgumentException e) {
            throw new RuntimeException(e);
        } catch (IllegalAccessException e) {
            throw new RuntimeException(e);
        } catch (InvocationTargetException e) {
            throw new RuntimeException(e);
        }
    }
}
