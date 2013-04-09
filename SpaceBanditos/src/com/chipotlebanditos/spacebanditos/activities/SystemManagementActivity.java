package com.chipotlebanditos.spacebanditos.activities;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Canvas;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.ToggleButton;

import com.chipotlebanditos.spacebanditos.R;
import com.chipotlebanditos.spacebanditos.SpaceBanditosApplication;
import com.chipotlebanditos.spacebanditos.model.Ship;
import com.chipotlebanditos.spacebanditos.model.ShipWithAI;
import com.chipotlebanditos.spacebanditos.model.systems.ShipSystem;
import com.chipotlebanditos.spacebanditos.model.systems.WeaponSystem;
import com.chipotlebanditos.spacebanditos.views.AbsShipSystemView;

public class SystemManagementActivity extends Activity {
    
    public static final String INTENT_EXTRA_SYSTEM_INDEX = "android.intent.extra.SYSTEM_INDEX";
    public static final int TARGET_REQUEST_CODE = 1;
    
    private SystemManagementView view;
    
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        
        Ship ship = ((SpaceBanditosApplication) getApplication()).game.playerShip;
        
        view = new SystemManagementView(ship, ship.getSystemByIndex(getIntent()
                .getIntExtra(INTENT_EXTRA_SYSTEM_INDEX, -1)), getBaseContext());
        setContentView(view);
    }
    
    private class SystemManagementView extends AbsShipSystemView {
        
        public Ship ship;
        
        public SystemManagementView(Ship ship, ShipSystem system,
                Context context) {
            super(system, context);
            this.ship = ship;
            
            View.inflate(getContext(), R.layout.activity_system_management,
                    this);
            
        }
        
        protected ToggleButton getRepairButton() {
            return (ToggleButton) findViewById(R.id.repair_button);
        }
        
        protected Button getTargetButton() {
            return (Button) findViewById(R.id.target_button);
        }
        
        protected Button getAddPowerButton() {
            return (Button) findViewById(R.id.add_power_button);
        }
        
        protected Button getRemovePowerButton() {
            return (Button) findViewById(R.id.remove_power_button);
        }
        
        @Override
        protected void onDraw(Canvas canvas) {
            getRepairButton().setChecked(system.beingRepaired);
            getRepairButton().setEnabled(system.damageLevel > 0);
            getTargetButton().setVisibility(
                    system instanceof WeaponSystem ? VISIBLE : GONE);
            ShipWithAI enemyShip = ((SpaceBanditosApplication) getApplication()).game.currentEvent.enemyShip;
            getTargetButton().setEnabled(
                    enemyShip != null && enemyShip.isHostile);
            
            getAddPowerButton().setEnabled(
                    view.ship.power.powerLevel > 0
                            && view.system.getMaxPowerLevel()
                                    - view.system.powerLevel > 0);
            
            getRemovePowerButton().setEnabled(view.system.powerLevel > 0);
            
            super.onDraw(canvas);
            view.invalidate();
        }
    }
    
    public void onBackButtonClick(View v) {
        finish();
    }
    
    public void onRepairButtonClick(View v) {
        view.system.beingRepaired = !view.system.beingRepaired;
    }
    
    public void onTargetButtonClick(View v) {
        if (((SpaceBanditosApplication) getApplication()).game.currentEvent.enemyShip == null) {
            return;
        }
        Intent intent = new Intent(this, EnemyShipActivity.class);
        this.startActivityForResult(intent, TARGET_REQUEST_CODE);
        overridePendingTransition(R.anim.right_to_left_enter,
                R.anim.right_to_left_exit);
    }
    
    public void onAddPowerButtonClick(View v) {
        if (view.ship.power.powerLevel > 0
                && view.system.getMaxPowerLevel() - view.system.powerLevel > 0) {
            view.ship.addPower(view.system);
        }
    }
    
    public void onRemovePowerButtonClick(View v) {
        if (view.system.powerLevel > 0) {
            view.ship.removePower(view.system);
        }
    }
    
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        switch (requestCode) {
        case TARGET_REQUEST_CODE:
            if (resultCode == RESULT_OK) {
                ((WeaponSystem) view.system).target = ((SpaceBanditosApplication) getApplication()).game.currentEvent
                        .getOpposingShip(view.ship)
                        .getSystemByIndex(
                                data.getIntExtra(INTENT_EXTRA_SYSTEM_INDEX, -1));
            }
            break;
        default:
            throw new IllegalArgumentException();
        }
    }
}
