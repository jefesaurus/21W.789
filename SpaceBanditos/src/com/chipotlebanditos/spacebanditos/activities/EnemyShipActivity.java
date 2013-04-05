package com.chipotlebanditos.spacebanditos.activities;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;

import com.chipotlebanditos.spacebanditos.R;
import com.chipotlebanditos.spacebanditos.SpaceBanditosApplication;
import com.chipotlebanditos.spacebanditos.model.Ship;
import com.chipotlebanditos.spacebanditos.views.ShipSystemView;
import com.chipotlebanditos.spacebanditos.views.ShipView;

public class EnemyShipActivity extends ShipActivity {
    
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        
        ViewGroup root = (ViewGroup) findViewById(android.R.id.content);
        View.inflate(root.getContext(), R.layout.activity_enemy_ship, root);
        
        final Ship ship = ((SpaceBanditosApplication) getApplication()).game.currentEvent.enemyShip;
        
        ShipView shipView = (ShipView) findViewById(R.id.ship);
        
        shipView.setShip(ship);
        ShipView.SystemsView systemsView = (ShipView.SystemsView) shipView
                .findViewById(R.id.systems);
        
        if (getCallingActivity() != null) {
            for (int i = 0; i < systemsView.getChildCount(); i++) {
                ShipSystemView view = (ShipSystemView) systemsView
                        .getChildAt(i);
                view.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Intent intent = new Intent();
                        intent.putExtra(
                                SystemManagementActivity.INTENT_EXTRA_SYSTEM_INDEX,
                                ship.getSystemIndex(((ShipSystemView) v).system));
                        setResult(RESULT_OK, intent);
                        finish();
                        overridePendingTransition(R.anim.left_to_right_enter,
                                R.anim.left_to_right_exit);
                    }
                });
            }
        }
    }
    
    public void onPlayerShipScreenButtonClick(View v) {
        if (getCallingActivity() != null) {
            setResult(RESULT_CANCELED, null);
        }
        finish();
        overridePendingTransition(R.anim.left_to_right_enter,
                R.anim.left_to_right_exit);
    }
    
    @Override
    public void onShipDestroyed() {
        if (getCallingActivity() != null) {
            setResult(RESULT_CANCELED, null);
        }
        finish();
        overridePendingTransition(0, 0);
    }
}
