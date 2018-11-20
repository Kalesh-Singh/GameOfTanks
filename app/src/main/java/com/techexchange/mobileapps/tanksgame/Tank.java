package com.techexchange.mobileapps.tanksgame;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Rect;

import java.util.List;

public class Tank {
    private final Context context;
    private final List<Rect> brickRects;
    private final int brickWidth;
    private final int brickHeight;
    private final int tankWidth;
    private final int tankHeight;
    private final int screenWidth;
    private final int screenHeight;
    private final Bitmap tankBitmap;
    private final int leftOffset;
    private final int topOffset;

    private Rect tankRect;

    private Canvas canvas = null;

    public Tank(Context context, List<Rect> brickRects, int brickWidth, int brickHeight, int screenWidth, int screenHeight) {
        this.context = context;
        this.brickRects = brickRects;
        this.brickWidth = brickWidth;
        this.brickHeight = brickHeight;
        this.tankWidth = Math.min(brickWidth, brickHeight) - 8;        // NOTE: Tank is square.
        this.tankHeight = Math.min(brickWidth, brickHeight) - 8;
        this.leftOffset = (brickWidth - tankWidth) / 2;
        this.topOffset = (brickHeight - tankHeight) / 2;
        this.screenWidth = screenWidth;
        this.screenHeight = screenHeight;
        this.tankBitmap = getTankBitmap();

        Start start = new Start(leftOffset + screenWidth - (4 * brickWidth), topOffset + (screenHeight - brickHeight));
        this.tankRect = new TankRect(start, tankWidth, tankHeight).getRect();

    }

    public void setCanvas(Canvas canvas) {
        this.canvas = canvas;
    }

    public void draw() {
        canvas.drawBitmap(tankBitmap, null, tankRect, null);
    }

    private Bitmap getTankBitmap() {
        Bitmap spriteSheet = BitmapFactory.decodeResource(context.getResources(), R.drawable.multicolortanks);
        int tileWidth = spriteSheet.getWidth() / 8;
        int tileHeight = spriteSheet.getHeight() / 8;
        return Bitmap.createBitmap(spriteSheet, 0, 0, tileWidth, tileHeight);
    }
}
