package com.chipotlebanditos.spacebanditos.views;

import android.app.Activity;
import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.util.AttributeSet;
import android.util.TypedValue;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnTouchListener;

import com.chipotlebanditos.spacebanditos.SpaceBanditosApplication;
import com.chipotlebanditos.spacebanditos.model.Game;
import com.chipotlebanditos.spacebanditos.model.GameEvent;

public class EventsView extends View implements OnTouchListener {
    
    private GameEvent selectedEvent = null;
    
    private Paint eventPaint = new Paint();
    {
        eventPaint.setColor(Color.GRAY);
        eventPaint.setStrokeWidth(3f);
        eventPaint.setStyle(Paint.Style.FILL);
        eventPaint.setAntiAlias(true);
    }
    
    private Paint eventPaintHighlight = new Paint();
    {
        eventPaintHighlight.setColor(Color.WHITE);
        eventPaintHighlight.setStrokeWidth(6f);
        eventPaintHighlight.setStyle(Paint.Style.FILL);
        eventPaint.setAntiAlias(true);
    }
    
    public EventsView(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }
    
    public EventsView(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        
        setOnTouchListener(this);
    }
    
    @Override
    public boolean onTouch(View v, MotionEvent event) {
        Game game = ((SpaceBanditosApplication) ((Activity) getContext())
                .getApplication()).game;
        
        synchronized (game) {
            float sizeRatio = Math.min(getWidth() / Game.EVENTS_REGION_WIDTH,
                    getHeight() / Game.EVENTS_REGION_HEIGHT);
            float touchAngle = (float) Math.atan2(event.getY()
                    - game.currentEvent.location.y * sizeRatio, event.getX()
                    - game.currentEvent.location.x * sizeRatio);
            GameEvent bestEvent = null;
            float bestAngleDiff = Float.MAX_VALUE;
            for (GameEvent e : game.events) {
                if (e == game.currentEvent) {
                    continue;
                }
                if (GameEvent.getDistanceBetween(e, game.currentEvent) > Game.PLAYER_JUMP_RADIUS) {
                    continue;
                }
                float targetAngle = (float) Math.atan2(
                        (e.location.y - game.currentEvent.location.y),
                        (e.location.x - game.currentEvent.location.x));
                
                float angleDiff = (float) Math.abs(touchAngle - targetAngle);
                angleDiff = (float) (angleDiff > Math.PI ? 2 * Math.PI
                        - angleDiff : angleDiff);
                if (bestEvent == null || angleDiff < bestAngleDiff) {
                    bestEvent = e;
                    bestAngleDiff = angleDiff;
                }
            }
            if (bestEvent != selectedEvent) {
                invalidate();
            }
            selectedEvent = bestEvent;
        }
        return true;
    }
    
    public GameEvent getSelectedEvent() {
        return selectedEvent;
    }
    
    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        float sizeRatio = Math.min(getWidth() / Game.EVENTS_REGION_WIDTH,
                getHeight() / Game.EVENTS_REGION_HEIGHT);
        Game game = ((SpaceBanditosApplication) ((Activity) getContext())
                .getApplication()).game;
        for (GameEvent event : game.events) {
            float x = event.location.x * sizeRatio, y = event.location.y
                    * sizeRatio;
            if (GameEvent.getDistanceBetween(event, game.currentEvent) < Game.PLAYER_JUMP_RADIUS) {
                canvas.drawLine(game.currentEvent.location.x * sizeRatio,
                        game.currentEvent.location.y * sizeRatio, x, y,
                        event == selectedEvent ? eventPaintHighlight
                                : eventPaint);
            }
            canvas.drawCircle(x, y, TypedValue.applyDimension(
                    TypedValue.COMPLEX_UNIT_DIP, 10f, getResources()
                            .getDisplayMetrics()),
                    event == game.currentEvent ? eventPaintHighlight
                            : eventPaint);
            // TODO: label exits (and stores?), mark visited vs. unvisited
        }
    }
}