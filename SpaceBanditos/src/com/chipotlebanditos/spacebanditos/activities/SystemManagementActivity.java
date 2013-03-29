package com.chipotlebanditos.spacebanditos.activities;

import android.app.Activity;
import android.content.Context;
import android.graphics.Canvas;
import android.os.Bundle;
import android.view.View;
import android.view.Window;

import com.chipotlebanditos.spacebanditos.R;
import com.chipotlebanditos.spacebanditos.SpaceBanditosApplication;
import com.chipotlebanditos.spacebanditos.model.Ship;
import com.chipotlebanditos.spacebanditos.model.systems.ShipSystem;
import com.chipotlebanditos.spacebanditos.views.AbsShipSystemView;

public class SystemManagementActivity extends Activity {
    
    public static final String INTENT_EXTRA_SYSTEM_INDEX = "android.intent.extra.SYSTEM_INDEX";
    
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        
        Ship ship = ((SpaceBanditosApplication) getApplication()).game.playerShip;
        
        setContentView(new SystemManagementView(ship,
                ship.systems.get(getIntent().getIntExtra(
                        INTENT_EXTRA_SYSTEM_INDEX, -1)), getBaseContext()));
    }
    
    private class SystemManagementView extends AbsShipSystemView {
        
        public Ship ship;
        
        public SystemManagementView(Ship ship, ShipSystem system,
                Context context) {
            super(system, context);
            this.ship = ship;
            
            View.inflate(getContext(), R.layout.activity_system_management,
                    this);
            
            this.setBackgroundDrawable(getResources().getDrawable(
                    R.drawable.system_management_background));
        }
        
        @Override
        protected void onDraw(Canvas canvas) {
            // TODO
            super.onDraw(canvas);
        }
    }
}
