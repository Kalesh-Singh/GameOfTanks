package com.techexchange.mobileapps.tanksgame;

import android.content.Context;
import android.graphics.Canvas;
import android.util.Log;
import android.view.View;

public class BattlegroundView extends View {

    static final String TAG = "Game";

    private static final long DELAY_MS = 30;

    private final Context context;
    private Bricks bricks;
    private Tank greenTank;     // TODO: Delete these
    private Tank redTank;

    public BattlegroundView(Context context) {
        super(context);
        this.context = context;
        this.bricks = null;
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);

        if (this.bricks != null) {
            bricks.draw(canvas);
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

        bricks = new Bricks(this.context, w, h);
        greenTank = new Tank(this.context, Tank.Color.GREEN, w, h);
        redTank = new Tank(this.context, Tank.Color.RED, w, h);
    }
}
