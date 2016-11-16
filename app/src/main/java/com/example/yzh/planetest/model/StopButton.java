package com.example.yzh.planetest.model;

import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Paint;

/**
 * Created by YZH on 2016/11/16.
 */

public class StopButton extends GameObject {

    private Bitmap button_stop_image;
    private Bitmap button_start_image;
    private boolean isStop;

    public StopButton(Bitmap button_stop_image,Bitmap button_start_image,int window_width,int window_height){
        this.button_stop_image = button_stop_image;
        this.button_start_image = button_start_image;
        this.window_width = window_width;
        this.window_height = window_height;
        this.width = button_stop_image.getWidth();
        this.height = button_stop_image.getHeight();
        this.x = window_width - this.width - 20;
        this.y = 50;
        this.isStop = false;
    }

    @Override
    public void draw(Canvas canvas, Paint paint) {
        if (isStop){
            canvas.drawBitmap(button_stop_image,x,y,paint);
        }else {
            canvas.drawBitmap(button_start_image,x,y,paint);
        }
    }

    public void setStop(boolean isStop){
        this.isStop = isStop;
    }
}
