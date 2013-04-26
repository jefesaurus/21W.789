package com.chipotlebanditos.spacebanditos.activities;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnTouchListener;
import android.widget.Button;

import com.chipotlebanditos.spacebanditos.R;
import com.chipotlebanditos.spacebanditos.SpaceBanditosApplication;
import com.chipotlebanditos.spacebanditos.model.Game;
import com.chipotlebanditos.spacebanditos.model.GameEvent;
import com.chipotlebanditos.spacebanditos.model.systems.EngineSystem;
import com.chipotlebanditos.spacebanditos.model.systems.WeaponSystem;
import com.chipotlebanditos.spacebanditos.views.EventsView;

public class JumpActivity extends Activity {
    
    private GameEvent nextEvent;
    
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_jump);
        
        getJumpButton().setEnabled(false);
        getEventsView().setOnTouchListener(new OnTouchListener() {
            
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                getEventsView().onTouch(v, event);
                getJumpButton().setEnabled(
                        getEventsView().getSelectedEvent() != null);
                return true;
            }
            
        });
    }
    
    private Button getJumpButton() {
        return (Button) findViewById(R.id.jump_button);
    }
    
    private EventsView getEventsView() {
        return (EventsView) findViewById(R.id.events_view);
    }
    
    public void onJumpButtonClick(View v) {
        Game game = ((SpaceBanditosApplication) getApplication()).game;
        synchronized (game) {
            game.playerShip.getSystem(EngineSystem.class).jumpMillisFraction = 0f;
            for (WeaponSystem system : game.playerShip
                    .getSystems(WeaponSystem.class)) {
                system.chargeMillisFraction = 0f;
                system.target = null;
            }
            game.currentEvent = getEventsView().getSelectedEvent();
            if (game.currentEvent.isDangerous()) {
                game.paused = true;
            }
        }
        Intent intent = new Intent(this, PlayerShipActivity.class);
        startActivity(intent);
    }
}
