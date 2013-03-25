package com.chipotlebanditos.spacebanditos.model.systems;

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
            return "WEAPON";
        } else {
            return equipped.getName();
        }
    }
    
    public boolean isCharged() {
        return equipped != null
                && chargeMillis == equipped.baseTotalChargeMillis;
    }
    
    @Override
    public void update(int delta, Ship ship, GameEvent event) {
        super.update(delta, ship, event);
        if (powerLevel == 0 || equipped == null) {
            chargeMillis = 0;
        } else {
            chargeMillis += delta; // TODO: account for power level
            while (target != null
                    && chargeMillis >= equipped.baseTotalChargeMillis) {
                chargeMillis -= equipped.baseTotalChargeMillis;
                ship.attack(equipped.attackDamage, event.getOpposingShip(ship),
                        target);
            }
        }
    }
}
