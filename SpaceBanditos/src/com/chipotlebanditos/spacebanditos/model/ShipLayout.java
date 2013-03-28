package com.chipotlebanditos.spacebanditos.model;

import android.graphics.PointF;

import com.chipotlebanditos.spacebanditos.model.systems.LifeSupportSystem;
import com.chipotlebanditos.spacebanditos.model.systems.ShipSystem;
import com.chipotlebanditos.spacebanditos.model.systems.WeaponSystem;
import com.google.common.collect.ImmutableListMultimap;
import com.google.common.collect.ListMultimap;

public enum ShipLayout {
    TEST_LAYOUT(
            -1,
            new ImmutableListMultimap.Builder<Class<? extends ShipSystem>, PointF>()
                    .put(LifeSupportSystem.class, new PointF(0f, 0f))
                    .put(WeaponSystem.class, new PointF(1f, 0f))
                    .put(WeaponSystem.class, new PointF(1f, 1f)).build());
    
    public final int imageResource;
    public final ListMultimap<Class<? extends ShipSystem>, PointF> systemPositions;
    
    private ShipLayout(int imageResource,
            ListMultimap<Class<? extends ShipSystem>, PointF> systemPositions) {
        this.imageResource = imageResource;
        this.systemPositions = systemPositions;
    }
}
