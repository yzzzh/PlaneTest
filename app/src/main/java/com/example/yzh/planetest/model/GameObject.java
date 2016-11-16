package com.example.yzh.planetest.model;

import android.graphics.Canvas;
import android.graphics.Paint;
import android.view.SurfaceView;

/**
 * Created by YZH on 2016/11/10.
 */

abstract public class GameObject{

    protected int x;
    protected int y;
    protected int height;
    protected int width;
    protected int window_height;
    protected int window_width;
    protected boolean isAlive;


    public abstract void draw(Canvas canvas, Paint paint);

    public void setX(int x) {
        this.x = x;
    }

    public void setY(int y) {
        this.y = y;
    }

    public void setHeight(int height) {
        this.height = height;
    }

    public void setWidth(int width) {
        this.width = width;
    }

    public void setWindow_height(int window_height) {
        this.window_height = window_height;
    }

    public void setWindow_width(int window_width) {
        this.window_width = window_width;
    }

    public void setAlive(boolean alive) {
        isAlive = alive;
    }



    public int getX() {
        return x;
    }

    public int getY() {
        return y;
    }

    public int getHeight() {
        return height;
    }

    public int getWidth() {
        return width;
    }

    public int getWindow_height() {
        return window_height;
    }

    public int getWindow_width() {
        return window_width;
    }

    public boolean isAlive() {
        return isAlive;
    }
}
