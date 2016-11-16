package com.example.yzh.planetest.view;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.media.AudioManager;
import android.media.SoundPool;
import android.view.MotionEvent;
import android.view.SurfaceHolder;
import android.view.SurfaceView;
import android.view.View;

import com.example.yzh.planetest.R;
import com.example.yzh.planetest.activity.MainActivity;
import com.example.yzh.planetest.model.Background;
import com.example.yzh.planetest.model.BigEnemy;
import com.example.yzh.planetest.model.Bomb;
import com.example.yzh.planetest.model.Bullet;
import com.example.yzh.planetest.model.Enemy;
import com.example.yzh.planetest.model.Goods;
import com.example.yzh.planetest.model.MiddleEnemy;
import com.example.yzh.planetest.model.MyPlane;
import com.example.yzh.planetest.model.SmallEnemy;
import com.example.yzh.planetest.model.StopButton;
import com.example.yzh.planetest.model.SuperBullet;
import com.example.yzh.planetest.tool.Tools;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

/**
 * Created by YZH on 2016/11/10.
 */

public class MainView extends SurfaceView implements Runnable,SurfaceHolder.Callback,View.OnTouchListener{

    private int window_width;
    private int window_height;
    private SurfaceHolder surfaceHolder;
    private Thread thread;
    private boolean isRunning;
    private Paint paint;
    private Paint paint_text;
    private Canvas canvas;
    private Canvas tempCanvas;//二级缓存
    private Bitmap tempBitmap;
    private Bitmap bgBitmap;
    private boolean isSelected;
    private boolean isSuperBullet;
    private boolean isStopped;
    private int bomb_number;
    private Random random;

    private int small_enemy_counter;
    private int middle_enemy_counter;
    private int big_enemy_counter;
    private int bullet_counter;
    private int invincible_counter;
    private int goods_counter;
    private int super_bullet_counter;

    private Bitmap background_image;
    private List<Bitmap> myplane_image_list;
    private List<Bitmap> myplane_bomb_image_list;
    private Bitmap small_enemy_image;
    private List<Bitmap> small_enemy_bomb_image_list;
    private Bitmap middle_enemy_image;
    private List<Bitmap> middle_enemy_bomb_image_list;
    private Bitmap big_enemy_image;
    private List<Bitmap> big_enemy_bomb_image_list;
    private Bitmap bullet_image;
    private Bitmap goods_bullet_image;
    private Bitmap goods_bomb_image;
    private Bitmap super_bullet_image;
    private Bitmap bomb_image;
    private Bitmap button_stop_image;
    private Bitmap button_start_image;

    private Background background;
    private MyPlane myPlane;
    private Bomb bomb;
    private StopButton stopButton;
    private ArrayList<Enemy> enemy_list;
    private ArrayList<Bullet> bullet_list;
    private ArrayList<Goods> goods_list;
    private ArrayList<SuperBullet> super_bullet_list;

    private MainActivity mainActivity;

    //积分和关卡
    private long my_score;
    private int level;
    //声音
    private SoundPool sounds;
    private int background_music;
    private int shoot_music;
    private int enemy_bomb_music;
    private int myplane_bomb_music;
    private int upgrade_music;
    private int gameover_music;

    public MainView(Context context){
        super(context);

        surfaceHolder = this.getHolder();
        surfaceHolder.addCallback(this);

        setOnTouchListener(this);

        mainActivity = (MainActivity) context;
        paint = new Paint();
        paint_text = new Paint();
        paint_text.setColor(Color.BLACK);
        paint_text.setTextSize(40);
        tempCanvas = null;
        canvas = null;
        bgBitmap = null;

        small_enemy_counter = 0;
        middle_enemy_counter = 0;
        big_enemy_counter = 0;
        bullet_counter = 0;
        invincible_counter = 0;
        goods_counter = 0;
        isSelected = false;
        my_score = 0;
        level = 1;
        isSuperBullet = false;
        isStopped = false;
        super_bullet_counter = 0;
        bomb_number = 3;
        random = new Random();

        myplane_image_list = new ArrayList<Bitmap>();
        myplane_bomb_image_list = new ArrayList<Bitmap>();
        small_enemy_bomb_image_list = new ArrayList<Bitmap>();
        middle_enemy_bomb_image_list = new ArrayList<Bitmap>();
        big_enemy_bomb_image_list = new ArrayList<Bitmap>();

        enemy_list = new ArrayList<Enemy>();
        bullet_list= new ArrayList<Bullet>();
        goods_list = new ArrayList<Goods>();
        super_bullet_list = new ArrayList<SuperBullet>();

        sounds = new SoundPool(20, AudioManager.STREAM_SYSTEM,0);
        background_music = sounds.load(getContext(),R.raw.background_music,10);
        shoot_music = sounds.load(getContext(),R.raw.shoot,9);
        enemy_bomb_music = sounds.load(getContext(),R.raw.enemy_bomb,8);
        myplane_bomb_music = sounds.load(getContext(),R.raw.me_bomb,7);
        upgrade_music = sounds.load(getContext(),R.raw.upgrade,6);
        gameover_music = sounds.load(getContext(),R.raw.game_over,5);

        initBitmap();

    }

