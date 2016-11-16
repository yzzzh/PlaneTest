package com.example.yzh.planetest.model;

import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Paint;

import java.util.List;

/**
 * Created by YZH on 2016/11/10.
 */

public class MyPlane extends GameObject{

    private List<Bitmap> myplane_image_list;
    private Bitmap myplane_image_1;
    private Bitmap myplane_image_2;
    private List<Bitmap> myplane_bomb_image_list;

    //是否处于爆炸状态
    private boolean isExploded;
    //三次机会
    private int life;
    //控制图片的显示时间
    private int index_counter;
    //绘画爆炸图片的计数器
    private int bomb_counter;
    //二级缓存
    private Bitmap temp_bitmap;
    //无敌状态
    private boolean isInvincible;

    public MyPlane(List<Bitmap> myplane_image_list,List<Bitmap>myplane_bomb_image_list,int window_width,int window_height){
        this.myplane_image_list = myplane_image_list;
        this.myplane_bomb_image_list = myplane_bomb_image_list;
        this.window_width = window_width;
        this.window_height = window_height;
        myplane_image_1 = myplane_image_list.get(0);
        myplane_image_2 = myplane_image_list.get(1);
        width = myplane_image_2.getWidth();
        height = myplane_image_2.getHeight();
        x = (window_width - width) / 2;
        y = window_height - height - 10;
        isAlive = true;
        life = 3;
        index_counter = 0;
        bomb_counter = 0;
        temp_bitmap = null;
        isInvincible = false;
        isExploded = false;
    }

    @Override
    public void draw(Canvas canvas, Paint paint) {
        if (!isInvincible) {
            if (!isExploded) {
                if (index_counter < 10){
                    canvas.drawBitmap(myplane_image_1,x,y,paint);
                } else {
                    canvas.drawBitmap(myplane_image_2,x,y,paint);
                    if (index_counter >= 20){
                        index_counter = 0;
                    }
                }
                index_counter++;
            } else {
                temp_bitmap = myplane_bomb_image_list.get(bomb_counter);
                if (index_counter >= 2) {
                    bomb_counter++;
                    if (bomb_counter == myplane_bomb_image_list.size()) {
                        bomb_counter = 0;
                        //爆炸完后复活
                        reset();
                    }
                    index_counter = 0;
                }
                index_counter++;
                canvas.drawBitmap(temp_bitmap, x, y, paint);
            }
        }else {
            //无敌状态会闪烁
            if (index_counter == 2 || index_counter == 3){
                canvas.drawBitmap(myplane_image_list.get(0),x,y,paint);
                if (index_counter >= 3){
                    index_counter = 0;
                }
            }
            index_counter++;
        }
    }
    //非无敌状态会减少一次生命
    public void collide(){
        if (!isInvincible) {
            life--;
            isExploded = true;
            if (life <= 0) {
                isAlive = false;
            }
        }
    }
    //复活
    private void reset(){
        isInvincible = true;
        isExploded = false;
        x = (window_width - width) / 2;
        y = window_height - height - 10;
    }

    public void setInvincible(boolean isInvincible){
        this.isInvincible = isInvincible;
    }

    public boolean getInvincible(){
        return isInvincible;
    }

    public boolean getAlive(){
        return isAlive;
    }

    public int getLife(){
        return life;
    }

}
