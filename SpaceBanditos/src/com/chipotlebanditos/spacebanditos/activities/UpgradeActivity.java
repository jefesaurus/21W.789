package com.chipotlebanditos.spacebanditos.activities;

import android.app.Activity;
import android.content.Context;
import android.graphics.Canvas;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.chipotlebanditos.spacebanditos.R;
import com.chipotlebanditos.spacebanditos.SpaceBanditosApplication;
import com.chipotlebanditos.spacebanditos.model.Game;
import com.chipotlebanditos.spacebanditos.model.systems.ShipSystem;
import com.chipotlebanditos.spacebanditos.views.AbsShipSystemView;
import com.chipotlebanditos.spacebanditos.views.LayeredSegmentFillBar;

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
        
        private LayeredSegmentFillBar getHullBar() {
            return (LayeredSegmentFillBar) findViewById(R.id.hull_bar);
        }
        
        private Button getRepairHullButton() {
            return (Button) findViewById(R.id.repair_hull_button);
        }
        
        private TextView getCash() {
            return (TextView) findViewById(R.id.cash);
        }
        
        private TextView getCurrentUpgradeDescription() {
            return (TextView) findViewById(R.id.current_upgrade_description);
        }
        
        private TextView getNextUpgradeDescription() {
            return (TextView) findViewById(R.id.next_upgrade_description);
        }
        
        private Button getUpgradeButton() {
            return (Button) findViewById(R.id.upgrade_button);
        }
        
        @Override
        protected void onDraw(Canvas canvas) {
            Game game = ((SpaceBanditosApplication) getApplication()).game;
            getHullBar().setLayerValue(0, game.playerShip.maxHull);
            getHullBar().setLayerValue(3, game.playerShip.hull);
            getRepairHullButton().setEnabled(isReadyToRepairHull());
            getCash().setText("$" + game.playerCash);
            getCurrentUpgradeDescription().setText(
                    selected == null ? "" : selected.upgrades
                            .getUpgradeDescription(selected.upgradeLevel));
            getNextUpgradeDescription().setText(
                    selected == null ? "" : selected.upgrades
                            .getUpgradeDescription(selected.upgradeLevel + 1));
            getUpgradeButton().setEnabled(isReadyToUpgrade());
            super.onDraw(canvas);
        }
        
        private class UpgradeSystemView extends AbsShipSystemView {
            
            public UpgradeSystemView(ShipSystem system, Context context) {
                super(system, context);
                View.inflate(getContext(), R.layout.upgrade_system_view, this);
            }
            
            @Override
            protected void onDrawPowerBar() {
                getPowerBar().setLayerValue(2,
                        system.upgrades.getMaxUpgradeLevel());
                getPowerBar().setLayerValue(3, system.upgradeLevel);
            }
            
            @Override
            protected void onDraw(Canvas canvas) {
                setSelected(selected == system);
                super.onDraw(canvas);
            }
        }
    }
    
    private boolean isReadyToRepairHull() {
        Game game = ((SpaceBanditosApplication) getApplication()).game;
        return game.playerShip.hull < game.playerShip.maxHull;
        // TODO: cost cash
    }
    
    public void onRepairHullButtonClick(View v) {
        if (isReadyToRepairHull()) {
            ((SpaceBanditosApplication) getApplication()).game.playerShip.hull++;
            // TODO: cost cash
        }
    }
    
    public void onBackButtonClick(View v) {
        finish();
    }
    
    private boolean isReadyToUpgrade() {
        return selected != null
                && selected.upgradeLevel < selected.upgrades
                        .getMaxUpgradeLevel()
                && selected.upgrades.getUpgradeCost(selected.upgradeLevel + 1) <= ((SpaceBanditosApplication) getApplication()).game.playerCash;
    }
    
    public void onUpgradeButtonClick(View v) {
        if (isReadyToUpgrade()) {
            ((SpaceBanditosApplication) getApplication()).game.playerCash -= selected.upgrades
                    .getUpgradeCost(selected.upgradeLevel + 1);
            selected.upgrade();
        }
    }
}
