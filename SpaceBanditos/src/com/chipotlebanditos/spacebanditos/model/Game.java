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
    
    public int playerCash;
    
    public static final float EVENTS_REGION_WIDTH = 6f,
            EVENTS_REGION_HEIGHT = 3f;
    public static final float PLAYER_JUMP_RADIUS = 2f;
    
    public final Set<GameEvent> events;
    
    public GameEvent currentEvent;
    
    public boolean paused = false;
    
    public Game(Ship playerShip, int playerCash, GameEvent... events) {
        this.playerShip = playerShip;
        this.playerCash = playerCash;
        this.events = new ImmutableSet.Builder<GameEvent>().addAll(
                Arrays.asList(events)).build();
        for (GameEvent event : this.events) {
            event.playerShip = this.playerShip;
            event.game = this;
        }
        this.currentEvent = events[0]; // TODO: choose more intelligently?
    }
    
    public static Game generateNewGame() {
        return new Game(generateNewGamePlayerShip(), 20,
                generateNewGameEvents((int) (EVENTS_REGION_WIDTH
                        * EVENTS_REGION_HEIGHT * .75f)));
    }
    
    private static Ship generateNewGamePlayerShip() {
        return new Ship(ShipLayout.PLAYER, 20, 20, 20, 60, 4,
                new Equipment[] {}, new LifeSupportSystem(1, 1, 0),
                new WeaponSystem(1, 1, 0, Weapon.LIGHT_BLASTER),
                new WeaponSystem(1, 0, 0, null), new ShieldsSystem(1, 1, 0),
                new EngineSystem(1, 1, 0));
    }
    
    private static GameEvent[] generateNewGameEvents(int count) {
        System.out.println("new game events");
        int zoneCols = (int) EVENTS_REGION_WIDTH, zoneRows = (int) EVENTS_REGION_HEIGHT;
        float xMargin = .1f, yMargin = .1f;
        
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
        System.out.println("new game events done");
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
        float x = (zone.x + xMargin + (1f - xMargin * 2)
                * (float) Math.random())
                * EVENTS_REGION_WIDTH / zoneCols;
        float y = (zone.y + yMargin + (1f - yMargin * 2)
                * (float) Math.random())
                * EVENTS_REGION_HEIGHT / zoneRows;
        return new PointF(x, y);
    }
}
