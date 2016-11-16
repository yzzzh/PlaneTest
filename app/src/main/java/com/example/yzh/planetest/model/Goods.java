package com.example.yzh.planetest.model;

import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Paint;

import java.util.Random;

/**
 * Created by YZH on 2016/11/15.
 */

public class Goods extends GameObject {

    private Bitmap goods_image;
    private Random random;
    private int speed_x;
    private int speed_y;
    public static final int TYPE_SUPERBULLET = 0;
    public static final int TYPE_BOMB = 1;
    private int type;

    public Goods(Bitmap goods_image,int type,int window_width,int window_height){
        this.goods_image = goods_image;
        random = new Random();
        this.width = goods_image.getWidth();
        this.height = goods_image.getHeight();
        this.isAlive = true;
        this.type = type;
        this.window_width = window_width;
        this.window_height = window_height;
        this.x = random.nextInt(window_width - this.width);
        this.y = -(random.nextInt(window_height) + this.height);
        this.speed_x = 5;
        this.speed_y = 5;
    }

    @Override
    public void draw(Canvas canvas, Paint paint) {
        move();
        canvas.drawBitmap(goods_image,x,y,paint);
    }

    private void move(){
        if ((speed_x > 0 && x + width >= window_width) || (speed_x < 0 && x <= 0)){
            speed_x = - speed_x;
        }
        if ((speed_y > 0 && y + height >= window_height) || (speed_y < 0 && y <= 0)){
            speed_y = - speed_y;
        }

        x += speed_x;
        y += speed_y;
    }

    public int getGoodsType(){
        return type;
    }
}
