package com.techexchange.mobileapps.tanksgame;

import android.content.Context;
import android.graphics.Canvas;
import android.support.v4.view.GestureDetectorCompat;
import android.util.Log;
import android.view.GestureDetector;
import android.view.MotionEvent;
import android.view.View;

public class BattlegroundView extends View implements GestureDetector.OnGestureListener {

    static final String TAG = "Game";

    private static final long DELAY_MS = 30;

    private final Context context;
    private final GestureDetectorCompat detector;

    private Maze maze;
    private Tank greenTank;     // TODO: Delete these
    private Tank redTank;

    public BattlegroundView(Context context) {
        super(context);

        this.context = context;
        this.detector = new GestureDetectorCompat(context, this);

        this.maze = null;
        this.greenTank = null;
        this.redTank = null;
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);

        if (this.maze != null) {
            maze.draw(canvas);
            greenTank.draw(canvas);
            redTank.draw(canvas);
        }

        try {
            Thread.sleep(DELAY_MS);
        } catch (InterruptedException e) {
            Log.e(TAG, "Sleep interrupted!", e);
        }

        invalidate();       // Forces a redraw.
    }

    @Override
    protected void onSizeChanged(int w, int h, int oldw, int oldh) {
        super.onSizeChanged(w, h, oldw, oldh);

        maze = new Maze(this.context, w, h);
        greenTank = new Tank(this.context, Tank.Color.GREEN, w, h);
        redTank = new Tank(this.context, Tank.Color.RED, w, h);
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        return this.detector.onTouchEvent(event) || super.onTouchEvent(event);
    }

    @Override
    public boolean onDown(MotionEvent e) {
        return true;
    }

    @Override
    public void onShowPress(MotionEvent e) {

    }

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
        // TODO: Shoot shell
    }

    @Override
    public boolean onFling(MotionEvent e1, MotionEvent e2, float velocityX, float velocityY) {
        if (greenTank != null) {
            if (Math.abs(velocityX) >= Math.abs(velocityY)) {
                if (velocityX < 0) {
                    Log.d(TAG, "Left swipe");
                    greenTank.handleLeft(maze.getBricks(), redTank);
                } else {
                    Log.d(TAG, "Right swipe");
                    greenTank.handleRight(maze.getBricks(), redTank);
                }
            } else {
                if (velocityY < 0) {
                    Log.d(TAG, "Up swipe");
                    greenTank.handleUp(maze.getBricks(), redTank);
                } else {
                    Log.d(TAG, "Down swipe");
                    greenTank.handleDown(maze.getBricks(), redTank);
                }
            }
        }
        return true;
    }
}
