package com.chipotlebanditos.spacebanditos.activities;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import com.chipotlebanditos.spacebanditos.R;
import com.chipotlebanditos.spacebanditos.SpaceBanditosApplication;
import com.chipotlebanditos.spacebanditos.model.Game;

public class MainMenuActivity extends Activity {
    
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main_menu);
    }
    
    @Override
    protected void onResume() {
        super.onResume();
        Game game = ((SpaceBanditosApplication) getApplication()).game;
        ((Button) findViewById(R.id.continue_button)).setEnabled(game != null
                && !game.playerShip.hasBeenDestroyed());
    }
    
    public void onNewGameButtonClick(View view) {
        ((SpaceBanditosApplication) getApplication()).game = Game
                .generateNewGame();
        Intent intent = new Intent(this, PlayerShipActivity.class);
        startActivity(intent);
    }
    
    public void onContinueGameButtonClick(View view) {
        Intent intent = new Intent(this, PlayerShipActivity.class);
        startActivity(intent);
    }
    
    public void onSettingsButtonClick(View view) {
        Intent intent = new Intent(this, SettingsMenuActivity.class);
        startActivity(intent);
    }
    
}
