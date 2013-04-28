package com.chipotlebanditos.spacebanditos.activities;

import android.app.Activity;
import android.content.Context;
import android.graphics.Canvas;
import android.os.Bundle;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.chipotlebanditos.spacebanditos.R;
import com.chipotlebanditos.spacebanditos.SpaceBanditosApplication;
import com.chipotlebanditos.spacebanditos.model.Game;
import com.chipotlebanditos.spacebanditos.model.systems.ShipSystem;
import com.chipotlebanditos.spacebanditos.views.AbsShipSystemView;

public class UpgradeActivity extends Activity {
    
    private ShipSystem selected = null;
    
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(new UpgradeView(this));
    }
    
    private class UpgradeView extends RelativeLayout {
        
        public UpgradeView(Context context) {
            super(context);
            this.setWillNotDraw(false);
            View.inflate(context, R.layout.activity_upgrade, this);
            Game game = ((SpaceBanditosApplication) getApplication()).game;
            for (ShipSystem system : game.playerShip.systems) {
                addSystemView(system, context);
            }
            addSystemView(game.playerShip.power, context);
            
        }
        
        private LinearLayout getUpgradeSystemsView() {
            return (LinearLayout) findViewById(R.id.upgrade_systems_view);
        }
        
        private void addSystemView(ShipSystem system, Context context) {
            UpgradeSystemView view = new UpgradeSystemView(system, context);
            view.setLayoutParams(new LinearLayout.LayoutParams(
                    LayoutParams.MATCH_PARENT, LayoutParams.WRAP_CONTENT, 1));
            view.setOnClickListener(new OnClickListener() {
                
                @Override
                public void onClick(View v) {
                    selected = ((UpgradeSystemView) v).system;
                }
            });
            getUpgradeSystemsView().addView(view);
        }
        
        private TextView getCurrentUpgradeDescription() {
            return (TextView) findViewById(R.id.current_upgrade_description);
        }
        
        private TextView getNextUpgradeDescription() {
            return (TextView) findViewById(R.id.next_upgrade_description);
        }
        
        @Override
        protected void onDraw(Canvas canvas) {
            getCurrentUpgradeDescription().setText(
                    selected == null ? "" : selected.upgrades
                            .getUpgradeDescription(selected.upgradeLevel));
            getNextUpgradeDescription().setText(
                    selected == null ? "" : selected.upgrades
                            .getUpgradeDescription(selected.upgradeLevel + 1));
            super.onDraw(canvas);
        }
        
        private class UpgradeSystemView extends AbsShipSystemView {
            
            public UpgradeSystemView(ShipSystem system, Context context) {
                super(system, context);
                View.inflate(getContext(), R.layout.upgrade_system_view, this);
            }
            
            @Override
            protected void onDrawPowerBar(Canvas canvas) {
                getPowerBar().setLayerValue(2,
                        system.upgrades.getMaxUpgradeLevel());
                getPowerBar().setLayerValue(3, system.upgradeLevel);
            }
        }
        
    }
    
}
