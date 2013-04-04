package com.chipotlebanditos.spacebanditos.activities;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import com.chipotlebanditos.spacebanditos.R;
import com.chipotlebanditos.spacebanditos.SpaceBanditosApplication;
import com.chipotlebanditos.spacebanditos.model.Game;
import com.chipotlebanditos.spacebanditos.model.Ship;
import com.chipotlebanditos.spacebanditos.views.ShipSystemView;
import com.chipotlebanditos.spacebanditos.views.ShipView;

public class PlayerShipActivity extends Activity {
    
    private boolean destroyed = false;
    
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        
        setContentView(R.layout.activity_player_ship);
        
        final Ship ship = ((SpaceBanditosApplication) getApplication()).game.playerShip;
        
        ShipView shipView = (ShipView) findViewById(R.id.ship);
        shipView.setShip(ship);
        
        for (int i = 0; i < shipView.getChildCount(); i++) {
            ShipSystemView view = (ShipSystemView) shipView.getChildAt(i);
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
        
        // Display display = getWindowManager().getDefaultDisplay();
        // Point size = new Point();
        // display.getSize(size);
        //
        // /*
        // * quirksmode method to get the size of the ship view before it has
        // * actually been set. Get the display size and use our own settings to
        // * figure out what the view size will be
        // */
        // int shipViewHeight = size.y - 50;
        // int shipViewWidth = size.x - 50;
        //
        // setContentView(R.layout.activity_player_ship);
        // AbsoluteLayout systemLayout = (AbsoluteLayout)
        // findViewById(R.id.systems);
        // final Ship ship = ((SpaceBanditosApplication)
        // getApplication()).game.playerShip;
        // for (ShipSystem system : ship.systems) {
        // ShipSystemView view = new ShipSystemView(system,
        // systemLayout.getContext());
        // view.setOnClickListener(new View.OnClickListener() {
        // @Override
        // public void onClick(View v) {
        // Intent intent = new Intent(PlayerShipActivity.this,
        // SystemManagementActivity.class);
        // intent.putExtra(
        // SystemManagementActivity.INTENT_EXTRA_SYSTEM_INDEX,
        // ship.getSystemIndex(((ShipSystemView) v).system));
        // startActivity(intent);
        // }
        // });
        // view.setX(ship.getLayoutPosition(system).x);
        // view.setY(ship.getLayoutPosition(system).y);
        // systemLayout.addView(view);
        // // TODO
        // // RelativeLayout.LayoutParams params = new
        // // RelativeLayout.LayoutParams(
        // // RelativeLayout.LayoutParams.WRAP_CONTENT,
        // // RelativeLayout.LayoutParams.WRAP_CONTENT);
        // // params.leftMargin = (int) (system.x * shipViewWidth);
        // // params.topMargin = (int) (system.y * shipViewHeight);
        // // ll.addView(new ShipSystemView(system, ll.getContext()), params);
        // }
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
