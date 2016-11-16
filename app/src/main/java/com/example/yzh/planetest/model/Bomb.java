package com.example.yzh.planetest.model;

import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Paint;

/**
 * Created by YZH on 2016/11/16.
 */

public class Bomb extends GameObject {

    private Bitmap bomb_image;

    public Bomb(Bitmap bomb_image,int window_width,int window_height){
        this.isAlive = true;
        this.bomb_image = bomb_image;
        this.width = bomb_image.getWidth();
        this.height = bomb_image.getHeight();
        this.window_height = window_height;
        this.window_width = window_width;
        x = 0;
        y = window_height - this.height - 150;
    }


    @Override
    public void draw(Canvas canvas, Paint paint) {
        canvas.drawBitmap(bomb_image,x,y,paint);
    }
}
