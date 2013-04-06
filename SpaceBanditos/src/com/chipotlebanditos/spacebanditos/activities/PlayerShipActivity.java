package com.chipotlebanditos.spacebanditos.activities;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

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
        
        setContentView(new PlayerShipActivityView(this));
        
        new RunGameThread().start();
    }
    
    private class PlayerShipActivityView extends ShipActivity.ShipActivityView {
        
        public PlayerShipActivityView(Context context) {
            super(context);
            
            View.inflate(context, R.layout.activity_player_ship, this);
            
            final Ship ship = ((SpaceBanditosApplication) getApplication()).game.playerShip;
            
            setShip(ship);
            findViewById(R.id.reserve_power).setVisibility(View.VISIBLE);
            SystemsView systemsView = (ShipView.SystemsView) findViewById(R.id.systems);
            
            for (int i = 0; i < systemsView.getChildCount(); i++) {
                ShipSystemView view = (ShipSystemView) systemsView
                        .getChildAt(i);
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
        }
        
        private Button getEnemyShipScreenButton() {
            return (Button) findViewById(R.id.enemy_ship_screen_button);
        }
        
        @Override
        public void onLayout(boolean changed, int l, int t, int r, int b) {
            if (((SpaceBanditosApplication) getApplication()).game.currentEvent.enemyShip == null) {
                getEnemyShipScreenButton().setVisibility(GONE);
            } else {
                getEnemyShipScreenButton().setVisibility(VISIBLE);
            }
            super.onLayout(changed, l, t, r, b);
        }
    }
    
    @Override
    protected void onDestroy() {
        super.onDestroy();
        destroyed = true;
        if (((SpaceBanditosApplication) getApplication()).game.playerShip
                .hasBeenDestroyed()) {
            ((SpaceBanditosApplication) getApplication()).game = null;
        }
    }
    
    public void onEnemyShipScreenButtonClick(View v) {
        if (((SpaceBanditosApplication) getApplication()).game.currentEvent.enemyShip == null) {
            return;
        }
        Intent intent = new Intent(this, EnemyShipActivity.class);
        startActivity(intent);
        overridePendingTransition(R.anim.right_to_left_enter,
                R.anim.right_to_left_exit);
    }
    
    @Override
    public void onShipDestroyed() {
        Toast.makeText(this, "Game over!", Toast.LENGTH_LONG).show();
        Intent intent = new Intent(this, MainMenuActivity.class);
        startActivity(intent);
        // TODO: test game over
    }
    
    private class RunGameThread extends Thread {
        
        private long previousTimeMillis = System.currentTimeMillis();
        
        @Override
        public void run() {
            while (true) {
                // TODO: all in-event UI code should be similarly synchronized
                synchronized (getApplication()) {
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
