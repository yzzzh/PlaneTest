package com.example.yzh.planetest.view;

import android.content.Context;
import android.view.SurfaceHolder;
import android.view.SurfaceView;
import android.view.View;

/**
 * Created by YZH on 2016/11/13.
 */

public class BaseView extends SurfaceView implements Runnable,SurfaceHolder.Callback,View.OnClickListener {
    public BaseView(Context context) {
        super(context);
    }

    @Override
    public void surfaceCreated(SurfaceHolder holder) {

    }

    @Override
    public void surfaceChanged(SurfaceHolder holder, int format, int width, int height) {

    }

    @Override
    public void surfaceDestroyed(SurfaceHolder holder) {

    }

    @Override
    public void onClick(View v) {

    }

    @Override
    public void run() {

    }
}
