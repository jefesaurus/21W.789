package com.chipotlebanditos.spacebanditos.activities;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import com.chipotlebanditos.spacebanditos.R;
import com.chipotlebanditos.spacebanditos.SpaceBanditosApplication;
import com.chipotlebanditos.spacebanditos.model.Game;
import com.chipotlebanditos.spacebanditos.model.GameEvent;
import com.chipotlebanditos.spacebanditos.model.systems.EngineSystem;

public class JumpActivity extends Activity {
    
    private GameEvent nextEvent;
    
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_jump);
        
        Game game = ((SpaceBanditosApplication) getApplication()).game;
        int index = game.events.indexOf(game.currentEvent) + 1;
        if (index < game.events.size()) {
            nextEvent = game.events.get(index);
        } else {
            nextEvent = null;
        }
        
        ((Button) findViewById(R.id.jump_button)).setEnabled(nextEvent != null);
    }
    
    public void onJumpButtonClick(View v) {
        Game game = ((SpaceBanditosApplication) getApplication()).game;
        game.currentEvent = nextEvent;
        game.playerShip.getSystem(EngineSystem.class).jumpMillisFraction = 0f;
        Intent intent = new Intent(this, PlayerShipActivity.class);
        startActivity(intent);
    }
}
