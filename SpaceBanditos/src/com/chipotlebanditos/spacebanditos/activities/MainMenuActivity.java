package com.chipotlebanditos.spacebanditos.activities;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import com.chipotlebanditos.spacebanditos.R;
import com.chipotlebanditos.spacebanditos.SpaceBanditosApplication;
import com.chipotlebanditos.spacebanditos.model.Game;

public class MainMenuActivity extends Activity {
    
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main_menu);
    }
    
    public void onNewGameButtonClick(View view) {
        Game game = ((SpaceBanditosApplication) getApplication()).game;
        if (game == null || game.playerShip.hasBeenDestroyed()) {
            ((SpaceBanditosApplication) getApplication()).game = Game
                    .generateNewGame();
        }
        Intent intent = new Intent(this, PlayerShipActivity.class);
        startActivity(intent);
    }
    
    public void onContinueGameButtonClick(View view) {
        // TODO figure out how to continue games
    }
    
    public void onSettingsButtonClick(View view) {
        Intent intent = new Intent(this, SettingsMenuActivity.class);
        startActivity(intent);
    }
    
}
