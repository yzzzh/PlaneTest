package com.example.yzh.planetest.tool;

import com.example.yzh.planetest.model.GameObject;

/**
 * Created by YZH on 2016/11/10.
 */

public class Tools {

    public static final int TO_MAIN_VIEW = 0;
    public static final int TO_END_VIEW = 1;
    public static final int END_GAME = 2;

    public static boolean isCollide(GameObject obj_1,GameObject obj_2){
        if (obj_2.getX() > obj_1.getX() && obj_2.getX() < obj_1.getX() + obj_1.getWidth() &&
                obj_2.getY() > obj_1.getY() && obj_2.getY() < obj_1.getY() + obj_1.getHeight()){
            return true;
        }else {
            return false;
        }
    }
}
