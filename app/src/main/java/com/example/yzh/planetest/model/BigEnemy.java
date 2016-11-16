package com.example.yzh.planetest.model;

import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;

import java.util.List;
import java.util.Random;

/**
 * Created by YZH on 2016/11/10.
 */

public class BigEnemy extends Enemy {
    //爆炸图片由四张图片组成
    private List<Bitmap> big_enemy_bomb_list;
    //正常飞行的图片
    private Bitmap big_enemy_image;
    private final int score = 2000;
    //产生敌机的速度
    public static int generate_speed = 200;
    //总的血量
    public static final int total_blood = 10;
    //当前血量占总的血量
    private float blood_percent;

    public BigEnemy(List<Bitmap> big_enemy_bomb_list, Bitmap big_enemy_image,int window_width,int window_height){
        this.big_enemy_bomb_list = big_enemy_bomb_list;
        this.big_enemy_image = big_enemy_image;
        this.window_width = window_width;
        this.window_height = window_height;
        random = new Random();
        width = big_enemy_image.getWidth();
        height = big_enemy_image.getHeight();
        x = random.nextInt(window_width - width);
        y = -(random.nextInt(window_height) + height);
        blood = total_blood;
        blood_percent = 0;
        bomb_counter = 0;
        index_counter = 0;
        speed = 1;
        isAlive = true;
        isExploded = false;
        temp_bitmap = null;
        paint_red = new Paint();
        paint_green = new Paint();
        paint_red.setColor(Color.RED);
        paint_green.setColor(Color.GREEN);
    }

    @Override
    public void draw(Canvas canvas, Paint paint) {
        blood_percent = (float)blood / (float)total_blood;
        if (!isExploded) {
            canvas.drawBitmap(big_enemy_image,x,y,paint);
            canvas.drawLine(x,y,x + width,y,paint_red);
            canvas.drawLine(x,y,x + width * blood_percent,y,paint_green);
        }else {
            temp_bitmap = big_enemy_bomb_list.get(bomb_counter);
            if (index_counter == 2) {
                bomb_counter++;
                if (bomb_counter == big_enemy_bomb_list.size()) {
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
