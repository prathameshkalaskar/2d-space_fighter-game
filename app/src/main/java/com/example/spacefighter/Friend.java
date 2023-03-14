package com.example.spacefighter;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Rect;

import java.util.Random;

public class Friend {
    Bitmap bitmap;

    int x,y;

    int speed = 1;

    int maxX, minX, maxY, minY;


    Rect detectCollision;

    public Friend(Context context,int screenX, int screenY){
        bitmap = BitmapFactory.decodeResource(context.getResources(), R.drawable.friend);
        maxX = screenX;
        maxY = screenY;
        minX = 0;
        minY = 0;

        Random random = new Random();
        speed = random.nextInt(6) + 10;
        x = screenX;
        y = random.nextInt(maxY) - bitmap.getHeight();

        detectCollision = new Rect(x, y, bitmap.getWidth(), bitmap.getHeight());

    }

    public void update(int playerSpeed){
        x -= playerSpeed;
        x -= speed;
        if(x< minX - bitmap.getWidth()){
            Random random = new Random();
            speed  = random.nextInt(10) + 10;
            x = maxX;
            y = random.nextInt(maxY) - bitmap.getHeight();
        }

        detectCollision.left = x;
        detectCollision.top = y;
        detectCollision.right = x + bitmap.getWidth();
        detectCollision.bottom = y + bitmap.getHeight();

    }

    public Rect getDetectCollision() {
        return detectCollision;
    }

    public Bitmap getBitmap() {
        return bitmap;
    }

    public int getX() {
        return x;
    }

    public int getY() {
        return y;
    }
}
