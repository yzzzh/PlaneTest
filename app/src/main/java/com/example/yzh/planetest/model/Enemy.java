package com.example.yzh.planetest.model;

import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Paint;

import java.util.Random;

/**
 * Created by YZH on 2016/11/10.
 */

public class Enemy extends GameObject {

    //血量
    protected int blood;
    //处于正在爆炸的状态，便于绘制爆炸的图片
    protected boolean isExploded;
    //用于绘画爆炸图片的计数器
    protected int bomb_counter;
    //用于控制每张爆炸图片显示的时间
    protected int index_counter;
    //移动速度
    protected int speed;
    //用于缓存
    protected Bitmap temp_bitmap;
    //产生随机值
    protected Random random;
    //画血量条
    protected Paint paint_red;
    protected Paint paint_green;
    //被谁撞
    public static final int TYPE_MYPLANE = 0;
    public static final int TYPE_BULLET = 1;
    public static final int TYPE_BOMB = 2;

    public void move(){
        y += speed;
    }

    public void collide(int type){
        if (type == TYPE_BULLET) {
            blood -= 1;
            if (blood <= 0) {
                isExploded = true;
            }
        }else if (type == TYPE_MYPLANE){
            blood = 0;
            isExploded = true;
        }else if (type == TYPE_BOMB){
            blood = 0;
            isExploded = true;
        }
    }

    public boolean getExploded(){
        return isExploded;
    }

    public boolean getAlive(){
        return isAlive;
    }

    @Override
    public void draw(Canvas canvas, Paint paint) {}

    public int getScore(){
        return 0;
    }


}
