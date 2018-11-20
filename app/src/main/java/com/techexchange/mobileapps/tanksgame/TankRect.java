package com.techexchange.mobileapps.tanksgame;

import android.graphics.Rect;

public class TankRect {
    private final Rect rect;

    public TankRect(Start start, int width, int height) {
        this.rect = new Rect(start.left, start.top,
                start.left + width, start.top + height);
    }

    public Rect getRect() {
        return this.rect;
    }
}

