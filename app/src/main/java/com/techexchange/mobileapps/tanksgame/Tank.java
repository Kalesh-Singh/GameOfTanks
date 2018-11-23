package com.techexchange.mobileapps.tanksgame;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Matrix;
import android.graphics.Rect;

public class Tank {

    static class Bitmaps {
        Bitmap up;
        Bitmap down;
        Bitmap left;
        Bitmap right;

        Bitmaps(Bitmap up, Bitmap down, Bitmap left, Bitmap right) {
            this.up = up;
            this.down = down;
            this.left = left;
            this.right = right;
        }
    }

    enum State {
        IN_MOTION, STATIONARY
    }

    enum Direction {
        UP, DOWN, LEFT, RIGHT
    }

    enum Color {
        GREEN, RED
    }

    // Required Parameters
    private final Context context;
    private final Color color;
    private final int screenWidth;
    private final int screenHeight;

    private final Bitmaps bitmaps;
    private final int brickWidth;
    private final int brickHeight;
    private final int tankWidth;
    private final int tankHeight;
    private final int leftOffset;       // Offset of the tank's left from bricks
    private final int topOffset;        // Offset of the tank's top from bricks
    private final int xSpeed;
    private final int ySpeed;

    private State state;
    private Direction direction;
    private Bitmap bitmap;
    private Rect rect;
    private int destinationLeft;
    private int destinationTop;


    public Tank(Context context, Color color, int screenWidth, int screenHeight) {
        this.context = context;
        this.color = color;
        this.screenWidth = screenWidth;
        this.screenHeight = screenHeight;

        this.bitmaps = getBitmaps();
        this.brickWidth = screenWidth / 8;
        this.brickHeight = screenHeight / 12;
        this.tankWidth = Math.min(brickWidth, brickHeight) - 8;        // NOTE: Tank is square.
        this.tankHeight = Math.min(brickWidth, brickHeight) - 8;
        this.leftOffset = (brickWidth - tankWidth) / 2;
        this.topOffset = (brickHeight - tankHeight) / 2;
        this.ySpeed = tankHeight * 2;
        this.xSpeed = tankWidth * 2;

        this.state = State.STATIONARY;
        this.direction = (this.color == Color.GREEN) ? Direction.UP : Direction.DOWN;
        this.bitmap = (this.color == Color.GREEN) ? bitmaps.up : bitmaps.down;
        this.rect = getStartRect();
        this.destinationLeft = rect.left;
        this.destinationTop = rect.top;
    }

    private Bitmap getRightBitmap() {
        Bitmap spriteSheet = BitmapFactory.decodeResource(context.getResources(),
                R.drawable.multicolortanks);
        int tileWidth = spriteSheet.getWidth() / 8;
        int tileHeight = spriteSheet.getHeight() / 8;
        if (color == Color.GREEN) {
            return Bitmap.createBitmap(spriteSheet, 0, 0, tileWidth, tileHeight);
        } else if (color == Color.RED) {
            return Bitmap.createBitmap(spriteSheet, 0, tileHeight, tileWidth, tileHeight);
        }
        return null;
    }

    private Bitmap rotateClockwise90Deg(Bitmap bitmap) {
        Matrix matrix = new Matrix();
        matrix.postRotate(90);
        return Bitmap.createBitmap(bitmap, 0, 0, bitmap.getWidth(),
                bitmap.getHeight(), matrix, true);
    }

    private Bitmaps getBitmaps() {
        Bitmap right = getRightBitmap();
        Bitmap down  = rotateClockwise90Deg(right);
        Bitmap left  = rotateClockwise90Deg(down);
        Bitmap up  = rotateClockwise90Deg(left);
        return new Bitmaps(up, down, left, right);
    }

    private Rect getStartRect() {
        int left = leftOffset + screenWidth - (4 * brickWidth);
        int right = left + tankWidth;
        int top = 0;
        if (color == Color.GREEN) {
            top = topOffset + (screenHeight - brickHeight);
        } else if (color == Color.RED) {
            top = topOffset;
        }
        int bottom = top + tankHeight;
        return new Rect(left, top, right, bottom);
    }
}
