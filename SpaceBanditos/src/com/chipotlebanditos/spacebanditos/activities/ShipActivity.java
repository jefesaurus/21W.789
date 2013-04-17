package com.chipotlebanditos.spacebanditos.activities;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.chipotlebanditos.spacebanditos.R;
import com.chipotlebanditos.spacebanditos.SpaceBanditosApplication;
import com.chipotlebanditos.spacebanditos.model.Game;
import com.chipotlebanditos.spacebanditos.model.Ship;
import com.chipotlebanditos.spacebanditos.model.SoundPlayer;
import com.chipotlebanditos.spacebanditos.views.LayeredSegmentFillBar;
import com.chipotlebanditos.spacebanditos.views.SystemsView;

public abstract class ShipActivity extends Activity {
    
    protected static final int SWIPE_MIN_DISTANCE = 100;
    protected static final int SWIPE_THRESHOLD_VELOCITY = 150;
    
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }
    
    protected abstract class ShipView extends RelativeLayout {
        
        private Ship ship = null;
        private SoundPlayer soundPlayer;
        
        public ShipView(Context context) {
            super(context);
            soundPlayer = new SoundPlayer(getContext());
            setPadding(
                    getResources().getDimensionPixelSize(
                            R.dimen.activity_vertical_margin),
                    getResources().getDimensionPixelSize(
                            R.dimen.activity_horizontal_margin),
                    getResources().getDimensionPixelSize(
                            R.dimen.activity_vertical_margin),
                    getResources().getDimensionPixelSize(
                            R.dimen.activity_horizontal_margin));
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
        
        private Button getUpgradesButton() {
            return (Button) findViewById(R.id.upgrades_button);
        }
        
        private Button getEquipmentButton() {
            return (Button) findViewById(R.id.equipment_button);
        }
        
        private Button getPauseButton() {
            return (Button) findViewById(R.id.pause_button);
        }
        
        private Button getJumpButton() {
            return (Button) findViewById(R.id.jump_button);
        }
        
        private TextView getPausedMessage() {
            return (TextView) findViewById(R.id.paused_message);
        }
        
        @Override
        protected void onLayout(boolean changed, int l, int t, int r, int b) {
            if (ship.hasBeenDestroyed()) {
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
            
            getUpgradesButton().setEnabled(!game.currentEvent.isDangerous());
            getEquipmentButton().setEnabled(!game.currentEvent.isDangerous());
            
            getPauseButton().setText(game.paused ? "UNPAUSE" : "PAUSE");
            getJumpButton().setEnabled(game.playerShip.isReadyForJump());
            getPausedMessage().setVisibility(game.paused ? VISIBLE : GONE);
            
            while (!ship.soundsQueue.isEmpty()) {
                soundPlayer.playSound(ship.soundsQueue.poll());
            }
            
            Ship opponent = game.currentEvent.getOpposingShip(ship);
            if (opponent != null) {
                opponent.soundsQueue.clear();
            }
            
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
        case R.id.action_save_and_quit:
            Intent intent = new Intent(this, MainMenuActivity.class);
            startActivity(intent);
            return true;
        case R.id.action_settings:
            ((SpaceBanditosApplication) getApplication()).game.paused = true;
            intent = new Intent(this, SettingsMenuActivity.class);
            startActivity(intent);
            return true;
        default:
            return super.onOptionsItemSelected(item);
        }
    }
    
    public void onUpgradesButtonClick(View v) {
        Toast.makeText(this, "Not yet implemented, coming soon!",
                Toast.LENGTH_SHORT).show();
        // TODO: go to upgrades
    }
    
    public void onEquipmentButtonClick(View v) {
        Toast.makeText(this, "Not yet implemented, coming soon!",
                Toast.LENGTH_SHORT).show();
        // TODO: go to equipment
    }
    
    public void onPauseButtonClick(View v) {
        Game game = ((SpaceBanditosApplication) getApplication()).game;
        game.paused = !game.paused;
    }
    
    public void onJumpButtonClick(View v) {
        Intent intent = new Intent(this, JumpActivity.class);
        startActivity(intent);
    }
    
    public abstract void onShipDestroyed();
    
}
