package com.chipotlebanditos.spacebanditos.model.systems;

import com.chipotlebanditos.spacebanditos.R;
import com.chipotlebanditos.spacebanditos.model.GameEvent;
import com.chipotlebanditos.spacebanditos.model.Ship;

public class EngineSystem extends ShipSystem {
    
    private static final long serialVersionUID = 7180312143894720628L;
    
    public float jumpMillisFraction = 0;
    
    public static final long BASE_TOTAL_JUMP_MILLIS = 1000L;
    
    public EngineSystem(int upgradeLevel, int powerLevel, int damageLevel) {
        super(upgradeLevel, powerLevel, damageLevel);
    }
    
    @Override
    public String getName() {
        return "ENGINE";
    }
    
    @Override
    public int getIconResource() {
        return R.drawable.life_support_icon;
    }
    
    public long getTotalJumpMillis() {
        if (powerLevel == 0) {
            throw new IllegalStateException();
        }
        return (long) Math.floor(BASE_TOTAL_JUMP_MILLIS
                / (1f + .05 * (powerLevel - 1)));
    }
    
    public boolean isReadyForJump() {
        return jumpMillisFraction == 1f;
    }
    
    public float getEvasion() {
        return powerLevel * 0.05f;
    }
    
    @Override
    public void update(int delta, Ship ship, GameEvent event) {
        super.update(delta, ship, event);
        if (powerLevel > 0) {
            if (ship == event.playerShip && !event.isDangerous()) {
                jumpMillisFraction = 1f;
            } else {
                jumpMillisFraction += (float) delta / getTotalJumpMillis();
                jumpMillisFraction = Math.min(jumpMillisFraction, 1f);
            }
        }
    }
}
