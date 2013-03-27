package com.chipotlebanditos.spacebanditos.activities;

import android.app.Activity;
import android.os.Bundle;
import android.widget.LinearLayout;

import com.chipotlebanditos.spacebanditos.R;
import com.chipotlebanditos.spacebanditos.SpaceBanditosApplication;
import com.chipotlebanditos.spacebanditos.model.Game;
import com.chipotlebanditos.spacebanditos.model.systems.ShipSystem;
import com.chipotlebanditos.spacebanditos.views.ShipSystemView;

public class PlayerShipActivity extends Activity {
    
    private boolean destroyed = false;
    
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_player_ship);
        LinearLayout ll = (LinearLayout) findViewById(R.id.systems);
        for (ShipSystem system : ((SpaceBanditosApplication) getApplication()).game.playerShip.systems) {
            ll.addView(new ShipSystemView(system, ll.getContext()));
        }
        new RunGameThread().start();
    }
    
    @Override
    protected void onDestroy() {
        destroyed = true;
    }
    
    private class RunGameThread extends Thread {
        
        private long previousTimeMillis = System.currentTimeMillis();
        
        @Override
        public void run() {
            while (true) {
                // all in-event UI code should be similarly synchronized
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
