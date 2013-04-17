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
        return R.drawable.weapon_icon;
    }
    
    public boolean isCharged() {
        return equipped != null && chargeMillisFraction == 1f;
    }
    
    public long getTotalChargeMillis() {
        if (powerLevel == 0) {
            throw new IllegalStateException();
        }
        return (long) Math.floor(equipped.baseTotalChargeMillis
                / (1f + .05 * (powerLevel - 1)));
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
            chargeMillisFraction += (float) delta / getTotalChargeMillis();
            while (target != null && chargeMillisFraction >= 1f) {
                chargeMillisFraction -= 1f;
                ship.attack(equipped.shotDamage, equipped.numShots,
                        event.getOpposingShip(ship), target, event);
            }
            chargeMillisFraction = Math.min(chargeMillisFraction, 1f);
        }
    }
}