    @Override
    public boolean onTouch(View v, MotionEvent event) {
        if (event.getAction() == MotionEvent.ACTION_DOWN){
            float x = event.getX();
            float y = event.getY();
            if (x > myPlane.getX()
                    && x < myPlane.getX() + myPlane.getWidth()
                    && y > myPlane.getY()
                    && y < myPlane.getY() + myPlane.getHeight()){
                isSelected = true;
            }else {
                isSelected = false;
            }
            if (x > bomb.getX()
                    && x < bomb.getX() + bomb.getWidth()
                    && y > bomb.getY()
                    && y < bomb.getY() + bomb.getHeight()){
                toBomb();
            }
            if (x > stopButton.getX()
                    && x < stopButton.getX() + stopButton.getWidth()
                    && y > stopButton.getY()
                    && y < stopButton.getY() + stopButton.getHeight()){
                if (isStopped){
                    isStopped = false;
                    stopButton.setStop(false);
                    //从暂停恢复为运行
                    synchronized (thread){
                        thread.notify();
                    }

                }else {
                    isStopped = true;
                    stopButton.setStop(true);
                }
            }
        }else if (event.getAction() == MotionEvent.ACTION_MOVE){
            if (isSelected){
                myPlane.setX((int) event.getX() - myPlane.getWidth()/2);
                myPlane.setY((int) event.getY() - myPlane.getHeight()/2);
            }
        }else if (event.getAction() == MotionEvent.ACTION_UP){
            isSelected = false;
        }

        return true;
    }

