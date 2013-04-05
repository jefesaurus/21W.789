package com.chipotlebanditos.spacebanditos.activities;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import com.chipotlebanditos.spacebanditos.R;
import com.chipotlebanditos.spacebanditos.SpaceBanditosApplication;
import com.chipotlebanditos.spacebanditos.model.Game;

public abstract class ShipActivity extends Activity {
    
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        
        setContentView(R.layout.activity_ship);
        
        Game game = ((SpaceBanditosApplication) getApplication()).game;
        ((Button) findViewById(R.id.pause_button))
                .setText(game.paused ? "UNPAUSE" : "PAUSE");
    }
    
    public void onPauseButtonClick(View v) {
        Game game = ((SpaceBanditosApplication) getApplication()).game;
        game.paused = !game.paused;
        ((Button) v).setText(game.paused ? "UNPAUSE" : "PAUSE");
    }
    
    public abstract void onShipDestroyed();
}
