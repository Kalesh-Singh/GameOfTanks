package com.techexchange.mobileapps.tanksgame;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Rect;

import java.util.ArrayList;
import java.util.List;

public class Bricks {

    private final int brickWidth;
    private final int brickHeight;

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
        this.brickWidth = screenWidth / 8;
        this.brickHeight = screenHeight / 12;
        this.brickBitmap = BitmapFactory.decodeResource(this.context.getResources(), R.drawable.brick);
        this.brickRects = new ArrayList<>();
        createMaze();
    }

    public List<Rect> getBrickRects() {
        return this.brickRects;
    }

    public int getBrickWidth() {
        return this.brickWidth;
    }

    public int getBrickHeight() {
        return this.brickHeight;
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
        createRow(new Start(screenWidth - (3 * brickWidth), 0), 2);
        start = createRow(new Start(screenWidth - (6 * brickWidth),
                screenHeight - 3 * brickHeight), 2);
        start.top -= brickHeight * 2;
        start = createColumn(start, 3);
        start.left += brickWidth;
        start.top -= brickHeight;
        createColumn(start, 3);
    }

    /**
     * Adds the column Rects to brickRects.
     * Returns the start for next col or row.
     * */
    private Start createColumn(Start start, int numBricks) {
        for (int i = 0; i < numBricks; ++i) {
            this.brickRects.add(new BrickRect(start, brickWidth, brickHeight).getRect());
            start.top += brickHeight;
        }
        return start;
    }

    /**
     * Adds the row Rects to brickRects.
     * Returns the start for the next col or row.
     * */
    private Start createRow(Start start, int numBricks) {
        for (int i = 0; i < numBricks; ++i) {
            this.brickRects.add(new BrickRect(start, brickWidth, brickHeight).getRect());
            start.left += brickWidth;
        }
        return start;
    }

}
