package com.chipotlebanditos.spacebanditos.model;

import java.io.Serializable;
import java.util.Arrays;
import java.util.Set;

import com.google.common.collect.ImmutableSet;

public class Game implements Serializable {
    
    private static final long serialVersionUID = -5499668584950852155L;
    
    public final Ship playerShip;
    
    public final Set<GameEvent> events;
    
    public Game(Ship playerShip, GameEvent... events) {
        this.playerShip = playerShip;
        this.events = new ImmutableSet.Builder<GameEvent>().addAll(
                Arrays.asList(events)).build();
    }
}
