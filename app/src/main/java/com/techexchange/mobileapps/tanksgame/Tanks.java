package com.techexchange.mobileapps.tanksgame;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Matrix;
import android.graphics.Rect;

import java.util.List;

public class Tanks {
    static final String TAG = "Tanks";

    private final Context context;
    private final List<Rect> brickRects;
    private final int brickWidth;
    private final int brickHeight;
    private final int tankWidth;
    private final int tankHeight;
    private final int screenWidth;
    private final int screenHeight;
    private final TankBitmap greenTankBitmaps;
    private final TankBitmap redTankBitmaps;
    private final int leftOffset;
    private final int topOffset;

    private Rect greenTankRect;
    private Rect redTankRect;
    private Bitmap greenTank;
    private Bitmap redTank;

    private Canvas canvas = null;

    public Tanks(Context context, List<Rect> brickRects, int brickWidth, int brickHeight, int screenWidth, int screenHeight) {
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
        this.greenTankBitmaps = getGreenTankBitmaps();
        this.redTankBitmaps = getRedTankBitmaps();
        this.greenTank = greenTankBitmaps.UP;
        this.redTank = redTankBitmaps.DOWN;
        this.greenTankRect = getGreenStartRect();
        this.redTankRect = getRedStartRect();

    }

    private Rect getGreenStartRect() {
        Start start = new Start(leftOffset + screenWidth - (4 * brickWidth), topOffset + (screenHeight - brickHeight));
        return new TankRect(start, tankWidth, tankHeight).getRect();
    }

    private Rect getRedStartRect() {
        Start start = new Start(leftOffset + screenWidth - (4 * brickWidth), topOffset);
        return new TankRect(start, tankWidth, tankHeight).getRect();
    }

    public void setCanvas(Canvas canvas) {
        this.canvas = canvas;
    }

    public void draw() {
        canvas.drawBitmap(greenTank, null, greenTankRect, null);
        canvas.drawBitmap(redTank, null, redTankRect, null);
    }

    private Bitmap getGreenTankRightBitmap() {
        Bitmap spriteSheet = BitmapFactory.decodeResource(context.getResources(), R.drawable.multicolortanks);
        int tileWidth = spriteSheet.getWidth() / 8;
        int tileHeight = spriteSheet.getHeight() / 8;
        return Bitmap.createBitmap(spriteSheet, 0, 0, tileWidth, tileHeight);
    }

    private Bitmap getRedTankRightBitmap() {
        Bitmap spriteSheet = BitmapFactory.decodeResource(context.getResources(), R.drawable.multicolortanks);
        int tileWidth = spriteSheet.getWidth() / 8;
        int tileHeight = spriteSheet.getHeight() / 8;
        return Bitmap.createBitmap(spriteSheet, 0, tileHeight, tileWidth, tileHeight);
    }

    private Bitmap rotateBitmapClockwise90Degrees(Bitmap bitmap) {
        Matrix matrix = new Matrix();
        matrix.postRotate(90);
        return Bitmap.createBitmap(bitmap, 0, 0, bitmap.getWidth(), bitmap.getHeight(), matrix, true);
    }

    private TankBitmap getRedTankBitmaps() {
        TankBitmap redTankBitmaps = new TankBitmap();
        redTankBitmaps.RIGHT = getRedTankRightBitmap();
        redTankBitmaps.DOWN = rotateBitmapClockwise90Degrees(redTankBitmaps.RIGHT);
        redTankBitmaps.LEFT = rotateBitmapClockwise90Degrees(redTankBitmaps.DOWN);
        redTankBitmaps.UP = rotateBitmapClockwise90Degrees(redTankBitmaps.LEFT);
        return redTankBitmaps;
    }

    private TankBitmap getGreenTankBitmaps() {
        TankBitmap greenTankBitmaps = new TankBitmap();
        greenTankBitmaps.RIGHT = getGreenTankRightBitmap();
        greenTankBitmaps.DOWN = rotateBitmapClockwise90Degrees(greenTankBitmaps.RIGHT);
        greenTankBitmaps.LEFT = rotateBitmapClockwise90Degrees(greenTankBitmaps.DOWN);
        greenTankBitmaps.UP = rotateBitmapClockwise90Degrees(greenTankBitmaps.LEFT);
        return greenTankBitmaps;
    }

    public void handleGreenUp() {
        greenTank = greenTankBitmaps.UP;
//        greenTankRect.top -= brickHeight;
    }

    public void handleGreenDown() {
        greenTank = greenTankBitmaps.DOWN;
//        greenTankRect.top -= brickHeight;
    }

    public void handleGreenRight() {
        // TODO:
        greenTank = greenTankBitmaps.RIGHT;
//        greenTankRect.left += brickWidth;
    }

    public void handleGreenLeft() {
        // TODO:
        greenTank = greenTankBitmaps.LEFT;
//        greenTankRect.left -= brickWidth;
    }

}
