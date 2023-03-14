package com.example.spacefighter;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Rect;
import android.media.MediaPlayer;
import android.view.MotionEvent;
import android.view.SurfaceHolder;
import android.view.SurfaceView;

import java.util.ArrayList;

public class GameView extends SurfaceView implements Runnable {

    volatile boolean playing;

    private  Thread gameThread = null;

    Player player;

    Paint paint;
    Canvas canvas;
    SurfaceHolder surfaceHolder;

    ArrayList<Star> stars = new ArrayList<Star>();

    Enemy enemies;

    int enemyCount = 1;

    Boom boom;

    Friend friend;

    int screenX;

    int countMisses;

    boolean flag;

    boolean isGameOver;

    int score;
    int highScore[] = new int[4];

    SharedPreferences sharedPreferences;

    static MediaPlayer gameOnSound;
    static MediaPlayer killedEnemySound;
    static MediaPlayer gameOverSound;

    Context context;

    public GameView(Context context, int screenX, int screenY) {
        super(context);

        player = new Player(context, screenX, screenY );

        surfaceHolder = getHolder();
        paint =new Paint();

        int starNums = 120;
        for(int i = 0; i < starNums; i++){
            Star s = new Star(screenX, screenY);
            stars.add(s);
        }

//        enemies = new Enemy[enemyCount];
//        for(int i = 0; i < enemyCount; i++){
//            enemies[i] = new Enemy(context, screenX, screenY);
//        }

        enemies = new Enemy(context, screenX, screenY);

        boom = new Boom(context);

        friend = new Friend(context, screenX, screenY);

        this.screenX = screenX;
        countMisses = 0;
        isGameOver = false;

        score = 0;
        sharedPreferences = context.getSharedPreferences("SPACE_FIGHTER", Context.MODE_PRIVATE);
        highScore[0] = sharedPreferences.getInt("score1", 0);
        highScore[1] = sharedPreferences.getInt("score2", 0);
        highScore[2] = sharedPreferences.getInt("score3", 0);
        highScore[3] = sharedPreferences.getInt("score4", 0);

        gameOnSound = MediaPlayer.create(context, R.raw.gameon);
        killedEnemySound = MediaPlayer.create(context, R.raw.killedenemy);
        gameOverSound = MediaPlayer.create(context, R.raw.gameover);

        gameOnSound.start();

        this.context = context;

    }

    @Override
    public void run() {
        while (playing){
            update();

            draw();

            control();

        }
    }

    private void control() {
        try{
            gameThread.sleep(20);
        }catch (InterruptedException e){
            e.printStackTrace();
        }
    }

    private void update() {

        score++;

        player.update();

        boom.setX(-250);
        boom.setY(-250);

        for(Star s : stars){
            s.update(player.getSpeed());
        }

//        for(int i=0; i<enemyCount; i++){
//            enemies[i].update(player.getSpeed());
//
//            if (Rect.intersects(player.getDetectCollision(), enemies[i].getDetectCollision())){
//
//                boom.setX(enemies[i].getX());
//                boom.setY(enemies[i].getY());
//
//                enemies[i].serX(-200);
//            }
//
//        }

        if(enemies.getX()==screenX){
            flag =true;
        }

        enemies.update((player.getSpeed()));

        if(Rect.intersects(player.getDetectCollision(), enemies.getDetectCollision())){
            boom.setX(enemies.getX());
            boom.setY(enemies.getY());

            killedEnemySound.start();

            enemies.serX(-200);
        }else {
            if (flag){
                if(player.getDetectCollision().exactCenterX() >= enemies.getDetectCollision().exactCenterX()){
                    countMisses++;
                    flag = false;
                    if(countMisses==3){
                        playing = false;
                        isGameOver = true;

                        gameOnSound.stop();

                        gameOverSound.start();

                        for(int i = 0; i<4; i++){
                            if(highScore[i]<score){
                                final  int finalI = i;
                                highScore[i] = score;
                                break;
                            }
                        }

                        SharedPreferences.Editor e = sharedPreferences.edit();
                        for(int i = 0; i<4; i++){
                            int j = i+1;
                            e.putInt("score"+j, highScore[i]);
                        }
                        e.apply();

                    }
                }
            }
        }

        friend.update(player.getSpeed());

        if(Rect.intersects(player.getDetectCollision(), friend.getDetectCollision())){
            boom.setX(friend.getX());
            boom.setY(friend.getY());
            playing = false;
            isGameOver = true;

            gameOnSound.stop();

            gameOverSound.start();

            for(int i = 0; i<4; i++){
                if(highScore[i]<score){
                    final  int finalI = i;
                    highScore[i] = score;
                    break;
                }
            }

            SharedPreferences.Editor e = sharedPreferences.edit();
            for(int i = 0; i<4; i++){
                int j = i+1;
                e.putInt("score"+j, highScore[i]);
            }
            e.apply();
        }

    }

    private void draw() {
        if(surfaceHolder.getSurface().isValid()){
            canvas = surfaceHolder.lockCanvas();
            canvas.drawColor(Color.BLACK);

            paint.setColor(Color.WHITE);

            for(Star s : stars){
                paint.setStrokeWidth(s.getStarWidth());
                canvas.drawPoint(s.getX(), s.getY(), paint);
            }

            paint.setTextSize(35);
            canvas.drawText("score:" +score, 120,60, paint);

            canvas.drawBitmap(
                    player.getBitmap(),
                    player.getX(),
                    player.getY(),
                    paint
            );

//            for(int i=0; i < enemyCount; i++){
//                canvas.drawBitmap(
//                        enemies[i].getBitmap(),
//                        enemies[i].getX(),
//                        enemies[i].getY(),
//                        paint
//                );
//            }

            canvas.drawBitmap(
                        enemies.getBitmap(),
                        enemies.getX(),
                        enemies.getY(),
                        paint
            );

            canvas.drawBitmap(
                    boom.getBitmap(),
                    boom.getX(),
                    boom.getY(),
                    paint
            );

            canvas.drawBitmap(
                    friend.getBitmap(),
                    friend.getX(),
                    friend.getY(),
                    paint
            );


            if(isGameOver){
                paint.setTextSize(180);
                paint.setTextAlign(Paint.Align.CENTER);

                int yPosition = (int) ((canvas.getHeight()/2) - ((paint.descent() + paint.ascent()/2)));
                canvas.drawText("GAME OVER!", canvas.getWidth()/2, yPosition, paint);

            }

            surfaceHolder.unlockCanvasAndPost(canvas);
        }
    }

    public void pause(){
        playing= false;
        try{
            gameThread.join();
        }catch (InterruptedException e){

        }
    }

    public void resume(){
        playing = true;
        gameThread = new Thread(this);
        gameThread.start();
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {

        switch(event.getAction() & MotionEvent.ACTION_MASK){
            case MotionEvent.ACTION_UP:
                player.stopBoosting();
                break;
            case MotionEvent.ACTION_DOWN:
                player.setBoosting();
                break;
        }

        if(isGameOver){
            if(event.getAction()==MotionEvent.ACTION_DOWN){
                context.startActivity(new Intent(context, MainActivity.class));
            }
        }



        return true;
    }

    public static  void stopMusic(){
        gameOnSound.stop();
    }
}
