package com.chipotlebanditos.spacebanditos.model;

import java.io.Serializable;
import java.util.Arrays;
import java.util.Iterator;
import java.util.List;

import android.graphics.PointF;

import com.chipotlebanditos.spacebanditos.model.systems.LifeSupportSystem;
import com.chipotlebanditos.spacebanditos.model.systems.ShieldsSystem;
import com.chipotlebanditos.spacebanditos.model.systems.ShipSystem;
import com.google.common.collect.ImmutableList;
import com.google.common.collect.Iterables;

public class Ship implements Serializable {
    
    private static final long serialVersionUID = -4798388937510217139L;
    
    public static final float ATMOSPHERE_LOSS_PER_MILLI = .001f;
    public static final float ATMOSPHERE_REQUIRED_PER_CREW = 1f;
    
    public final ShipLayout layout;
    
    public final List<ShipSystem> systems;
    
    public int hull, maxHull;
    
    public int crew;
    
    public float atmosphere;
    
    public final ShipSystem power;
    
    public final List<Equipment> inventory;
    
    public Ship(ShipLayout layout, int hull, int maxHull, int crew,
            float atmosphere, int totalPower, Equipment[] inventory,
            ShipSystem... systems) {
        this.layout = layout;
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
    
    public int getSystemIndex(ShipSystem system) {
        return systems.indexOf(system);
    }
    
    public ShipSystem getSystemByIndex(int index) {
        return systems.get(index);
    }
    
    public PointF getLayoutPosition(ShipSystem system) {
        Iterator<? extends ShipSystem> iter = getSystems(system.getClass())
                .iterator();
        for (int i = 0; iter.hasNext(); i++) {
            if (iter.next() == system) {
                return layout.systemPositions.get(system.getClass()).get(i);
            }
        }
        throw new IllegalArgumentException();
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
    
    public int getShields() {
        if (getSystem(ShieldsSystem.class) == null) {
            return 0;
        } else {
            return getSystem(ShieldsSystem.class).shields;
        }
    }
    
    public int getMaxShields() {
        if (getSystem(ShieldsSystem.class) == null) {
            return 0;
        } else {
            return getSystem(ShieldsSystem.class).getMaxShields();
        }
    }
    
    public void attack(int damage, Ship ship, ShipSystem system) {
        // TODO: account for evasion/accuracy
        ship.takeDamage(damage, system);
    }
    
    public int getMaxSustainableCrew() {
        LifeSupportSystem system = getSystem(LifeSupportSystem.class);
        if (system == null) {
            return 0;
        } else {
            return (int) Math.floor(system.getMaxAtmosphere()
                    / ATMOSPHERE_REQUIRED_PER_CREW);
        }
    }
    
    public void update(int delta, GameEvent event) {
        atmosphere -= ATMOSPHERE_LOSS_PER_MILLI;
        for (ShipSystem system : systems) {
            system.update(delta, this, event);
        }
        atmosphere = Math.max(atmosphere, 0f);
        crew = Math.min(crew,
                (int) Math.floor(atmosphere * ATMOSPHERE_REQUIRED_PER_CREW));
        // TODO: update other stats
    }
}
