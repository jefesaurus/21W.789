package com.chipotlebanditos.spacebanditos.model;

public enum Weapon implements Equipment {
    TEST_WEAPON("TEST WEAPON", 10000L, 2, 1);
    
    private final String name;
    
    public final long baseTotalChargeMillis;
    public final int numShots;
    public final int shotDamage;
    
    private Weapon(String name, long baseTotalChargeMillis, int numShots,
            int shotDamage) {
        this.name = name;
        this.baseTotalChargeMillis = baseTotalChargeMillis;
        this.numShots = numShots;
        this.shotDamage = shotDamage;
    }
    
    @Override
    public String getName() {
        return name;
    }
}
