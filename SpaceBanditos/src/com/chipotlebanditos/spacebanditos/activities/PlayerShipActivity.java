package com.chipotlebanditos.spacebanditos.activities;

import android.app.Activity;
import android.graphics.Point;
import android.os.Bundle;
import android.view.Display;
import android.widget.RelativeLayout;

import com.chipotlebanditos.spacebanditos.R;
import com.chipotlebanditos.spacebanditos.SpaceBanditosApplication;
import com.chipotlebanditos.spacebanditos.model.Game;
import com.chipotlebanditos.spacebanditos.model.systems.ShipSystem;
import com.chipotlebanditos.spacebanditos.views.ShipSystemView;

public class PlayerShipActivity extends Activity {
    
    private boolean destroyed = false;
    
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        Display display = getWindowManager().getDefaultDisplay();
        Point size = new Point();
        display.getSize(size);
        
        super.onCreate(savedInstanceState);
        
        /*
         * quirksmode method to get the size of the ship view before it has
         * actually been set. Get the display size and use our own settings to
         * figure out what the view size will be
         */
        int shipViewHeight = size.y - 50;
        int shipViewWidth = size.x - 50;
        
        setContentView(R.layout.activity_player_ship);
        RelativeLayout ll = (RelativeLayout) findViewById(R.id.systems);
        for (ShipSystem system : ((SpaceBanditosApplication) getApplication()).game.playerShip.systems) {
            RelativeLayout.LayoutParams params = new RelativeLayout.LayoutParams(
                    RelativeLayout.LayoutParams.WRAP_CONTENT,
                    RelativeLayout.LayoutParams.WRAP_CONTENT);
            params.leftMargin = (int) (system.x * shipViewWidth);
            params.topMargin = (int) (system.y * shipViewHeight);
            ll.addView(new ShipSystemView(system, ll.getContext()), params);
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
