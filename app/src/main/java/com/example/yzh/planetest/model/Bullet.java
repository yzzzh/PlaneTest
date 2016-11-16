package com.example.yzh.planetest.model;

import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Paint;

/**
 * Created by YZH on 2016/11/10.
 */

public class Bullet extends GameObject{

    private Bitmap bullet;
    private int speed;

    public Bullet(Bitmap bullet,MyPlane myPlane){
        this.bullet = bullet;
        width = bullet.getWidth();
        height = bullet.getHeight();
        x = myPlane.getX() + myPlane.getWidth() / 2 - 7;
        y = myPlane.getY() - 10;
        speed = 10;
        isAlive = true;
    }

    @Override
    public void draw(Canvas canvas, Paint paint) {
        if (isAlive){
            canvas.drawBitmap(bullet,x,y,paint);
        }
    }

    public void move(){
        y -= speed;
    }

    public void collide(){
        isAlive = false;
    }
}
