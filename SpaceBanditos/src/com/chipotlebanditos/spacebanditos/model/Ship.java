package com.chipotlebanditos.spacebanditos.model;

import java.io.Serializable;
import java.util.Arrays;
import java.util.Iterator;
import java.util.List;

import com.chipotlebanditos.spacebanditos.model.systems.ShieldsSystem;
import com.chipotlebanditos.spacebanditos.model.systems.ShipSystem;
import com.google.common.collect.ImmutableList;
import com.google.common.collect.Iterables;

public class Ship implements Serializable {
    
    private static final long serialVersionUID = -4798388937510217139L;
    
    public static final float ATMOSPHERE_LOSS_PER_MILLI = .001f;
    
    public final List<ShipSystem> systems; // TODO: use a better class for
                                           // organization (e.g. multimap by
                                           // class)
    
    public int hull, maxHull;
    
    public int crew;
    
    public float atmosphere;
    
    public final ShipSystem power;
    
    public final List<Equipment> inventory;
    
    public Ship(int hull, int maxHull, int crew, float atmosphere,
            int totalPower, Equipment[] inventory, ShipSystem... systems) {
        this.hull = hull;
        this.maxHull = maxHull;
        this.crew = crew;
        this.atmosphere = atmosphere;
        int reservePower = totalPower;
        for (ShipSystem system : systems) {
            reservePower -= system.powerLevel;
        }
        this.power = new ShipSystem(totalPower, reservePower, 0) {
            
            private static final long serialVersionUID = -5264026005696896366L;
            
            @Override
            public String getName() {
                throw new UnsupportedOperationException();
            }
            
            @Override
            public int getIconResource() {
                throw new UnsupportedOperationException();
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
    
    public void addPower(ShipSystem system) {
        assert power.powerLevel > 0
                && system.powerLevel < system.getMaxPowerLevel();
        power.powerLevel--;
        system.powerLevel++;
    }
    
    public void removePower(ShipSystem system) {
        assert power.powerLevel < power.getMaxPowerLevel()
                && system.powerLevel > 0;
        power.powerLevel++;
        system.powerLevel--;
    }
    
    public void takeDamage(int damage, ShipSystem system) {
        if (damage == 0) {
            return;
        }
        if (getSystem(ShieldsSystem.class) != null) {
            damage = getSystem(ShieldsSystem.class).takeShieldDamage(damage);
            if (damage == 0) {
                return;
            }
        }
        system.takeDamage(damage, this);
        hull = Math.max(hull - damage, 0);
    }
    
    public void attack(int damage, Ship ship, ShipSystem system) {
        // TODO: account for evasion/accuracy
        ship.takeDamage(damage, system);
    }
    
    public void update(int delta, GameEvent event) {
        atmosphere -= ATMOSPHERE_LOSS_PER_MILLI;
        for (ShipSystem system : systems) {
            system.update(delta, this, event);
        }
        atmosphere = Math.max(atmosphere, 0f);
        // TODO
    }
}