    @Override
    public void surfaceCreated(SurfaceHolder holder) {
        window_height = this.getHeight();
        window_width = this.getWidth();
        tempBitmap = Bitmap.createBitmap(window_width, window_height, Bitmap.Config.ARGB_8888);
        bgBitmap = Bitmap.createBitmap(window_width, window_height, Bitmap.Config.ARGB_8888);
        background = new Background(background_image, bgBitmap);
        myPlane = new MyPlane(myplane_image_list,myplane_bomb_image_list,window_width,window_height);
        bomb = new Bomb(bomb_image,window_width,window_height);
        stopButton = new StopButton(button_stop_image,button_start_image,window_width,window_height);
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
    public void run() {
        while (isRunning){
            long startTime = System.currentTimeMillis();
            sounds.play(background_music,1,1,1,-1,1);
            draw();
            if (isStopped){
                synchronized (thread){
                    try {
                        //暂停
                        thread.wait();
                    }catch (Exception e){
                        e.printStackTrace();
                    }
                }
            }
            long endTime = System.currentTimeMillis();

            try {
                if (endTime - startTime < 10)
                    Thread.sleep(10 - (endTime - startTime));
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }
    private void draw(){
        //先移动
        for (Enemy enemy:(ArrayList<Enemy>)enemy_list.clone()){
            enemy.move();
            if (enemy.getY() >= window_height){
                enemy_list.remove(enemy);
            }
        }
        for (Bullet bullet:(ArrayList<Bullet>)bullet_list.clone()){
            bullet.move();
            if (bullet.getY() < - bullet.getHeight()){
                bullet_list.remove(bullet);
            }
        }
        for (SuperBullet superBullet:(ArrayList<SuperBullet>) super_bullet_list.clone()){
            superBullet.move();
            if (superBullet.getY() < - superBullet.getHeight()){
                super_bullet_list.remove(superBullet);
            }
        }
        //检测有没有碰撞
        for (Enemy enemy:(ArrayList<Enemy>) enemy_list.clone()){
            //与自己碰撞
            if (!enemy.getExploded() && Tools.isCollide(enemy,myPlane)){
                enemy.collide(Enemy.TYPE_MYPLANE);
                myPlane.collide();
                sounds.play(myplane_bomb_music,1,1,4,0,1);
            }
            //与子弹碰撞
            for (Bullet bullet : (ArrayList<Bullet>) bullet_list.clone()) {
                if (!enemy.getExploded() && Tools.isCollide(enemy, bullet)) {
                    enemy.collide(Enemy.TYPE_BULLET);
                    bullet.collide();
                    bullet_list.remove(bullet);
                    break;
                }
            }
            for (SuperBullet superBullet:(ArrayList<SuperBullet>) super_bullet_list.clone()){
                if (!enemy.getExploded() && Tools.isCollide(enemy, superBullet)) {
                    enemy.collide(Enemy.TYPE_BULLET);
                    superBullet.collide();
                    bullet_list.remove(superBullet);
                    break;
                }
            }

            if (!enemy.getAlive()){
                my_score += enemy.getScore();
                enemy_list.remove(enemy);
                sounds.play(enemy_bomb_music,1,1,3,0,1);
            }
        }

        //与补给碰撞
        for (Goods goods:(ArrayList<Goods>)goods_list){
            if (Tools.isCollide(myPlane,goods)){
                if (goods.getGoodsType() == Goods.TYPE_SUPERBULLET){
                    isSuperBullet = true;
                    goods_list.remove(goods);
                }else if (goods.getGoodsType() == Goods.TYPE_BOMB){
                    bomb_number++;
                    goods_list.remove(goods);
                }
            }
        }

        //自己已死
        if (!myPlane.getAlive()){
            toEndView();
        }
        //计算无敌时间
        if (myPlane.getInvincible()) {
            if (invincible_counter == 200) {
                myPlane.setInvincible(false);
                invincible_counter = 0;
            }
            invincible_counter++;
        }
        //计算超级子弹的时间
        if (isSuperBullet){
            if (super_bullet_counter == 2000){
                isSuperBullet = false;
                super_bullet_counter = 0;
            }
            super_bullet_counter++;
        }

        levelUp();

        try {
            //使用二级缓存绘制
            tempCanvas = new Canvas(tempBitmap);

            //20帧增加一个小型敌机
            if (small_enemy_counter++ == SmallEnemy.generate_speed){
                enemy_list.add(new SmallEnemy(small_enemy_bomb_image_list,small_enemy_image,window_width,window_height));
                small_enemy_counter = 0;
            }
            //40帧增加一个中型敌机
            if (middle_enemy_counter++ == MiddleEnemy.generate_speed){
                enemy_list.add(new MiddleEnemy(middle_enemy_bomb_image_list,middle_enemy_image,window_width,window_height));
                middle_enemy_counter = 0;
            }
            //60帧增加一个大型敌机
            if (big_enemy_counter++ == BigEnemy.generate_speed){
                enemy_list.add(new BigEnemy(big_enemy_bomb_image_list,big_enemy_image,window_width,window_height));
                big_enemy_counter = 0;
            }
            //1000帧增加一个补给
            if (goods_counter++ == 1000){
                boolean x = random.nextBoolean();
                if (x) {
                    goods_list.add(new Goods(goods_bullet_image, Goods.TYPE_SUPERBULLET, window_width, window_height));
                }else {
                    goods_list.add(new Goods(goods_bomb_image,Goods.TYPE_BOMB,window_width,window_height));
                }
                goods_counter = 0;
            }
            //2帧增加一个子弹
            if (isSelected) {
                if (bullet_counter++ == 5) {
                    if (!isSuperBullet) {
                        bullet_list.add(new Bullet(bullet_image, myPlane));
                    }else {
                        super_bullet_list.add(new SuperBullet(super_bullet_image,myPlane,SuperBullet.TYPE_LEFT));
                        super_bullet_list.add(new SuperBullet(super_bullet_image,myPlane,SuperBullet.TYPE_RIGHT));
                    }
                    bullet_counter = 0;
                    sounds.play(shoot_music, 1, 1, 2, 0, 1);
                }
            }

            //开始绘制
            //背景
            background.draw(tempCanvas,paint);
            //自己
            myPlane.draw(tempCanvas,paint);
            //敌机
            for (Enemy enemy:enemy_list){
                enemy.draw(tempCanvas,paint);
            }
            //子弹
            for (Bullet bullet:bullet_list){
                bullet.draw(tempCanvas,paint);
            }
            //超级子弹
            for (SuperBullet superBullet:super_bullet_list){
                superBullet.draw(tempCanvas,paint);
            }
            //补给
            for (Goods goods:goods_list){
                goods.draw(tempCanvas, paint);
            }
            //炸弹
            bomb.draw(tempCanvas,paint);
            tempCanvas.drawText(" X " + bomb_number,bomb.getX() + bomb.getWidth(),bomb.getY() + bomb.getHeight() / 2,paint_text);

            //暂停按钮
            stopButton.draw(tempCanvas,paint);

            //关卡和积分
            tempCanvas.drawText("Level: "+level,0,50,paint_text);
            tempCanvas.drawText("Score: "+my_score,0,100,paint_text);
            //生命值
            tempCanvas.drawText("Life: "+myPlane.getLife(),0,window_height - 50,paint_text);

            canvas = surfaceHolder.lockCanvas();
            canvas.drawBitmap(tempBitmap,0,0,paint);

        }catch (Exception e){
            e.printStackTrace();
        }finally {
            surfaceHolder.unlockCanvasAndPost(canvas);
        }
    }

    public void setThreadFlag(boolean flag){
        isRunning = flag;
    }

    private void initBitmap(){
        background_image = BitmapFactory.decodeResource(getResources(),R.drawable.background);

        myplane_image_list.add(BitmapFactory.decodeResource(getResources(),R.drawable.my_plane_1));
        myplane_image_list.add(BitmapFactory.decodeResource(getResources(),R.drawable.my_plane_2));
        myplane_bomb_image_list.add(BitmapFactory.decodeResource(getResources(),R.drawable.my_plane_down_1));
        myplane_bomb_image_list.add(BitmapFactory.decodeResource(getResources(),R.drawable.my_plane_down_2));
        myplane_bomb_image_list.add(BitmapFactory.decodeResource(getResources(),R.drawable.my_plane_down_3));
        myplane_bomb_image_list.add(BitmapFactory.decodeResource(getResources(),R.drawable.my_plane_down_4));

        small_enemy_image = BitmapFactory.decodeResource(getResources(),R.drawable.enemy1);
        small_enemy_bomb_image_list.add(BitmapFactory.decodeResource(getResources(),R.drawable.enemy1_down_1));
        small_enemy_bomb_image_list.add(BitmapFactory.decodeResource(getResources(),R.drawable.enemy1_down_2));
        small_enemy_bomb_image_list.add(BitmapFactory.decodeResource(getResources(),R.drawable.enemy1_down_3));
        small_enemy_bomb_image_list.add(BitmapFactory.decodeResource(getResources(),R.drawable.enemy1_down_4));

        middle_enemy_image = BitmapFactory.decodeResource(getResources(),R.drawable.enemy2);
        middle_enemy_bomb_image_list.add(BitmapFactory.decodeResource(getResources(),R.drawable.enemy2_down_1));
        middle_enemy_bomb_image_list.add(BitmapFactory.decodeResource(getResources(),R.drawable.enemy2_down_2));
        middle_enemy_bomb_image_list.add(BitmapFactory.decodeResource(getResources(),R.drawable.enemy2_down_3));
        middle_enemy_bomb_image_list.add(BitmapFactory.decodeResource(getResources(),R.drawable.enemy2_down_4));

        big_enemy_image = BitmapFactory.decodeResource(getResources(),R.drawable.enemy3);
        big_enemy_bomb_image_list.add(BitmapFactory.decodeResource(getResources(),R.drawable.enemy3_down_1));
        big_enemy_bomb_image_list.add(BitmapFactory.decodeResource(getResources(),R.drawable.enemy3_down_2));
        big_enemy_bomb_image_list.add(BitmapFactory.decodeResource(getResources(),R.drawable.enemy3_down_3));
        big_enemy_bomb_image_list.add(BitmapFactory.decodeResource(getResources(),R.drawable.enemy3_down_4));

        bullet_image = BitmapFactory.decodeResource(getResources(),R.drawable.bullet);
        goods_bullet_image = BitmapFactory.decodeResource(getResources(),R.drawable.goods_bullet);
        goods_bomb_image = BitmapFactory.decodeResource(getResources(),R.drawable.goods_bomb);
        super_bullet_image = BitmapFactory.decodeResource(getResources(),R.drawable.super_bullet);
        bomb_image = BitmapFactory.decodeResource(getResources(),R.drawable.bomb);
        button_stop_image = BitmapFactory.decodeResource(getResources(),R.drawable.button_stop);
        button_start_image = BitmapFactory.decodeResource(getResources(),R.drawable.button_start);
    }

    private void levelUp(){
        if (level == 1 && my_score >= 5000){
            level = 2;
            addEnemy(5,10,20);
            sounds.play(upgrade_music,1,1,5,0,1);
        }else if (level == 2 && my_score >= 20000){
            level = 3;
            addEnemy(5,10,20);
            sounds.play(upgrade_music,1,1,5,0,1);
        }else if (level == 3 && my_score >= 50000){
            level = 4;
            addEnemy(5,10,20);
            sounds.play(upgrade_music,1,1,5,0,1);
        }else if (level == 4 && my_score >= 100000){
            level = 5;
            addEnemy(5,10,20);
            sounds.play(upgrade_music,1,1,5,0,1);
        }
    }

    private void toEndView(){
        sounds.stop(background_music);
        sounds.play(gameover_music,1,1,6,0,1);
        mainActivity.getHandler().sendEmptyMessage(Tools.TO_END_VIEW);
    }

    private void addEnemy(int small,int middle,int big){
        SmallEnemy.generate_speed -= small;
        MiddleEnemy.generate_speed -= middle;
        BigEnemy.generate_speed -= big;
    }

    private void toBomb(){
        if (bomb_number > 0){
            bomb_number --;
            for (Enemy enemy:(ArrayList<Enemy>)enemy_list){
                enemy.collide(Enemy.TYPE_BOMB);
            }
        }
    }

}
