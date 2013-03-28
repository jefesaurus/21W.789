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
import android.widget.LinearLayout.LayoutParams;

import com.chipotlebanditos.spacebanditos.R;

public class LayeredSegmentFillBar extends View {
    
    private LevelListDrawable segmentDrawable;
    
    private int baseDirection;
    
    private int spacing;
    
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
        
        spacing = a.getDimensionPixelSize(
                R.styleable.LayeredSegmentFillBar_spacing, 0);
        
        a.recycle();
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
    
    public void setBaseDirection(int baseDirection) {
        this.baseDirection = baseDirection;
    }
    
    public int getBaseDirection() {
        return baseDirection;
    }
    
    public void setSpacing(int spacing) {
        this.spacing = spacing;
    }
    
    public int getSpacing() {
        return spacing;
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
    
    private int getDrawSizeInSegments() {
        int size = 0;
        for (int v : layerValues.values()) {
            size = Math.max(size, v);
        }
        return size;
    }
    
    public int getSizeInSegments(int segmentLevel) {
        segmentDrawable.setLevel(segmentLevel);
        if (baseDirection == LEFT || baseDirection == RIGHT) {
            if (getWidth() - getPaddingLeft() - getPaddingRight() < segmentDrawable
                    .getIntrinsicWidth()) {
                return 0;
            } else {
                return (int) Math
                        .floor((getWidth() - getPaddingLeft()
                                - getPaddingRight() - segmentDrawable
                                    .getIntrinsicWidth())
                                / (float) (segmentDrawable.getIntrinsicWidth() + spacing)
                                + 1);
            }
        } else {
            if (getHeight() - getPaddingTop() - getPaddingBottom() < segmentDrawable
                    .getIntrinsicHeight()) {
                return 0;
            } else {
                return (int) Math
                        .floor((getHeight() - getPaddingTop()
                                - getPaddingBottom() - segmentDrawable
                                    .getIntrinsicHeight())
                                / (float) (segmentDrawable.getIntrinsicHeight() + spacing)
                                + 1);
            }
        }
    }
    
    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        int totalWidth = 0, totalHeight = 0;
        for (int i = 0; i < getDrawSizeInSegments(); i++) {
            segmentDrawable.setLevel(getSegmentLevel(i));
            if (baseDirection == LEFT || baseDirection == RIGHT) {
                totalWidth += segmentDrawable.getIntrinsicWidth()
                        + (i > 0 ? spacing : 0);
                totalHeight = Math.max(totalHeight,
                        segmentDrawable.getIntrinsicHeight());
            } else {
                totalWidth = Math.max(totalWidth,
                        segmentDrawable.getIntrinsicWidth());
                totalHeight += segmentDrawable.getIntrinsicHeight()
                        + (i > 0 ? spacing : 0);
            }
        }
        totalWidth += getPaddingLeft() + getPaddingRight();
        totalHeight += getPaddingTop() + getPaddingBottom();
        
        if (this.getLayoutParams().width == LayoutParams.FILL_PARENT
                || this.getLayoutParams().width == LayoutParams.MATCH_PARENT) {
            totalWidth = MeasureSpec.getSize(widthMeasureSpec);
        }
        
        if (this.getLayoutParams().height == LayoutParams.FILL_PARENT
                || this.getLayoutParams().height == LayoutParams.MATCH_PARENT) {
            totalHeight = MeasureSpec.getSize(heightMeasureSpec);
        }
        
        setMeasuredDimension(totalWidth, totalHeight);
    }
    
    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        int x = (baseDirection == RIGHT ? getWidth() - getPaddingRight()
                : getPaddingLeft()), y = (baseDirection == BOTTOM ? getHeight()
                - getPaddingBottom() : getPaddingTop());
        for (int i = 0; i < getDrawSizeInSegments(); i++) {
            segmentDrawable.setLevel(getSegmentLevel(i));
            switch (baseDirection) {
            case LEFT:
                segmentDrawable.setBounds(x, y,
                        x += segmentDrawable.getIntrinsicWidth(), y
                                + segmentDrawable.getIntrinsicHeight());
                x += i > 0 ? spacing : 0;
                break;
            case TOP:
                segmentDrawable.setBounds(x, y,
                        x + segmentDrawable.getIntrinsicWidth(),
                        y += segmentDrawable.getIntrinsicHeight());
                y += i > 0 ? spacing : 0;
                break;
            case RIGHT:
                segmentDrawable.setBounds(
                        x -= segmentDrawable.getIntrinsicWidth()
                                + (i > 0 ? spacing : 0), y,
                        x + segmentDrawable.getIntrinsicWidth(), y
                                + segmentDrawable.getIntrinsicHeight());
                break;
            case BOTTOM:
                segmentDrawable.setBounds(x,
                        y -= segmentDrawable.getIntrinsicHeight()
                                + (i > 0 ? spacing : 0),
                        x + segmentDrawable.getIntrinsicWidth(), y
                                + segmentDrawable.getIntrinsicHeight());
                break;
            }
            segmentDrawable.draw(canvas);
        }
    }
}