package com.chipotlebanditos.spacebanditos.views;

import android.content.Context;
import android.view.View;

import com.chipotlebanditos.spacebanditos.R;
import com.chipotlebanditos.spacebanditos.model.systems.ShipSystem;

public class ShipSystemView extends AbsShipSystemView {
    
    public ShipSystemView(ShipSystem system, Context context) {
        super(system, context);
        View.inflate(context, R.layout.ship_system_view, this);
        this.setBackgroundResource(R.drawable.ship_system_view_background);
    }
}
