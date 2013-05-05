package com.chipotlebanditos.spacebanditos.model.systems;

import com.chipotlebanditos.spacebanditos.R;
import com.chipotlebanditos.spacebanditos.model.GameEvent;
import com.chipotlebanditos.spacebanditos.model.Ship;
import com.chipotlebanditos.spacebanditos.model.Weapon;

public class WeaponSystem extends ShipSystem {
    
    private static final long serialVersionUID = -6638405764604872553L;
    
    public Weapon equipped;
    
    public ShipSystem target = null;
    
    public float chargeMillisFraction = 0f;
    
    public WeaponSystem(int upgradeLevel, int powerLevel, int damageLevel,
            Weapon weapon) {
        super(upgradeLevel, powerLevel, damageLevel, new SystemUpgradeSequence(
                0, 20, 30, 50, 80) {
            
            @Override
            public String calculateUpgradeDescription(int level) {
                return String.format("%s%% faster charge", 5 * (level - 1));
            }
            
        });
        this.equipped = weapon;
    }
    
    @Override
    public String getName() {
        if (equipped == null) {
            return "NO WEAPON";
        } else {
            return equipped.getName();
        }
    }
    
    @Override
    public int getIconResource() {
        return R.drawable.weapon_icon;
    }
    
    public boolean isCharged() {
        return equipped != null && chargeMillisFraction == 1f;
    }
    
    public long getTotalChargeMillis(Ship ship) {
        if (powerLevel == 0) {
            throw new IllegalStateException();
        }
        return (long) Math.floor(equipped.baseTotalChargeMillis
                / (1f + .05f * (powerLevel - 1) + (beingRepaired ? 0f
                        : .0005f * ship.crew)));
    }
    
    @Override
    public void update(int delta, Ship ship, GameEvent event) {
        super.update(delta, ship, event);
        if (event.getOpposingShip(ship) == null) {
            target = null;
        }
        if (powerLevel == 0 || equipped == null) {
            chargeMillisFraction = 0f;
        } else {
            chargeMillisFraction += (float) delta / getTotalChargeMillis(ship);
            while (target != null && chargeMillisFraction >= 1f) {
                chargeMillisFraction -= 1f;
                ship.attack(equipped.shotDamage, equipped.numShots,
                        event.getOpposingShip(ship), target, event);
            }
            chargeMillisFraction = Math.min(chargeMillisFraction, 1f);
        }
    }
}
