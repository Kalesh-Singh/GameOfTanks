package com.techexchange.mobileapps.tanksgame;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Rect;

import java.util.ArrayList;
import java.util.List;

public class Bricks {

    private static final int WIDTH = 200;
    private static final int HEIGHT = 200;

    private final Context context;
    private Canvas canvas = null;

    private final int screenWidth;
    private final int screenHeight;

    private final Bitmap brickBitmap;
    private final List<Rect> brickRects;

    public Bricks(Context context, int screenWidth, int screenHeight) {
        this.context = context;
        this.screenWidth = screenWidth;
        this.screenHeight = screenHeight;
        this.brickBitmap = BitmapFactory.decodeResource(this.context.getResources(), R.drawable.brick);
        this.brickRects = new ArrayList<>();
        createMaze();
    }

    public void setCanvas(Canvas canvas) {
        this.canvas = canvas;
    }

    public void draw() {
        for (Rect rect : brickRects) {
            canvas.drawBitmap(brickBitmap, null, rect, null);
        }
    }

    private void createMaze() {
        Start start = createColumn(new Start(150, 0), 3);
        createRow(start, 3);

        createRow(new Start(screenWidth - (3 * WIDTH), 0), 2);

        start = createRow(new Start(screenWidth - (6 * WIDTH), screenHeight - 3 * HEIGHT), 2);
        start.top -= 2 * HEIGHT;
        start = createColumn(start, 3);
        start.left += WIDTH;
        start.top -= HEIGHT;
        createColumn(start, 3);
    }

    /**
     * Adds the column Rects to brickRects.
     * Returns the start for next col or row.
     * */
    private Start createColumn(Start start, int numBricks) {
        for (int i = 0; i < numBricks; ++i) {
            this.brickRects.add(new BrickRect(start, WIDTH, HEIGHT).getRect());
            start.top += HEIGHT;
        }
        return start;
    }

    /**
     * Adds the row Rects to brickRects.
     * Returns the start for the next col or row.
     */

    private Start createRow(Start start, int numBricks) {
        for (int i = 0; i < numBricks; ++i) {
            this.brickRects.add(new BrickRect(start, WIDTH, HEIGHT).getRect());
            start.left += WIDTH;
        }
        return start;
    }

}
