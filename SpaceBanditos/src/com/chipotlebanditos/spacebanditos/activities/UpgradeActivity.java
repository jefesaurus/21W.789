package com.chipotlebanditos.spacebanditos.activities;

import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;

import com.chipotlebanditos.spacebanditos.R;
import com.chipotlebanditos.spacebanditos.SpaceBanditosApplication;
import com.chipotlebanditos.spacebanditos.model.Game;
import com.chipotlebanditos.spacebanditos.model.systems.ShipSystem;
import com.chipotlebanditos.spacebanditos.views.AbsShipSystemView;

public class UpgradeActivity extends Activity {
    
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(new UpgradeView(this));
    }
    
    private class UpgradeView extends RelativeLayout {
        
        private LinearLayout getUpgradeSystemsView() {
            return (LinearLayout) findViewById(R.id.upgrade_systems_view);
        }
        
        public UpgradeView(Context context) {
            super(context);
            View.inflate(context, R.layout.activity_upgrade, this);
            Game game = ((SpaceBanditosApplication) getApplication()).game;
            for (ShipSystem system : game.playerShip.systems) {
                getUpgradeSystemsView().addView(
                        new UpgradeSystemView(system, context));
            }
            getUpgradeSystemsView().addView(
                    new UpgradeSystemView(game.playerShip.power, context));
            
        }
        
        private class UpgradeSystemView extends AbsShipSystemView {
            
            public UpgradeSystemView(ShipSystem system, Context context) {
                super(system, context);
                View.inflate(getContext(), R.layout.upgrade_system_view, this);
            }
            
        }
        
    }
    
}
