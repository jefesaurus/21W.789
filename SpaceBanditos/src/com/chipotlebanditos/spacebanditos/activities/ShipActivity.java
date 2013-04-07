package com.chipotlebanditos.spacebanditos.activities;

import android.app.Activity;
import android.content.Context;
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
    
    protected class ShipActivityView extends RelativeLayout {
        
        private Ship ship = null;
        
        public ShipActivityView(Context context) {
            super(context);
            
            View.inflate(context, R.layout.activity_any_ship, this);
            
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
