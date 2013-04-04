package com.chipotlebanditos.spacebanditos.views;

import android.content.Context;
import android.util.AttributeSet;
import android.widget.AbsoluteLayout;

import com.chipotlebanditos.spacebanditos.model.Ship;
import com.chipotlebanditos.spacebanditos.model.systems.ShipSystem;

public class ShipView extends AbsoluteLayout {
    
    private Ship ship = null;
    
    public ShipView(Context context) {
        super(context);
    }
    
    public ShipView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }
    
    public ShipView(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
    }
    
    public void setShip(Ship ship) {
        if (ship != this.ship) {
            this.removeAllViews();
            this.ship = ship;
            if (ship != null) {
                createShipSystemViews();
            }
        }
    }
    
    public Ship getShip() {
        return ship;
    }
    
    private void createShipSystemViews() {
        removeAllViews();
        for (ShipSystem system : ship.systems) {
            ShipSystemView view = new ShipSystemView(system, getContext());
            addView(view);
        }
    }
    
    private void updateSystemViewPositions() {
        for (int i = 0; i < getChildCount(); i++) {
            ShipSystemView view = (ShipSystemView) getChildAt(i);
            view.setX(ship.getLayoutPosition(view.system).x * getWidth()
                    - view.getWidth() / 2);
            view.setY(ship.getLayoutPosition(view.system).y * getHeight()
                    - view.getHeight() / 2);
        }
    }
    
    @Override
    protected void onLayout(boolean changed, int l, int t, int r, int b) {
        this.setBackgroundResource(ship.layout.imageResource);
        updateSystemViewPositions();
        super.onLayout(changed, l, t, r, b);
    }
}
