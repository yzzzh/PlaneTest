package com.example.yzh.planetest.view;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Rect;
import android.view.MotionEvent;
import android.view.SurfaceHolder;
import android.view.SurfaceView;
import android.view.View;

import com.example.yzh.planetest.R;
import com.example.yzh.planetest.activity.MainActivity;
import com.example.yzh.planetest.model.GameObject;
import com.example.yzh.planetest.tool.Tools;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by YZH on 2016/11/13.
 */


public class EndView extends SurfaceView implements Runnable,SurfaceHolder.Callback,View.OnTouchListener{

    private int window_width;
    private int window_height;
    private SurfaceHolder surfaceHolder;
    private Thread thread;
    private boolean isRunning;
    private Paint paint;
    private Canvas canvas;
    private Canvas tempCanvas;//二级缓存
    private Bitmap tempBitmap;
    private GameStart game_start;
    private GameEnd game_end;

    private Bitmap background_image;
    private Bitmap start_game_image;
    private Bitmap end_game_image;
    private int background_width;
    private int background_height;

    private MainActivity mainActivity;

    public EndView(Context context){
        super(context);

        surfaceHolder = this.getHolder();
        surfaceHolder.addCallback(this);

        setOnTouchListener(this);

        mainActivity = (MainActivity) context;
        thread = null;
        isRunning = false;
        paint = new Paint();
        canvas = null;
        tempCanvas = null;
        tempBitmap = null;

        initBitmap();

        game_start = new GameStart(start_game_image);
        game_end = new GameEnd(end_game_image);



    }

    @Override
    public void surfaceCreated(SurfaceHolder holder) {
        window_height = this.getHeight();
        window_width = this.getWidth();
        tempBitmap = Bitmap.createBitmap(window_width, window_height, Bitmap.Config.ARGB_8888);

        game_start.setX((window_width - game_start.getWidth())/2);
        game_start.setY((window_height - game_start.getHeight())/3);

        game_end.setX(game_start.getX());
        game_end.setY(game_start.getY() + game_start.getHeight() + 10);

        isRunning = true;
        thread = new Thread(this);
        thread.start();
    }

    @Override
    public void surfaceChanged(SurfaceHolder holder, int format, int width, int height) {

    }

    @Override
    public void surfaceDestroyed(SurfaceHolder holder) {
        isRunning = false;
    }

    @Override
    public boolean onTouch(View v, MotionEvent event) {
        if (event.getAction() == MotionEvent.ACTION_DOWN){
            if (event.getX() > game_start.getX()
                    && event.getX() < game_start.getX() + game_start.getWidth()
                    && event.getY() > game_start.getY()
                    && event.getY() < game_start.getY() + game_start.getHeight()) {
                mainActivity.getHandler().sendEmptyMessage(Tools.TO_MAIN_VIEW);
            }else if (event.getX() > game_end.getX()
                    && event.getX() < game_end.getX() + game_end.getWidth()
                    && event.getY() > game_end.getY()
                    && event.getY() < game_end.getY() + game_end.getHeight()) {
                mainActivity.getHandler().sendEmptyMessage(Tools.END_GAME);
            }
        }

        return true;
    }

    @Override
    public void run() {
        while (isRunning){
            long startTime = System.currentTimeMillis();
            draw();
            long endTime = System.currentTimeMillis();

            try {
                if (endTime - startTime < 100)
                    Thread.sleep(100 - (endTime - startTime));
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    private void draw(){

        try {
            tempCanvas = new Canvas(tempBitmap);

            tempCanvas.drawBitmap(background_image,new Rect(0,0,background_width,background_height),new Rect(0,0,window_width,window_height),paint);
            game_start.draw(tempCanvas,paint);
            game_end.draw(tempCanvas,paint);

            canvas = surfaceHolder.lockCanvas();
            canvas.drawBitmap(tempBitmap,0,0,paint);
        }catch (Exception e){
            e.printStackTrace();
        }finally {
            surfaceHolder.unlockCanvasAndPost(canvas);
        }
    }

    private void initBitmap() {
        background_image = BitmapFactory.decodeResource(getResources(), R.drawable.background);
        background_width = background_image.getWidth();
        background_height = background_image.getHeight();
        start_game_image = BitmapFactory.decodeResource(getResources(), R.drawable.start_game);
        end_game_image = BitmapFactory.decodeResource(getResources(), R.drawable.end_game);
    }

    public void setThreadFlag(boolean flag){
        isRunning = flag;
    }


    private class GameStart extends GameObject{
        Bitmap game_start_image;

        public GameStart(Bitmap game_start_image){
            this.game_start_image = game_start_image;
            this.height = game_start_image.getHeight();
            this.width = game_start_image.getWidth();
            this.x = 0;
            this.y = 0;
        }

        @Override
        public void draw(Canvas canvas, Paint paint) {
            canvas.drawBitmap(game_start_image,x,y,paint);
        }
    }

    private class GameEnd extends GameObject{
        Bitmap game_end_image;

        public GameEnd(Bitmap game_end_image){
            this.game_end_image = game_end_image;
            this.height = game_end_image.getHeight();
            this.width = game_end_image.getWidth();
            this.x = 0;
            this.y = 0;
        }

        @Override
        public void draw(Canvas canvas, Paint paint) {
            canvas.drawBitmap(game_end_image,x,y,paint);
        }
    }
}