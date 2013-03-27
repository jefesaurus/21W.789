package com.chipotlebanditos.spacebanditos.views;

import android.content.Context;
import android.graphics.Canvas;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.chipotlebanditos.spacebanditos.R;
import com.chipotlebanditos.spacebanditos.model.systems.ShipSystem;
import com.chipotlebanditos.spacebanditos.model.systems.WeaponSystem;

public class ShipSystemView extends LinearLayout {
    
    public final ShipSystem system;
    
    private final TextView systemName;
    private final ImageView systemIcon;
    private final LayeredSegmentFillBar powerBar;
    private final LayeredSegmentFillBar weaponChargeBar;
    
    public ShipSystemView(ShipSystem system, Context context) {
        super(context);
        this.system = system;
        this.setWillNotDraw(false);
        View.inflate(context, R.layout.ship_system_view, this);
        systemName = (TextView) findViewById(R.id.system_name);
        systemIcon = (ImageView) findViewById(R.id.system_icon);
        powerBar = (LayeredSegmentFillBar) findViewById(R.id.power_bar);
        weaponChargeBar = (LayeredSegmentFillBar) findViewById(R.id.weapon_charge_bar);
        this.setBackgroundDrawable(getResources().getDrawable(
                R.drawable.ship_system_view_background));
        if (system instanceof WeaponSystem) {
            weaponChargeBar.setVisibility(VISIBLE);
        }
    }
    
    @Override
    protected void onDraw(Canvas canvas) {
        systemName.setText(system.getName());
        systemIcon.setImageResource(system.getIconResource());
        powerBar.setLayerValue(0, 5);
        powerBar.setLayerValue(1, system.upgradeLevel);
        powerBar.setLayerValue(2, system.powerLevel);
        if (system instanceof WeaponSystem) {
            weaponChargeBar.setLayerValue(0,
                    weaponChargeBar.getSizeInSegments(0));
            weaponChargeBar.setLayerValue(
                    1,
                    (int) Math.floor(weaponChargeBar.getSizeInSegments(0)
                            * ((WeaponSystem) system).getChargeFraction()));
        }
        super.onDraw(canvas);
    }
}
