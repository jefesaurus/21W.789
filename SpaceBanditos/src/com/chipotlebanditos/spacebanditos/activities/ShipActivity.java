package com.chipotlebanditos.spacebanditos.activities;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.RelativeLayout;

import com.chipotlebanditos.spacebanditos.R;
import com.chipotlebanditos.spacebanditos.SpaceBanditosApplication;
import com.chipotlebanditos.spacebanditos.model.Game;
import com.chipotlebanditos.spacebanditos.model.Ship;
import com.chipotlebanditos.spacebanditos.views.LayeredSegmentFillBar;
import com.chipotlebanditos.spacebanditos.views.SystemsView;

public abstract class ShipActivity extends Activity {
    
    protected abstract class ShipView extends RelativeLayout {
        
        private Ship ship = null;
        
        public ShipView(Context context) {
            super(context);
            
        }
        
        public void setShip(Ship ship) {
            this.ship = ship;
            ((SystemsView) findViewById(R.id.systems)).setShip(ship);
        }
        
        public Ship getShip() {
            return ship;
        }
        
        private LayeredSegmentFillBar getHullAndShieldsBar() {
            return (LayeredSegmentFillBar) findViewById(R.id.hull_and_shields_bar);
        }
        
        private Button getPauseButton() {
            return (Button) findViewById(R.id.pause_button);
        }
        
        private Button getJumpButton() {
            return (Button) findViewById(R.id.jump_button);
        }
        
        @Override
        protected void onLayout(boolean changed, int l, int t, int r, int b) {
            if (ship.hasBeenDestroyed() && getContext() instanceof ShipActivity) {
                ((ShipActivity) getContext()).onShipDestroyed();
            }
            getHullAndShieldsBar().setLayerValue(0,
                    ship.maxHull + ship.getMaxShields());
            getHullAndShieldsBar().setLayerValue(1,
                    ship.hull + ship.getMaxShields());
            getHullAndShieldsBar().setLayerValue(2,
                    ship.hull + ship.getShields());
            getHullAndShieldsBar().setLayerValue(3, ship.hull);
            
            Game game = ((SpaceBanditosApplication) getApplication()).game;
            getPauseButton().setText(game.paused ? "UNPAUSE" : "PAUSE");
            getJumpButton().setEnabled(ship.isReadyForJump());
            
            super.onLayout(changed, l, t, r, b);
        }
    }
    
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.in_game, menu);
        return true;
    }
    
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
        case R.id.action_main_menu:
            Intent intent = new Intent(this, MainMenuActivity.class);
            startActivity(intent);
            return true;
        case R.id.action_settings:
            // TODO: go to settings
            return true;
        default:
            return super.onOptionsItemSelected(item);
        }
    }
    
    public void onUpgradesButtonClick(View v) {
        // TODO: go to upgrades
    }
    
    public void onEquipmentButtonClick(View v) {
        // TODO: go to equipment
    }
    
    public void onPauseButtonClick(View v) {
        Game game = ((SpaceBanditosApplication) getApplication()).game;
        game.paused = !game.paused;
    }
    
    public void onJumpButtonClick(View v) {
        // TODO: go to jump
    }
    
    public abstract void onShipDestroyed();
    
}
