package com.chipotlebanditos.spacebanditos.model;

import java.io.Serializable;
import java.util.Arrays;
import java.util.List;

import com.chipotlebanditos.spacebanditos.model.systems.EngineSystem;
import com.chipotlebanditos.spacebanditos.model.systems.LifeSupportSystem;
import com.chipotlebanditos.spacebanditos.model.systems.ShieldsSystem;
import com.chipotlebanditos.spacebanditos.model.systems.WeaponSystem;
import com.google.common.collect.ImmutableList;

public class Game implements Serializable {
    
    private static final long serialVersionUID = -5499668584950852155L;
    
    public final Ship playerShip;
    
    public final List<GameEvent> events; // TODO: should eventually be a set
    
    public GameEvent currentEvent;
    
    public boolean paused = false;
    
    public Game(Ship playerShip, GameEvent... events) {
        this.playerShip = playerShip;
        this.events = new ImmutableList.Builder<GameEvent>().addAll(
                Arrays.asList(events)).build();
        for (GameEvent event : this.events) {
            event.playerShip = this.playerShip;
        }
        this.currentEvent = events[0]; // TODO: choose more intelligently
    }
    
    public static Game generateNewGame() {
        return new Game(generateNewGamePlayerShip(), generateNewGameEvents(3));
    }
    
    private static Ship generateNewGamePlayerShip() {
        return new Ship(ShipLayout.PLAYER, 20, 20, 20, 100, 10,
                new Equipment[] {}, new LifeSupportSystem(2, 1, 0),
                new WeaponSystem(4, 2, 0, Weapon.LIGHT_BLASTER),
                new WeaponSystem(1, 0, 0, null), new ShieldsSystem(2, 1, 0),
                new EngineSystem(5, 5, 0));
    }
    
    private static GameEvent[] generateNewGameEvents(int count) {
        GameEvent[] events = new GameEvent[count];
        events[0] = GameEvent.getStartGameEvent();
        for (int i = 1; i < count; i++) {
            events[i] = GameEvent.getRandomEvent();
        }
        return events;
    }
}
