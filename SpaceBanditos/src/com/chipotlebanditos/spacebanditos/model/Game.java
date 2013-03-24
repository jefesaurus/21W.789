package com.chipotlebanditos.spacebanditos.model;

import java.io.Serializable;
import java.util.Arrays;
import java.util.Set;

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
        Ship playerShip = new Ship(100, 100, 100, 100, 2, new Equipment[] {});
        GameEvent[] events = new GameEvent[] { new GameEvent(null, null, null) };
        
        return new Game(playerShip, events);
    }
}
