package com.techexchange.mobileapps.tanksgame;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Matrix;
import android.graphics.Rect;
import android.util.Log;

import java.util.List;

public class Tanks {

    enum Direction {
        UP, DOWN, LEFT, RIGHT
    }

    static final String TAG = "Tanks";

    private static final long DELAY_MS = 30;
    private static final float TIME_STEP = DELAY_MS / 1000.f;

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

    private float ySpeed;
    private float xSpeed;

    private Direction greenDirection;

    private int greenDestTop;
    private int greenDestLeft;

    public Tanks(Context context, List<Rect> brickRects, int brickWidth,
                 int brickHeight, int screenWidth, int screenHeight) {
        this.context = context;
        this.brickRects = brickRects;
        this.brickWidth = brickWidth;
        this.brickHeight = brickHeight;
        this.tankWidth = Math.min(brickWidth, brickHeight) - 8;        // NOTE: Tank is square.
        this.tankHeight = Math.min(brickWidth, brickHeight) - 8;
        this.leftOffset = (brickWidth - tankWidth) / 2;
        this.topOffset = (brickHeight - tankHeight) / 2;
        this.ySpeed = tankHeight / 5.f;
        this.xSpeed = tankWidth / 5.f;
        this.screenWidth = screenWidth;
        this.screenHeight = screenHeight;
        this.greenTankBitmaps = getGreenTankBitmaps();
        this.redTankBitmaps = getRedTankBitmaps();
        this.greenTank = greenTankBitmaps.UP;
        this.redTank = redTankBitmaps.DOWN;
        this.greenTankRect = getGreenStartRect();
        this.redTankRect = getRedStartRect();
        this.greenDirection = Direction.UP;
        this.greenDestTop = greenTankRect.top;
        this.greenDestLeft = greenTankRect.left;

    }

    public void updateGreenTankPosition() {
        if (greenInUpwardMotion()) {
            moveGreenUp();
        } else if (greenInDownwardMotion()) {
            moveGreenDown();
        } else if (greenInLeftwardMotion()) {
            moveGreenLeft();
        } else if (greenInRightwardMotion()) {
            moveGreenRight();
        }
    }

    public boolean greenInMotion() {
        return greenTankRect.top != greenDestTop || greenTankRect.left != greenDestLeft;
    }

    private boolean greenInUpwardMotion() {
        return (greenDirection == Direction.UP)
                && (greenTankRect.top != greenDestTop);
    }

    private boolean greenInDownwardMotion() {
        return (greenDirection == Direction.DOWN)
                && (greenTankRect.top != greenDestTop);
    }

    private boolean greenInLeftwardMotion() {
        return (greenDirection == Direction.LEFT)
                && (greenTankRect.left != greenDestLeft);
    }

    private boolean greenInRightwardMotion() {
        return (greenDirection == Direction.RIGHT)
                && (greenTankRect.left != greenDestLeft);
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
        greenDirection = Direction.UP;
        greenDestTop -= brickHeight;
        moveGreenUp();
    }

    public void handleGreenDown() {
        greenTank = greenTankBitmaps.DOWN;
        greenDirection = Direction.DOWN;
        greenDestTop += brickHeight;
        moveGreenDown();
    }

    public void handleGreenRight() {
        greenTank = greenTankBitmaps.RIGHT;
        greenDirection = Direction.UP;
        greenDestLeft += brickWidth;
        moveGreenRight();
    }

    public void handleGreenLeft() {
        greenTank = greenTankBitmaps.LEFT;
        greenDirection = Direction.LEFT;
        greenDestLeft -= brickWidth;
        moveGreenLeft();
    }

    private void moveGreenUp() {
        greenTankRect.top -= ySpeed * TIME_STEP;
        greenTankRect.bottom -= ySpeed * TIME_STEP;
        if (greenTankRect.top < greenDestTop) {
            int depth = greenDestTop - greenTankRect.top;
            greenTankRect.top += depth;
            greenTankRect.bottom += depth;
        }
    }

    private void moveGreenDown() {
        greenTankRect.top += ySpeed * TIME_STEP;
        greenTankRect.bottom += ySpeed * TIME_STEP;
        if (greenTankRect.top > greenDestTop) {
            int depth = greenTankRect.top - greenDestTop;
            greenTankRect.top -= depth;
            greenTankRect.bottom -= depth;
        }
    }

    private void moveGreenRight() {
        greenTankRect.left += ySpeed * TIME_STEP;
        greenTankRect.right += ySpeed * TIME_STEP;
        if (greenTankRect.left > greenDestLeft) {
            int depth = greenTankRect.left - greenDestLeft;
            greenTankRect.left -= depth;
            greenTankRect.right -= depth;
        }
    }

    private void moveGreenLeft() {
        greenTankRect.left -= ySpeed * TIME_STEP;
        greenTankRect.right -= ySpeed * TIME_STEP;
        if (greenTankRect.left < greenDestLeft) {
            int depth = greenDestLeft - greenTankRect.left;
            greenTankRect.left += depth;
            greenTankRect.right += depth;
        }
    }

}