package com.chipotlebanditos.spacebanditos.model.systems;

import com.chipotlebanditos.spacebanditos.R;
import com.chipotlebanditos.spacebanditos.model.GameEvent;
import com.chipotlebanditos.spacebanditos.model.Ship;
import com.chipotlebanditos.spacebanditos.model.Weapon;

public class WeaponSystem extends ShipSystem {
    
    private static final long serialVersionUID = -6638405764604872553L;
    
    public Weapon equipped;
    
    public ShipSystem target = null;
    
    public long chargeMillis = 0;
    
    public WeaponSystem(int upgradeLevel, int powerLevel, int damageLevel,
            Weapon weapon) {
        super(upgradeLevel, powerLevel, damageLevel);
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
        return R.drawable.life_support_icon;
    }
    
    public boolean isCharged() {
        return equipped != null && chargeMillis == getTotalChargeMillis();
    }
    
    public long getTotalChargeMillis() {
        if (powerLevel == 0) {
            throw new IllegalStateException();
        }
        return (long) Math.floor(equipped.baseTotalChargeMillis
                / (1f + .05 * (powerLevel - 1)));
    }
    
    public float getChargeFraction() {
        if (equipped == null || powerLevel == 0) {
            return 0;
        } else {
            return chargeMillis / (float) getTotalChargeMillis();
        }
    }
    
    @Override
    public void update(int delta, Ship ship, GameEvent event) {
        super.update(delta, ship, event);
        if (event.getOpposingShip(ship) == null) {
            target = null;
        }
        if (powerLevel == 0 || equipped == null) {
            chargeMillis = 0;
        } else {
            chargeMillis += delta;
            while (target != null && chargeMillis >= getTotalChargeMillis()) {
                chargeMillis -= getTotalChargeMillis();
                ship.attack(equipped.shotDamage, equipped.numShots,
                        event.getOpposingShip(ship), target, event);
            }
            chargeMillis = Math.min(chargeMillis, getTotalChargeMillis());
        }
    }
}
