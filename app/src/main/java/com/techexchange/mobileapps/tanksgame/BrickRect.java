package com.techexchange.mobileapps.tanksgame;

import android.graphics.Rect;

public class BrickRect {
    private final Rect rect;

    public BrickRect(Start start, int width, int height) {
        this.rect = new Rect(start.left, start.top,
                start.left + width, start.top + height);
    }

    public Rect getRect() {
        return this.rect;
    }
}
