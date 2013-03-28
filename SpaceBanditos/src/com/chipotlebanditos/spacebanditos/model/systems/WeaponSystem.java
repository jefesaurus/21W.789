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
            Weapon weapon, double x, double y) {
        super(upgradeLevel, powerLevel, damageLevel, x, y);
        this.equipped = weapon;
    }
    
    @Override
    public String getName() {
        if (equipped == null) {
            return "WEAPON";
        } else {
            return equipped.getName();
        }
    }
    
    @Override
    public int getIconResource() {
        return R.drawable.life_support_icon;
    }
    
    public boolean isCharged() {
        return equipped != null
                && chargeMillis == equipped.baseTotalChargeMillis;
    }
    
    public long getTotalChargeMillis() {
        return equipped.baseTotalChargeMillis; // TODO: account for power level
    }
    
    public float getChargeFraction() {
        if (equipped == null) {
            return 0;
        } else {
            return chargeMillis / (float) getTotalChargeMillis();
        }
    }
    
    @Override
    public void update(int delta, Ship ship, GameEvent event) {
        super.update(delta, ship, event);
        if (powerLevel == 0 || equipped == null) {
            chargeMillis = 0;
        } else {
            chargeMillis += delta;
            while (target != null && chargeMillis >= getTotalChargeMillis()) {
                chargeMillis -= getTotalChargeMillis();
                ship.attack(equipped.attackDamage, event.getOpposingShip(ship),
                        target);
            }
            chargeMillis = Math.min(chargeMillis, getTotalChargeMillis());
        }
    }
}
