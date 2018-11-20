package com.techexchange.mobileapps.tanksgame;

import android.content.Context;
import android.graphics.Canvas;
import android.view.View;

public class GameView extends View {

    private final Context context;

    public GameView(Context context) {
        super(context);
        this.context = context;
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);

        Bricks bricks = new Bricks(this.context, canvas);
        bricks.draw();

        invalidate();       // Forces a redraw.
    }
}
