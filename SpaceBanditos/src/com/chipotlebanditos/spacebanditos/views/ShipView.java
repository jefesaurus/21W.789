package com.chipotlebanditos.spacebanditos.views;

import android.content.Context;
import android.util.AttributeSet;
import android.view.View;
import android.widget.AbsoluteLayout;
import android.widget.RelativeLayout;

import com.chipotlebanditos.spacebanditos.R;
import com.chipotlebanditos.spacebanditos.activities.ShipActivity;
import com.chipotlebanditos.spacebanditos.model.Ship;
import com.chipotlebanditos.spacebanditos.model.systems.ShipSystem;

public class ShipView extends RelativeLayout {
    
    private Ship ship = null;
    
    public ShipView(Context context) {
        super(context);
        construct(context);
    }
    
    public ShipView(Context context, AttributeSet attrs) {
        super(context, attrs);
        construct(context);
    }
    
    public ShipView(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        construct(context);
    }
    
    private void construct(Context context) {
        View.inflate(context, R.layout.ship_view, this);
    }
    
    public void setShip(Ship ship) {
        this.ship = ship;
        ((SystemsView) findViewById(R.id.systems)).setShip(ship);
    }
    
    public Ship getShip() {
        return ship;
    }
    
    private LayeredSegmentFillBar getHullAndShieldsBar() {
        return (LayeredSegmentFillBar) findViewById(R.id.hull_and_shields_bar);
    }
    
    private LayeredSegmentFillBar getPowerBar() {
        return (LayeredSegmentFillBar) findViewById(R.id.reserve_power_bar);
    }
    
    @Override
    protected void onLayout(boolean changed, int l, int t, int r, int b) {
        if (ship.hasBeenDestroyed() && getContext() instanceof ShipActivity) {
            ((ShipActivity) getContext()).onShipDestroyed();
        }
        getHullAndShieldsBar().setLayerValue(0,
                ship.maxHull + ship.getMaxShields());
        getHullAndShieldsBar().setLayerValue(1,
                ship.hull + ship.getMaxShields());
        getHullAndShieldsBar().setLayerValue(2, ship.hull + ship.getShields());
        getHullAndShieldsBar().setLayerValue(3, ship.hull);
        
        getPowerBar().setLayerValue(2, ship.power.upgradeLevel);
        getPowerBar().setLayerValue(3, ship.power.powerLevel);
        
        super.onLayout(changed, l, t, r, b);
    }
    
    public static class SystemsView extends AbsoluteLayout {
        
        private Ship ship = null;
        
        private boolean reversed = false;
        
        public SystemsView(Context context) {
            super(context);
        }
        
        public SystemsView(Context context, AttributeSet attrs) {
            super(context, attrs);
        }
        
        public SystemsView(Context context, AttributeSet attrs, int defStyle) {
            super(context, attrs, defStyle);
        }
        
        public void setShip(Ship ship) {
            if (ship != this.ship) {
                this.removeAllViews();
                this.ship = ship;
                if (ship != null) {
                    ((SystemsView) findViewById(R.id.systems))
                            .createShipSystemViews();
                }
            }
        }
        
        public Ship getShip() {
            return ship;
        }
        
        public void setReversed(boolean reversed) {
            this.reversed = reversed;
        }
        
        public boolean getReversed() {
            return reversed;
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
                float xF = ship.getLayoutPosition(view.system).x, yF = ship
                        .getLayoutPosition(view.system).y;
                if (reversed) {
                    xF = 1 - xF;
                    yF = 1 - yF;
                }
                view.setX(xF * getWidth() - view.getWidth() / 2);
                view.setY(yF * getHeight() - view.getHeight() / 2);
            }
        }
        
        @Override
        protected void onLayout(boolean changed, int l, int t, int r, int b) {
            this.setBackgroundDrawable(reversed ? ship.layout
                    .getImageDrawableReverse(getResources()) : ship.layout
                    .getImageDrawable(getResources()));
            updateSystemViewPositions();
            super.onLayout(changed, l, t, r, b);
        }
    }
}