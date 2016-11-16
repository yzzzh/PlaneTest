package com.example.yzh.planetest.model;

import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Rect;

/**
 * Created by YZH on 2016/11/10.
 */

public class Background {

    private Bitmap bg;
    private Bitmap newBg;
    private int window_height;
    private int window_width;
    private int height;
    private int width;
    private int inc_h;

    public Background(Bitmap bg,Bitmap newBg){
        //bg是背景图片，newBg是二级缓存，大小跟屏幕一样，我们要将bg的大小调整为newBg的大小
        this.bg = bg;
        this.newBg = newBg;
        this.width = bg.getWidth();
        this.height = bg.getHeight();
        this.window_width = newBg.getWidth();
        this.window_height = newBg.getHeight();
        inc_h = 0;
    }

    public void draw(Canvas canvas, Paint paint){
        //二级缓存
        Canvas temp_canvas = new Canvas(newBg);
        //将传入的图片画在画布上，并进行拉伸
        temp_canvas.drawBitmap(bg,new Rect(0,0,this.width,this.height),new Rect(0,inc_h, window_width, window_height + inc_h),paint);
        temp_canvas.drawBitmap(bg,new Rect(0,0,this.width,this.height),new Rect(0,-window_height + inc_h, window_width,inc_h),paint);

        canvas.drawBitmap(newBg,0,0,paint);

        inc_h+=3;
        if (inc_h >= window_height){
            inc_h = 0;
        }
    }
}
