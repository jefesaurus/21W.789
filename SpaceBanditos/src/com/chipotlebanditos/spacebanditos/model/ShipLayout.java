package com.chipotlebanditos.spacebanditos.model;

import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Matrix;
import android.graphics.PointF;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;

import com.chipotlebanditos.spacebanditos.R;
import com.chipotlebanditos.spacebanditos.model.systems.LifeSupportSystem;
import com.chipotlebanditos.spacebanditos.model.systems.ShieldsSystem;
import com.chipotlebanditos.spacebanditos.model.systems.ShipSystem;
import com.chipotlebanditos.spacebanditos.model.systems.WeaponSystem;
import com.google.common.collect.ImmutableListMultimap;
import com.google.common.collect.ListMultimap;

public enum ShipLayout {
    TEST_LAYOUT(
            R.drawable.player_ship,
            new ImmutableListMultimap.Builder<Class<? extends ShipSystem>, PointF>()
                    .put(LifeSupportSystem.class, new PointF(.4f, .2f))
                    .put(WeaponSystem.class, new PointF(.7f, .3f))
                    .put(WeaponSystem.class, new PointF(.7f, .7f))
                    .put(ShieldsSystem.class, new PointF(.4f, .8f)).build());
    
    private final int imageResource;
    private Drawable imageDrawable = null, imageDrawableReverse = null;
    public final ListMultimap<Class<? extends ShipSystem>, PointF> systemPositions;
    
    private ShipLayout(int imageResource,
            ListMultimap<Class<? extends ShipSystem>, PointF> systemPositions) {
        this.imageResource = imageResource;
        this.systemPositions = systemPositions;
    }
    
    public Drawable getImageDrawable(Resources resources) {
        if (imageDrawable == null) {
            imageDrawable = resources.getDrawable(imageResource);
        }
        return imageDrawable;
    }
    
    public Drawable getImageDrawableReverse(Resources resources) {
        if (imageDrawableReverse == null) {
            Bitmap bIn = ((BitmapDrawable) getImageDrawable(resources))
                    .getBitmap();
            Bitmap bOut = Bitmap.createBitmap(bIn.getWidth(), bIn.getHeight(),
                    bIn.getConfig());
            Canvas c = new Canvas(bOut);
            Matrix m = new Matrix();
            m.postRotate(180, c.getWidth() / 2, c.getHeight() / 2);
            c.drawBitmap(bIn, m, null);
            imageDrawableReverse = new BitmapDrawable(resources, bOut);
        }
        return imageDrawableReverse;
    }
}
