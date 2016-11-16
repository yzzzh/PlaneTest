package com.example.yzh.planetest.model;

import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.telephony.SmsManager;

import java.util.List;
import java.util.Random;

/**
 * Created by YZH on 2016/11/10.
 */

public class SmallEnemy extends Enemy{
    //爆炸图片由四张图片组成
    private List<Bitmap> small_enemy_bomb_list;
    //正常飞行的图片
    private Bitmap small_enemy_image;
    //积分
    private final int score = 300;
    //产生敌机的速度
    public static int generate_speed = 50;
    //总的血量
    public static final int total_blood = 1;
    //当前血量占总的血量
    private float blood_percent;

    public SmallEnemy(List<Bitmap> small_enemy_bomb_list, Bitmap small_enemy_image,int window_width,int window_height){
        this.small_enemy_bomb_list = small_enemy_bomb_list;
        this.small_enemy_image = small_enemy_image;
        this.window_width = window_width;
        this.window_height = window_height;
        random = new Random();
        width = small_enemy_image.getWidth();
        height = small_enemy_image.getHeight();
        x = random.nextInt(window_width - width);
        y = -(random.nextInt(window_height) + height);
        blood = total_blood;
        bomb_counter = 0;
        index_counter = 0;
        speed = 3;
        isAlive = true;
        isExploded = false;
        temp_bitmap = null;
        blood_percent = 0.0f;
        paint_red = new Paint();
        paint_green = new Paint();
        paint_red.setColor(Color.RED);
        paint_green.setColor(Color.GREEN);
    }

    @Override
    public void draw(Canvas canvas, Paint paint) {
        blood_percent = (float)blood / (float)total_blood;
        if (!isExploded) {
            canvas.drawBitmap(small_enemy_image,x,y,paint);
            canvas.drawLine(x,y,x + width,y,paint_red);
            canvas.drawLine(x,y,x + width * blood_percent,y,paint_green);
        }else{
            temp_bitmap = small_enemy_bomb_list.get(bomb_counter);
            if (index_counter == 2) {
                bomb_counter++;
                if (bomb_counter == small_enemy_bomb_list.size()) {
                    bomb_counter = 0;
                    isAlive = false;
                }
                index_counter = 0;
            }
            index_counter++;
            canvas.drawBitmap(temp_bitmap,x,y,paint);
        }
    }

    @Override
    public int getScore(){
        return this.score;
    }


}
