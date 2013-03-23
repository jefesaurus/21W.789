package com.chipotlebanditos.spacebanditos.model;

public enum Weapon implements Equipment {
    ;
    
    private final String name;
    
    public final int baseTotalChargeMillis;
    
    private Weapon(String name, int baseTotalChargeMillis) {
        this.name = name;
        this.baseTotalChargeMillis = baseTotalChargeMillis;
    }
    
    @Override
    public String getName() {
        return name;
    }
}
