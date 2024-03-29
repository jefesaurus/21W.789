package com.chipotlebanditos.spacebanditos.activities;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.GestureDetector;
import android.view.GestureDetector.SimpleOnGestureListener;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Button;

import com.chipotlebanditos.spacebanditos.R;
import com.chipotlebanditos.spacebanditos.SpaceBanditosApplication;
import com.chipotlebanditos.spacebanditos.model.Ship;
import com.chipotlebanditos.spacebanditos.views.ShipSystemView;
import com.chipotlebanditos.spacebanditos.views.SystemsView;

public class EnemyShipActivity extends ShipActivity {
    
    private GestureDetector gestureDetector;
    
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        
        setContentView(new EnemyShipView(this));
        
        gestureDetector = new GestureDetector(this,
                new SimpleOnGestureListener() {
                    @Override
                    public boolean onFling(MotionEvent e1, MotionEvent e2,
                            float velocityX, float velocityY) {
                        // left to right swipe
                        if (e2.getX() - e1.getX() > SWIPE_MIN_DISTANCE
                                && Math.abs(velocityX) > SWIPE_THRESHOLD_VELOCITY) {
                            moveToPlayerShipScreen();
                        }
                        return false;
                    }
                });
    }
    
    private class EnemyShipView extends ShipActivity.ShipView {
        
        public EnemyShipView(Context context) {
            super(context);
            
            View.inflate(context, R.layout.activity_enemy_ship, this);
            
            final Ship ship = ((SpaceBanditosApplication) getApplication()).game.currentEvent.enemyShip;
            
            setShip(ship);
            SystemsView systemsView = (SystemsView) findViewById(R.id.systems);
            systemsView.setReversed(true);
            
            if (getCallingActivity() == null) {
                getPlayerShipScreenButton().setText("S\nH\nI\nP");
            } else {
                getPlayerShipScreenButton()
                        .setText("N\nO\n \nT\nA\nR\nG\nE\nT");
                for (int i = 0; i < systemsView.getChildCount(); i++) {
                    ShipSystemView view = (ShipSystemView) systemsView
                            .getChildAt(i);
                    view.setEnabled(true);
                    view.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            Intent intent = new Intent();
                            intent.putExtra(
                                    SystemManagementActivity.INTENT_EXTRA_SYSTEM_INDEX,
                                    ship.getSystemIndex(((ShipSystemView) v).system));
                            setResult(RESULT_OK, intent);
                            finish();
                            overridePendingTransition(
                                    R.anim.left_to_right_enter,
                                    R.anim.left_to_right_exit);
                        }
                    });
                }
            }
        }
        
        private Button getPlayerShipScreenButton() {
            return (Button) findViewById(R.id.player_ship_screen_button);
        }
        
        @Override
        public void onLayout(boolean changed, int l, int t, int r, int b) {
            super.onLayout(changed, l, t, r, b);
        }
    }
    
    public void onPlayerShipScreenButtonClick(View v) {
        moveToPlayerShipScreen();
    }
    
    private void moveToPlayerShipScreen() {
        if (getCallingActivity() != null) {
            final Ship ship = ((SpaceBanditosApplication) getApplication()).game.currentEvent.enemyShip;
            Intent intent = new Intent();
            intent.putExtra(SystemManagementActivity.INTENT_EXTRA_SYSTEM_INDEX,
                    ship.getSystemIndex(null));
            setResult(RESULT_OK, intent);
        }
        finish();
        overridePendingTransition(R.anim.left_to_right_enter,
                R.anim.left_to_right_exit);
    }
    
    @Override
    public void onShipDestroyed() {
        if (getCallingActivity() != null) {
            setResult(RESULT_CANCELED, null);
        }
        finish();
        overridePendingTransition(0, 0);
    }
    
    @Override
    public boolean onTouchEvent(MotionEvent event) {
        return gestureDetector.onTouchEvent(event);
    }
    
}
