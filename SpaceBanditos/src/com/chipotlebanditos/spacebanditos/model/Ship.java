package com.chipotlebanditos.spacebanditos.model;

import java.io.Serializable;
import java.util.Arrays;
import java.util.Iterator;
import java.util.List;

import com.chipotlebanditos.spacebanditos.model.systems.ShipSystem;
import com.google.common.collect.ImmutableList;
import com.google.common.collect.Iterables;

public class Ship implements Serializable {
    
    private static final long serialVersionUID = -4798388937510217139L;
    
    public final List<ShipSystem> systems; // TODO: use a better class for
                                           // organization (e.g. multimap by
                                           // class)
    
    public int hull, maxHull;
    
    public int crew;
    
    public float atmosphere;
    
    public final ShipSystem power;
    
    public final List<Equipment> inventory;
    
    public Ship(int hull, int maxHull, int crew, float atmosphere,
            int reservePower, int powerUpgradeLevel, Equipment[] inventory,
            ShipSystem... systems) {
        this.hull = hull;
        this.maxHull = maxHull;
        this.crew = crew;
        this.atmosphere = atmosphere;
        this.power = new ShipSystem(powerUpgradeLevel, reservePower, 0) {
            
            private static final long serialVersionUID = -5264026005696896366L;
            
            @Override
            public String getName() {
                return null;
            }
            
        };
        this.inventory = Arrays.asList(inventory);
        this.systems = new ImmutableList.Builder<ShipSystem>().addAll(
                Arrays.asList(systems)).build();
    }
    
    public <T extends ShipSystem> Iterable<T> getSystems(Class<T> type) {
        return Iterables.filter(systems, type);
    }
    
    public <T extends ShipSystem> T getSystem(Class<T> type) {
        Iterator<T> iter = getSystems(type).iterator();
        if (iter.hasNext()) {
            return iter.next();
        } else {
            return null;
        }
    }
    
    public void update(int delta, GameEvent event) {
        for (ShipSystem system : systems) {
            system.update(delta, this, event);
        }
        // TODO
    }
}
