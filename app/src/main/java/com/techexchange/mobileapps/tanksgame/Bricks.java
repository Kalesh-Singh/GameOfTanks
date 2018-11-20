package com.techexchange.mobileapps.tanksgame;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Rect;

import java.util.ArrayList;
import java.util.List;

public class Bricks {

    private static final int WIDTH = 100;
    private static final int HEIGHT = 100;

    private final Context context;
    private final Canvas canvas;
    private final Bitmap brickBitmap;
    private List<Rect> brickRectangles;

    public Bricks(Context context, Canvas canvas) {
        this.context = context;
        this.canvas = canvas;
        this.brickBitmap = BitmapFactory.decodeResource(this.context.getResources(), R.drawable.brick);
        this.brickRectangles = new ArrayList<>();
        this.brickRectangles.add(new BrickRect(new Start(0, 0), WIDTH, HEIGHT).getRect());
        // TODO: Create more brick rectangles.
    }

    public void draw() {
        for (Rect rect : brickRectangles) {
            canvas.drawBitmap(brickBitmap, null, rect, null);
        }
    }

}
