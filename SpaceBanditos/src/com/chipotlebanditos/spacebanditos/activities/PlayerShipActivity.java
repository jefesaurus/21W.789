package com.chipotlebanditos.spacebanditos.activities;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.chipotlebanditos.spacebanditos.R;
import com.chipotlebanditos.spacebanditos.SpaceBanditosApplication;
import com.chipotlebanditos.spacebanditos.model.Game;
import com.chipotlebanditos.spacebanditos.model.Ship;
import com.chipotlebanditos.spacebanditos.views.LayeredSegmentFillBar;
import com.chipotlebanditos.spacebanditos.views.ShipSystemView;
import com.chipotlebanditos.spacebanditos.views.SystemsView;

;

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
            SystemsView systemsView = (SystemsView) findViewById(R.id.systems);
            systemsView.setReversed(false);
            
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
        
        private TextView getAtmosphere() {
            return (TextView) findViewById(R.id.atmosphere);
        }
        
        private TextView getCrew() {
            return (TextView) findViewById(R.id.crew);
        }
        
        private TextView getEvasion() {
            return (TextView) findViewById(R.id.evasion);
        }
        
        private LayeredSegmentFillBar getPowerBar() {
            return (LayeredSegmentFillBar) findViewById(R.id.reserve_power_bar);
        }
        
        @Override
        public void onLayout(boolean changed, int l, int t, int r, int b) {
            getAtmosphere().setText(
                    "ATMO: " + (int) Math.floor(getShip().atmosphere) + "/"
                            + (int) Math.floor(getShip().getMaxAtmosphere()));
            getCrew().setText(
                    "CREW: " + getShip().crew + "/"
                            + getShip().getSustainableCrew() + "/"
                            + getShip().getMaxSustainableCrew());
            getEvasion().setText(
                    "EVADE: " + (int) Math.round(getShip().getEvasion() * 100f)
                            + "%");
            
            getPowerBar().setLayerValue(2, getShip().power.upgradeLevel);
            getPowerBar().setLayerValue(3, getShip().power.powerLevel);
            
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
