package com.chipotlebanditos.spacebanditos.model;

import java.io.Serializable;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.Set;

import android.graphics.Point;
import android.graphics.PointF;

import com.chipotlebanditos.spacebanditos.model.systems.EngineSystem;
import com.chipotlebanditos.spacebanditos.model.systems.LifeSupportSystem;
import com.chipotlebanditos.spacebanditos.model.systems.ShieldsSystem;
import com.chipotlebanditos.spacebanditos.model.systems.WeaponSystem;
import com.google.common.collect.ImmutableSet;

public class Game implements Serializable {
    
    private static final long serialVersionUID = -5499668584950852155L;
    
    public final Ship playerShip;
    
    public static final float EVENTS_REGION_WIDTH = 6f,
            EVENTS_REGION_HEIGHT = 3f;
    public static final float PLAYER_JUMP_RADIUS = 1f;
    // TODO: give region proportions to actually make circle
    
    public final Set<GameEvent> events; // TODO: should eventually be a set
    
    public GameEvent currentEvent;
    
    public boolean paused = false;
    
    public Game(Ship playerShip, GameEvent... events) {
        this.playerShip = playerShip;
        this.events = new ImmutableSet.Builder<GameEvent>().addAll(
                Arrays.asList(events)).build();
        for (GameEvent event : this.events) {
            event.playerShip = this.playerShip;
        }
        this.currentEvent = events[0]; // TODO: choose more intelligently?
    }
    
    public static Game generateNewGame() {
        return new Game(generateNewGamePlayerShip(),
                generateNewGameEvents((int) (EVENTS_REGION_WIDTH
                        * EVENTS_REGION_HEIGHT * .75f)));
    }
    
    private static Ship generateNewGamePlayerShip() {
        return new Ship(ShipLayout.PLAYER, 20, 20, 20, 60, 7,
                new Equipment[] {}, new LifeSupportSystem(2, 1, 0),
                new WeaponSystem(2, 2, 0, Weapon.LIGHT_BLASTER),
                new WeaponSystem(1, 0, 0, null), new ShieldsSystem(2, 1, 0),
                new EngineSystem(2, 2, 0));
    }
    
    private static GameEvent[] generateNewGameEvents(int count) {
        int zoneCols = (int) EVENTS_REGION_WIDTH, zoneRows = (int) EVENTS_REGION_HEIGHT;
        float xMargin = .5f, yMargin = .5f;
        
        LinkedList<Point> zones = new LinkedList<Point>();
        for (int i = 0; i < zoneCols; i++) {
            for (int j = 0; j < zoneRows; j++) {
                zones.add(new Point(i, j));
            }
        }
        Collections.shuffle(zones);
        
        GameEvent[] events = new GameEvent[count];
        
        Point playerZone = new Point(0, (int) Math.floor(Math.random()
                * zoneRows));
        zones.remove(playerZone);
        events[0] = GameEvent.getStartGameEvent(
                zoneToPoint(playerZone, zoneCols, zoneRows, xMargin, yMargin),
                false);
        
        Point exitZone = new Point(zoneCols - 1, (int) Math.floor(Math.random()
                * zoneRows));
        zones.remove(exitZone);
        events[count - 1] = GameEvent.getRandomEvent(
                zoneToPoint(exitZone, zoneCols, zoneRows, xMargin, yMargin),
                true);
        
        for (int i = 1; i < count - 1; i++) {
            Point zone = zones.pop();
            events[i] = GameEvent.getRandomEvent(
                    zoneToPoint(zone, zoneCols, zoneRows, xMargin, yMargin),
                    i == count - 1);
        }
        if (!hasValidPath(events, events[0], PLAYER_JUMP_RADIUS,
                new HashSet<GameEvent>())) {
            return generateNewGameEvents(count);
        }
        return events;
    }
    
    private static boolean hasValidPath(GameEvent[] events, GameEvent current,
            float adjDist, Set<GameEvent> visited) {
        visited.add(current);
        if (current.isExit) {
            return true;
        }
        for (GameEvent event : events) {
            if (visited.contains(event)) {
                continue;
            }
            if (GameEvent.getDistanceBetween(event, current) < adjDist) {
                if (hasValidPath(events, event, adjDist, visited)) {
                    return true;
                }
            }
        }
        return false;
    }
    
    private static PointF zoneToPoint(Point zone, int zoneCols, int zoneRows,
            float xMargin, float yMargin) {
        float x = xMargin + (zone.x + (float) Math.random())
                * (EVENTS_REGION_WIDTH - xMargin * 2) / zoneCols;
        float y = yMargin + (zone.y + (float) Math.random())
                * (EVENTS_REGION_HEIGHT - yMargin * 2) / zoneRows;
        return new PointF(x, y);
    }
}
