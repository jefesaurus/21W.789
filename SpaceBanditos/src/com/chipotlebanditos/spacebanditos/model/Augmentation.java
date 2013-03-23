package com.chipotlebanditos.spacebanditos.model;

public enum Augmentation implements Equipment {
    ;
    
    private final String name;
    
    private Augmentation(String name) {
        this.name = name;
    }
    
    @Override
    public String getName() {
        return name;
    }
}
