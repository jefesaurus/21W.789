package com.chipotlebanditos.spacebanditos.activities;

import android.app.Activity;
import android.content.Context;
import android.view.View;
import android.widget.Button;

import com.chipotlebanditos.spacebanditos.R;
import com.chipotlebanditos.spacebanditos.SpaceBanditosApplication;
import com.chipotlebanditos.spacebanditos.model.Game;
import com.chipotlebanditos.spacebanditos.views.ShipView;

public abstract class ShipActivity extends Activity {
    
    protected class ShipActivityView extends ShipView {
        
        public ShipActivityView(Context context) {
            super(context);
            
            View.inflate(context, R.layout.activity_ship, this);
            
        }
        
        @Override
        public void onLayout(boolean changed, int l, int t, int r, int b) {
            Game game = ((SpaceBanditosApplication) getApplication()).game;
            ((Button) findViewById(R.id.pause_button))
                    .setText(game.paused ? "UNPAUSE" : "PAUSE");
            super.onLayout(changed, l, t, r, b);
        }
        
    }
    
    public void onPauseButtonClick(View v) {
        Game game = ((SpaceBanditosApplication) getApplication()).game;
        game.paused = !game.paused;
        ((Button) v).setText(game.paused ? "UNPAUSE" : "PAUSE");
    }
    
    public abstract void onShipDestroyed();
}
