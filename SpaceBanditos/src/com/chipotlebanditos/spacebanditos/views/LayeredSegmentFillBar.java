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
    
    private LevelListDrawable elementDrawable;
    
    private int baseDirection;
    
    private int elementWidth, elementHeight;
    
    private int elementSpacing;
    
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
                .getDrawable(R.styleable.LayeredSegmentFillBar_elementDrawable);
        if (d == null) {
            elementDrawable = new LevelListDrawable();
        } else if (d instanceof LevelListDrawable) {
            elementDrawable = (LevelListDrawable) d;
        } else {
            elementDrawable = new LevelListDrawable();
            elementDrawable.addLevel(Integer.MIN_VALUE, Integer.MAX_VALUE, d);
        }
        
        baseDirection = a.getInteger(
                R.styleable.LayeredSegmentFillBar_baseDirection, TOP);
        
        elementWidth = a.getDimensionPixelSize(
                R.styleable.LayeredSegmentFillBar_elementWidth, elementWidth);
        elementHeight = a.getDimensionPixelSize(
                R.styleable.LayeredSegmentFillBar_elementHeight, elementHeight);
        
        elementSpacing = a.getDimensionPixelSize(
                R.styleable.LayeredSegmentFillBar_elementSpacing, 0);
        
        a.recycle();
    }
    
    public void setLayerValue(int layer, int value) {
        if (!Integer.valueOf(value).equals(layerValues.get(-layer))) {
            invalidate();
        }
        layerValues.put(layer, value);
    }
    
    public void setElementDrawable(LevelListDrawable drawable) {
        elementDrawable = drawable;
    }
    
    public LevelListDrawable getElementDrawable() {
        return elementDrawable;
    }
    
    public void setBaseDirection(int baseDirection) {
        this.baseDirection = baseDirection;
    }
    
    public int getBaseDirection() {
        return baseDirection;
    }
    
    public int getElementWidth() {
        return elementWidth;
    }
    
    public void setElementWidth(int elementWidth) {
        this.elementWidth = elementWidth;
    }
    
    public int getElementHeight() {
        return elementHeight;
    }
    
    public void setElementHeight(int elementHeight) {
        this.elementHeight = elementHeight;
    }
    
    public void setElementSpacing(int spacing) {
        this.elementSpacing = spacing;
    }
    
    public int getElementSpacing() {
        return elementSpacing;
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
    
    private int getSizeInSegments(int width, int height) {
        if (baseDirection == LEFT || baseDirection == RIGHT) {
            if (width - getPaddingLeft() - getPaddingRight() < elementWidth) {
                return 0;
            } else {
                return (int) Math.floor((width - getPaddingLeft()
                        - getPaddingRight() - elementDrawable
                            .getIntrinsicWidth())
                        / (float) (elementWidth + elementSpacing) + 1);
            }
        } else {
            if (height - getPaddingTop() - getPaddingBottom() < elementDrawable
                    .getIntrinsicHeight()) {
                return 0;
            } else {
                return (int) Math.floor((height - getPaddingTop()
                        - getPaddingBottom() - elementDrawable
                            .getIntrinsicHeight())
                        / (float) (elementHeight + elementSpacing) + 1);
            }
        }
    }
    
    public int getSizeInSegments() {
        return getSizeInSegments(getWidth(), getHeight());
    }
    
    private int getFillWidthOfSegments(int segments) {
        return (segments == 0 ? 0 : segments * elementWidth + (segments - 1)
                * elementSpacing)
                + getPaddingLeft() + getPaddingRight();
    }
    
    private int getFillHeightOfSegments(int segments) {
        return (segments == 0 ? 0 : segments * elementHeight + (segments - 1)
                * elementSpacing)
                + getPaddingTop() + getPaddingBottom();
    }
    
    private int getFillSizeInWidthSegments(int width) {
        if (width - getPaddingLeft() - getPaddingRight() < elementDrawable
                .getIntrinsicWidth()) {
            return 0;
        } else {
            return (int) Math.floor((width - getPaddingLeft()
                    - getPaddingRight() - elementDrawable.getIntrinsicWidth())
                    / (float) (elementWidth + elementSpacing) + 1);
        }
    }
    
    private int getFillSizeInHeightSegments(int height) {
        if (height - getPaddingTop() - getPaddingBottom() < elementDrawable
                .getIntrinsicHeight()) {
            return 0;
        } else {
            return (int) Math
                    .floor((height - getPaddingTop() - getPaddingBottom() - elementDrawable
                            .getIntrinsicHeight())
                            / (float) (elementHeight + elementSpacing) + 1);
        }
    }
    
    private int getDrawWidth(int widthMeasureSpec) {
        if (baseDirection == LEFT || baseDirection == RIGHT) {
            if (this.getLayoutParams().width == LayoutParams.FILL_PARENT
                    || this.getLayoutParams().width == LayoutParams.MATCH_PARENT) {
                return getFillWidthOfSegments(getFillSizeInWidthSegments(MeasureSpec
                        .getSize(widthMeasureSpec)));
            } else {
                return getFillWidthOfSegments(getDrawSizeInSegments());
            }
        } else {
            return elementWidth + getPaddingLeft() + getPaddingRight();
        }
    }
    
    private int getDrawHeight(int heightMeasureSpec) {
        if (baseDirection == TOP || baseDirection == BOTTOM) {
            if (this.getLayoutParams().height == LayoutParams.FILL_PARENT
                    || this.getLayoutParams().height == LayoutParams.MATCH_PARENT) {
                return getFillHeightOfSegments(getFillSizeInHeightSegments(MeasureSpec
                        .getSize(heightMeasureSpec)));
            } else {
                return getFillHeightOfSegments(getDrawSizeInSegments());
            }
        } else {
            return elementHeight + getPaddingTop() + getPaddingBottom();
        }
    }
    
    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        setMeasuredDimension(getDrawWidth(widthMeasureSpec),
                getDrawHeight(heightMeasureSpec));
    }
    
    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        int x = (baseDirection == RIGHT ? getWidth() - getPaddingRight()
                : getPaddingLeft()), y = (baseDirection == BOTTOM ? getHeight()
                - getPaddingBottom() : getPaddingTop());
        for (int i = 0; i < getDrawSizeInSegments(); i++) {
            elementDrawable.setLevel(getSegmentLevel(i));
            switch (baseDirection) {
            case LEFT:
                elementDrawable.setBounds(x, y, x += elementWidth, y
                        + elementHeight);
                x += elementSpacing;
                break;
            case TOP:
                elementDrawable.setBounds(x, y, x + elementWidth,
                        y += elementHeight);
                y += elementSpacing;
                break;
            case RIGHT:
                elementDrawable.setBounds(x -= elementWidth
                        + (i > 0 ? elementSpacing : 0), y, x + elementWidth, y
                        + elementHeight);
                break;
            case BOTTOM:
                elementDrawable.setBounds(x, y -= elementHeight
                        + (i > 0 ? elementSpacing : 0), x + elementWidth, y
                        + elementHeight);
                break;
            }
            elementDrawable.draw(canvas);
        }
    }
}
