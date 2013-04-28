package com.chipotlebanditos.spacebanditos.views;

import android.content.Context;
import android.graphics.Canvas;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.TextView;

import com.chipotlebanditos.spacebanditos.R;
import com.chipotlebanditos.spacebanditos.model.systems.ShipSystem;
import com.chipotlebanditos.spacebanditos.model.systems.WeaponSystem;

public abstract class AbsShipSystemView extends FrameLayout {
    
    public final ShipSystem system;
    
    public AbsShipSystemView(ShipSystem system, Context context) {
        super(context);
        this.system = system;
        this.setWillNotDraw(false);
    }
    
    protected TextView getSystemNameView() {
        return (TextView) findViewById(R.id.system_name);
    }
    
    protected ImageView getSystemIconView() {
        return (ImageView) findViewById(R.id.system_icon);
    }
    
    protected LayeredSegmentFillBar getPowerBar() {
        return (LayeredSegmentFillBar) findViewById(R.id.power_bar);
    }
    
    protected LayeredSegmentFillBar getWeaponChargeBar() {
        return (LayeredSegmentFillBar) findViewById(R.id.weapon_charge_bar);
    }
    
    protected void onDrawPowerBar(Canvas canvas) {
        getPowerBar().setLayerValue(0, system.upgrades.getMaxUpgradeLevel());
        getPowerBar().setLayerValue(1, system.upgradeLevel);
        getPowerBar()
                .setLayerValue(2, system.upgradeLevel - system.damageLevel);
        getPowerBar().setLayerValue(3, system.powerLevel);
    }
    
    @Override
    protected void onDraw(Canvas canvas) {
        getSystemNameView().setText(system.getName());
        getSystemIconView().setImageResource(system.getIconResource());
        onDrawPowerBar(canvas);
        if (getWeaponChargeBar() != null) {
            if (system instanceof WeaponSystem) {
                getWeaponChargeBar().setVisibility(VISIBLE);
                getWeaponChargeBar().setLayerValue(0,
                        getWeaponChargeBar().getSizeInSegments());
                getWeaponChargeBar()
                        .setLayerValue(
                                1,
                                (int) Math
                                        .floor(getWeaponChargeBar()
                                                .getSizeInSegments()
                                                * ((WeaponSystem) system).chargeMillisFraction));
            } else {
                getWeaponChargeBar().setVisibility(GONE);
            }
        }
        super.onDraw(canvas);
    }
}
