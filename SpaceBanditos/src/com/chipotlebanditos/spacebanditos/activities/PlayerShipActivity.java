package com.chipotlebanditos.spacebanditos.activities;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;

import com.chipotlebanditos.spacebanditos.R;
import com.chipotlebanditos.spacebanditos.SpaceBanditosApplication;
import com.chipotlebanditos.spacebanditos.model.Game;
import com.chipotlebanditos.spacebanditos.model.Ship;
import com.chipotlebanditos.spacebanditos.views.ShipSystemView;
import com.chipotlebanditos.spacebanditos.views.ShipView;

public class PlayerShipActivity extends ShipActivity {
    
    private boolean destroyed = false;
    
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        
        ViewGroup root = (ViewGroup) findViewById(android.R.id.content);
        View.inflate(root.getContext(), R.layout.activity_player_ship, root);
        
        final Ship ship = ((SpaceBanditosApplication) getApplication()).game.playerShip;
        
        ShipView shipView = (ShipView) findViewById(R.id.ship);
        
        shipView.setShip(ship);
        shipView.findViewById(R.id.reserve_power).setVisibility(View.VISIBLE);
        ShipView.SystemsView systemsView = (ShipView.SystemsView) shipView
                .findViewById(R.id.systems);
        
        for (int i = 0; i < systemsView.getChildCount(); i++) {
            ShipSystemView view = (ShipSystemView) systemsView.getChildAt(i);
            view.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent = new Intent(PlayerShipActivity.this,
                            SystemManagementActivity.class);
                    intent.putExtra(
                            SystemManagementActivity.INTENT_EXTRA_SYSTEM_INDEX,
                            ship.getSystemIndex(((ShipSystemView) v).system));
                    startActivity(intent);
                }
            });
        }
        new RunGameThread().start();
    }
    
    @Override
    protected void onDestroy() {
        super.onDestroy();
        destroyed = true;
    }
    
    public void onEnemyShipScreenButtonClick(View v) {
        Intent intent = new Intent(this, EnemyShipActivity.class);
        startActivity(intent);
        overridePendingTransition(R.anim.right_to_left_enter,
                R.anim.right_to_left_exit);
    }
    
    @Override
    public void onShipDestroyed() {
        // TODO: game over
    }
    
    private class RunGameThread extends Thread {
        
        private long previousTimeMillis = System.currentTimeMillis();
        
        @Override
        public void run() {
            while (true) {
                // TODO: all in-event UI code should be similarly synchronized
                synchronized (((SpaceBanditosApplication) getApplication()).game) {
                    if (PlayerShipActivity.this.destroyed) {
                        break;
                    }
                    long currentTimeMillis = System.currentTimeMillis();
                    int delta = (int) (currentTimeMillis - previousTimeMillis);
                    Game game = ((SpaceBanditosApplication) getApplication()).game;
                    if (!game.paused) {
                        game.currentEvent.update(delta, game);
                    }
                    previousTimeMillis = currentTimeMillis;
                }
            }
        }
    }
    
}
