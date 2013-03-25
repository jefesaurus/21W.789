package com.chipotlebanditos.spacebanditos.model;

public enum Weapon implements Equipment {
    ;
    
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
