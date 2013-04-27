package com.chipotlebanditos.spacebanditos.model.systems;

import com.chipotlebanditos.spacebanditos.R;
import com.chipotlebanditos.spacebanditos.model.GameEvent;
import com.chipotlebanditos.spacebanditos.model.Ship;

public class EngineSystem extends ShipSystem {
    
    private static final long serialVersionUID = 7180312143894720628L;
    
    public float jumpMillisFraction = 0;
    
    public static final long BASE_TOTAL_JUMP_MILLIS = 20000L;
    
    public EngineSystem(int upgradeLevel, int powerLevel, int damageLevel) {
        super(upgradeLevel, powerLevel, damageLevel, new SystemUpgradeSequence(
                0, 30, 40, 60, 100) {
            
            @Override
            public String getUpgradeDescription(int level) {
                return String.format(
                        "%s\\% chance of evasion, %s\\% faster jump charge",
                        level * 10, 5 * (level - 1));
            }
            
        });
    }
    
    @Override
    public String getName() {
        return "ENGINE";
    }
    
    @Override
    public int getIconResource() {
        return R.drawable.engine_icon;
    }
    
    public long getTotalJumpMillis(Ship ship) {
        if (powerLevel == 0) {
            throw new IllegalStateException();
        }
        return (long) Math.floor(BASE_TOTAL_JUMP_MILLIS
                / (1f + .05f * (powerLevel - 1f) + (beingRepaired ? 0
                        : .0005f * ship.crew)));
    }
    
    public boolean isReadyForJump() {
        return jumpMillisFraction == 1f && powerLevel > 0;
    }
    
    public float getEvasion(Ship ship) {
        if (powerLevel == 0) {
            return 0;
        } else {
            return powerLevel * 0.1f + (beingRepaired ? 0f : .001f * ship.crew);
        }
    }
    
    @Override
    public void update(int delta, Ship ship, GameEvent event) {
        super.update(delta, ship, event);
        if (powerLevel > 0) {
            if (ship == event.playerShip && !event.isDangerous()) {
                jumpMillisFraction = 1f;
            } else {
                jumpMillisFraction += (float) delta / getTotalJumpMillis(ship);
                jumpMillisFraction = Math.min(jumpMillisFraction, 1f);
            }
        }
    }
}
