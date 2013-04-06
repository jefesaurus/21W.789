package com.chipotlebanditos.spacebanditos.model;

import java.io.Serializable;
import java.util.Arrays;
import java.util.Set;

import com.chipotlebanditos.spacebanditos.model.systems.EngineSystem;
import com.chipotlebanditos.spacebanditos.model.systems.LifeSupportSystem;
import com.chipotlebanditos.spacebanditos.model.systems.ShieldsSystem;
import com.chipotlebanditos.spacebanditos.model.systems.WeaponSystem;
import com.google.common.collect.ImmutableSet;

public class Game implements Serializable {
    
    private static final long serialVersionUID = -5499668584950852155L;
    
    public final Ship playerShip;
    
    public final Set<GameEvent> events;
    
    public GameEvent currentEvent;
    
    public boolean paused = false;
    
    public Game(Ship playerShip, GameEvent... events) {
        this.playerShip = playerShip;
        this.events = new ImmutableSet.Builder<GameEvent>().addAll(
                Arrays.asList(events)).build();
        for (GameEvent event : this.events) {
            event.playerShip = this.playerShip;
        }
        this.currentEvent = events[0];
    }
    
    public static Game generateNewGame() {
        Ship playerShip = new Ship(ShipLayout.TEST_LAYOUT, 12, 20, 100, 100,
                10, new Equipment[] {}, new LifeSupportSystem(2, 1, 0),
                new WeaponSystem(4, 2, 0, Weapon.TEST_WEAPON),
                new WeaponSystem(1, 0, 0, null), new ShieldsSystem(2, 1, 0),
                new EngineSystem(5, 5, 0));
        GameEvent[] events = new GameEvent[] { new GameEvent(new ShipWithAI(
                true, ShipLayout.TEST_LAYOUT, 4, 10, 100, 100, 6,
                new Equipment[] {}, new LifeSupportSystem(2, 1, 0),
                new WeaponSystem(2, 2, 0, Weapon.TEST_WEAPON),
                new ShieldsSystem(1, 1, 0)) {
            private static final long serialVersionUID = -6222368591517806993L;
            
            @Override
            public void AI(GameEvent event) {
                for (WeaponSystem system : getSystems(WeaponSystem.class)) {
                    if (system.target == null
                            && event.getOpposingShip(this) != null) {
                        system.target = event.getOpposingShip(this).systems.get(0);
                    }
                }
            }
        }, null, null) };
        
        return new Game(playerShip, events);
    }
}
