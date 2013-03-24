package com.chipotlebanditos.spacebanditos.model.systems;

import com.chipotlebanditos.spacebanditos.model.Weapon;

public class WeaponSystem extends ShipSystem {
    
    private static final long serialVersionUID = -6638405764604872553L;
    
    public Weapon equipped;
    
    public int chargeMillis = 0;
    
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
}
