package com.chipotlebanditos.spacebanditos.activities;

import android.app.Activity;
import android.os.Bundle;

import com.chipotlebanditos.spacebanditos.SpaceBanditosApplication;
import com.chipotlebanditos.spacebanditos.model.Game;
import com.example.spacebanditos.R;

public class PlayerShipActivity extends Activity {
    
    private boolean destroyed = false;
    
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_player_ship);
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
            while (!PlayerShipActivity.this.destroyed) {
                long currentTimeMillis = System.currentTimeMillis();
                int delta = (int) (currentTimeMillis - previousTimeMillis);
                Game game = ((SpaceBanditosApplication) getApplication()).game;
                if (game.paused) {
                    game.currentEvent.update(delta, game);
                }
                previousTimeMillis = currentTimeMillis;
            }
        }
    }
    
}
