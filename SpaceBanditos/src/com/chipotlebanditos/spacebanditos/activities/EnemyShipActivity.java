package com.chipotlebanditos.spacebanditos.activities;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import com.chipotlebanditos.spacebanditos.R;
import com.chipotlebanditos.spacebanditos.SpaceBanditosApplication;
import com.chipotlebanditos.spacebanditos.model.Ship;
import com.chipotlebanditos.spacebanditos.views.ShipSystemView;
import com.chipotlebanditos.spacebanditos.views.ShipView;

public class EnemyShipActivity extends Activity {
    
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        
        setContentView(R.layout.activity_enemy_ship);
        
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
                        EnemyShipActivity.this.setResult(RESULT_OK, intent);
                        EnemyShipActivity.this.finish();
                        // TODO: return enemy ship activity result
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
        // Intent intent = new Intent(this, PlayerShipActivity.class);
        // startActivity(intent);
    }
    
}
