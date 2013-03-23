package com.chipotlebanditos.spacebanditos.model;

import java.io.Serializable;
import java.util.Arrays;
import java.util.List;

public class StoreInventory implements Serializable {
    
    private static final long serialVersionUID = -5976010467499106803L;
    
    public final List<Equipment> forSale;
    
    public StoreInventory(Equipment... forSale) {
        this.forSale = Arrays.asList(forSale);
    }
    
}
