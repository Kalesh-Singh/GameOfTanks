package com.techexchange.mobileapps.tanksgame;

import android.content.Context;
import android.graphics.Canvas;
import android.support.v4.view.GestureDetectorCompat;
import android.util.Log;
import android.view.GestureDetector;
import android.view.MotionEvent;
import android.view.View;

public class GameView extends View implements GestureDetector.OnGestureListener {

    private final Context context;
    private GestureDetectorCompat detector;
    private Bricks bricks = null;
    private Tanks tanks = null;

    public GameView(Context context) {
        super(context);
        this.context = context;
        this.setClickable(true);
        this.setLongClickable(true);
        // Instantiate the gesture detector with the
        // application context and an implementation of
        // GestureDetector.OnGestureListener
        detector = new GestureDetectorCompat(context, this);

    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        if (this.detector.onTouchEvent(event)) {
            return true;
        }
        return super.onTouchEvent(event);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);

        if (this.bricks != null && this.tanks != null) {
            bricks.setCanvas(canvas);
            bricks.draw();
            tanks.setCanvas(canvas);
            tanks.draw();
        }
        invalidate();       // Forces a redraw.
    }


    @Override
    protected void onSizeChanged(int w, int h, int oldw, int oldh) {
        super.onSizeChanged(w, h, oldw, oldh);

        bricks = new Bricks(this.context, w, h);
        tanks = new Tanks(context, bricks.getBrickRects(), bricks.getBrickWidth(), bricks.getBrickHeight(), w, h);
    }

    @Override
    public boolean onDown(MotionEvent e) {
        return true;
    }

    @Override
    public void onShowPress(MotionEvent e) { return; }

    @Override
    public boolean onSingleTapUp(MotionEvent e) {
        return false;
    }

    @Override
    public boolean onScroll(MotionEvent e1, MotionEvent e2, float distanceX, float distanceY) {
        return false;
    }

    @Override
    public void onLongPress(MotionEvent e) {
        Log.d(Tanks.TAG, "LongPress");
        Log.d(Tanks.TAG, "SHELL fired!");
        // TODO: Shoot firebomb

    }

    @Override
    public boolean onFling(MotionEvent e1, MotionEvent e2, float velocityX, float velocityY) {
        if (Math.abs(velocityX) >= Math.abs(velocityY)) {
            if (velocityX < 0) {
                Log.d(Tanks.TAG, "Left swipe");
                tanks.handleGreenLeft();
            } else {
                Log.d(Tanks.TAG, "Right swipe");
                tanks.handleGreenRight();
            }
        } else {
            if (velocityY < 0) {
                Log.d(Tanks.TAG, "Up swipe");
                tanks.handleGreenUp();
            } else {
                Log.d(Tanks.TAG, "Down swipe");
                tanks.handleGreenDown();
            }
        }
        return true;
    }
}
