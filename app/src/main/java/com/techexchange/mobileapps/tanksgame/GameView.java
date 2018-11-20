package com.techexchange.mobileapps.tanksgame;

import android.content.Context;
import android.graphics.Canvas;
import android.view.View;

public class GameView extends View {

    private final Context context;
    private Bricks bricks = null;

    public GameView(Context context) {
        super(context);
        this.context = context;
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);

        if (this.bricks != null) {
            bricks.setCanvas(canvas);
            bricks.draw();
        }

        invalidate();       // Forces a redraw.
    }

    @Override
    protected void onSizeChanged(int w, int h, int oldw, int oldh) {
        super.onSizeChanged(w, h, oldw, oldh);

        bricks = new Bricks(this.context, w, h);
    }
}
