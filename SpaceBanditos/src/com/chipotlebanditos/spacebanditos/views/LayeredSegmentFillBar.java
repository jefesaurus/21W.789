package com.chipotlebanditos.spacebanditos.views;

import java.util.HashMap;
import java.util.Map;
import java.util.Map.Entry;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.drawable.Drawable;
import android.graphics.drawable.LevelListDrawable;
import android.util.AttributeSet;
import android.view.View;

import com.chipotlebanditos.spacebanditos.R;

public class LayeredSegmentFillBar extends View {
    
    private LevelListDrawable segmentDrawable;
    
    private int baseDirection;
    
    private Map<Integer, Integer> layerValues = new HashMap<Integer, Integer>();
    
    public LayeredSegmentFillBar(Context context) {
        super(context);
    }
    
    public static final int LEFT = 0, TOP = 1, RIGHT = 2, BOTTOM = 3;
    
    public LayeredSegmentFillBar(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }
    
    public LayeredSegmentFillBar(Context context, AttributeSet attrs,
            int defStyle) {
        super(context, attrs, defStyle);
        
        TypedArray a = context.obtainStyledAttributes(attrs,
                R.styleable.LayeredSegmentFillBar);
        
        Drawable d = a
                .getDrawable(R.styleable.LayeredSegmentFillBar_segmentDrawable);
        if (d == null) {
            segmentDrawable = new LevelListDrawable();
        } else if (d instanceof LevelListDrawable) {
            segmentDrawable = (LevelListDrawable) d;
        } else {
            segmentDrawable = new LevelListDrawable();
            segmentDrawable.addLevel(Integer.MIN_VALUE, Integer.MAX_VALUE, d);
        }
        
        baseDirection = a.getInteger(
                R.styleable.LayeredSegmentFillBar_baseDirection, TOP);
        
        a.recycle();
        
        setLayerValue(0, 3);
        setLayerValue(1, 2);
        setLayerValue(2, 1);
    }
    
    public void setLayerValue(int layer, int value) {
        if (!Integer.valueOf(value).equals(layerValues.get(-layer))) {
            invalidate();
        }
        layerValues.put(layer, value);
    }
    
    public void setSegmentDrawable(LevelListDrawable drawable) {
        segmentDrawable = drawable;
    }
    
    public LevelListDrawable getSegmentDrawable() {
        return segmentDrawable;
    }
    
    private int getSegmentLevel(int index) {
        int level = -1;
        for (Entry<Integer, Integer> e : layerValues.entrySet()) {
            if (e.getValue() > index && e.getKey() > level) {
                level = e.getKey();
            }
        }
        return level;
    }
    
    private int getSizeInSegments() {
        int size = 0;
        for (int v : layerValues.values()) {
            size = Math.max(size, v);
        }
        return size;
    }
    
    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        int totalWidth = 0, totalHeight = 0;
        for (int i = 0; i < getSizeInSegments(); i++) {
            segmentDrawable.setLevel(getSegmentLevel(i));
            if (baseDirection == LEFT || baseDirection == RIGHT) {
                totalWidth += segmentDrawable.getIntrinsicWidth();
                totalHeight = Math.max(totalHeight,
                        segmentDrawable.getIntrinsicHeight());
            } else {
                totalWidth = Math.max(totalWidth,
                        segmentDrawable.getIntrinsicWidth());
                totalHeight += segmentDrawable.getIntrinsicHeight();
            }
        }
        setMeasuredDimension(totalWidth, totalHeight);
    }
    
    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        int x = 0, y = 0;
        for (int i = 0; i < getSizeInSegments(); i++) {
            segmentDrawable.setLevel(getSegmentLevel(i));
            switch (baseDirection) {
            case LEFT:
                segmentDrawable.setBounds(x, y,
                        x += segmentDrawable.getIntrinsicWidth(), y
                                + segmentDrawable.getIntrinsicHeight());
                break;
            case TOP:
                segmentDrawable.setBounds(x, y,
                        x + segmentDrawable.getIntrinsicWidth(),
                        y += segmentDrawable.getIntrinsicHeight());
                break;
            case RIGHT:
                segmentDrawable
                        .setBounds((x -= segmentDrawable.getIntrinsicWidth())
                                + getWidth(), y,
                                x + segmentDrawable.getIntrinsicWidth()
                                        + getWidth(),
                                y + segmentDrawable.getIntrinsicHeight());
                break;
            case BOTTOM:
                segmentDrawable.setBounds(x,
                        (y -= segmentDrawable.getIntrinsicHeight())
                                + getHeight(),
                        x + segmentDrawable.getIntrinsicWidth(), y
                                + segmentDrawable.getIntrinsicHeight()
                                + getHeight());
                break;
            }
            segmentDrawable.draw(canvas);
        }
    }
}
