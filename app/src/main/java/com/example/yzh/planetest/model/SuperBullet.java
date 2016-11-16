package com.example.yzh.planetest.model;

import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Paint;

/**
 * Created by YZH on 2016/11/16.
 */

public class SuperBullet extends GameObject {

    private Bitmap super_bullet_image;
    public static final int TYPE_LEFT = 0;
    public static final int TYPE_RIGHT = 1;
    private int speed;

    public SuperBullet(Bitmap super_bullet_image,MyPlane myPlane,int type){
        isAlive = true;
        speed = 10;
        this.super_bullet_image = super_bullet_image;
        width = super_bullet_image.getWidth();
        height = super_bullet_image.getHeight();
        if (type == TYPE_LEFT) {
            x = myPlane.getX() + 30;
            y = myPlane.getY() + 60;
        }else if (type == TYPE_RIGHT) {
            x = myPlane.getX() + myPlane.getWidth() - 35;
            y = myPlane.getY() + 60;
        }
    }

    @Override
    public void draw(Canvas canvas, Paint paint) {
        if (isAlive){
            canvas.drawBitmap(super_bullet_image,x,y,paint);
        }
    }

    public void move(){
        y -= speed;
    }

    public void collide(){
        isAlive = false;
    }
}
