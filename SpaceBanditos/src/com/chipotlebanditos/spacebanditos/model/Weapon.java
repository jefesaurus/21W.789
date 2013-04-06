package com.chipotlebanditos.spacebanditos.model;

public enum Weapon implements Equipment {
    TEST_WEAPON("TEST WEAPON", 5000L, 2);
    
    private final String name;
    
    public final long baseTotalChargeMillis;
    public final int attackDamage;
    
    private Weapon(String name, long baseTotalChargeMillis, int attackDamage) {
        this.name = name;
        this.baseTotalChargeMillis = baseTotalChargeMillis;
        this.attackDamage = attackDamage;
    }
    
    @Override
    public String getName() {
        return name;
    }
}
