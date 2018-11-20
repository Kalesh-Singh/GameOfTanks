package com.techexchange.mobileapps.tanksgame;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Rect;

import java.util.ArrayList;
import java.util.List;

public class Bricks {

    private static final int WIDTH = 150;
    private static final int HEIGHT = 150;

    private final Context context;
    private final Canvas canvas;
    private final Bitmap brickBitmap;
    private List<Rect> brickRects;

    public Bricks(Context context, Canvas canvas) {
        this.context = context;
        this.canvas = canvas;
        this.brickBitmap = BitmapFactory.decodeResource(this.context.getResources(), R.drawable.brick);
        this.brickRects = new ArrayList<>();
        Start start = createColumn(new Start(150, 0), 3);
        start = createRow(start, 3);
        // TODO: Create more brick rectangles.
    }

    public void draw() {
        for (Rect rect : brickRects) {
            canvas.drawBitmap(brickBitmap, null, rect, null);
        }
    }

    /**
     * Adds the column Rects to brickRects.
     * Returns the start for next col or row.
     * */
    private Start createColumn(Start start, int numBricks) {
        Start rectStart = start;
        for (int i = 0; i < numBricks; ++i) {
            this.brickRects.add(new BrickRect(rectStart, WIDTH, HEIGHT).getRect());
            rectStart.top += HEIGHT;
        }
        return rectStart;
    }

    /**
     * Adds the row Rects to brickRects.
     * Returns the start for the next col or row.
     */

    private Start createRow(Start start, int numBricks) {
        Start rectStart = start;
        for (int i = 0; i < numBricks; ++i) {
            this.brickRects.add(new BrickRect(rectStart, WIDTH, HEIGHT).getRect());
            rectStart.left += WIDTH;
        }
        return rectStart;
    }

}
