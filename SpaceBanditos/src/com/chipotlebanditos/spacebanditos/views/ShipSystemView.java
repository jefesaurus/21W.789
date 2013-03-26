package com.chipotlebanditos.spacebanditos.views;

import android.content.Context;
import android.graphics.Canvas;
import android.view.View;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.chipotlebanditos.spacebanditos.R;
import com.chipotlebanditos.spacebanditos.model.systems.ShipSystem;

public class ShipSystemView extends RelativeLayout {
    
    public final ShipSystem system;
    
    private final TextView systemName;
    private final LayeredSegmentFillBar powerBar;
    
    public ShipSystemView(ShipSystem system, Context context) {
        super(context);
        this.system = system;
        this.setWillNotDraw(false);
        View.inflate(context, R.layout.ship_system_view, this);
        systemName = (TextView) findViewById(R.id.system_name);
        powerBar = (LayeredSegmentFillBar) findViewById(R.id.power_bar);
        this.setBackgroundDrawable(getResources().getDrawable(
                R.drawable.system_view_background));
    }
    
    @Override
    protected void onDraw(Canvas canvas) {
        systemName.setText(system.getName());
        powerBar.setLayerValue(0, 5);
        powerBar.setLayerValue(1, system.upgradeLevel);
        powerBar.setLayerValue(2, system.powerLevel);
        super.onDraw(canvas);
    }
}
