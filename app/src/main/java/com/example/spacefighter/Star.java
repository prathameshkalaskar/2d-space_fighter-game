package com.example.spacefighter;

import java.util.Random;

public class Star {
    int x,y, speed;
    int maxX, maxY, minX, minY;

    public Star(int screenX, int screenY) {
        maxX = screenX;
        maxY = screenY;
        minX = 0;
        minY = 0;

        Random generator = new Random();
        speed = generator.nextInt();

        x = generator.nextInt(maxX);
        y = generator.nextInt(maxY);

    }

    public void update(int playerSpeed){
        x -= playerSpeed;
        x -= speed;

        if(x < 0){
            x = maxX;
            Random generator = new Random();
            y = generator.nextInt(maxY);
            speed = generator.nextInt(15);
        }

    }

    public float getStarWidth(){
        float minX = 1.0f;
        float maxX = 4.0f;
        Random random = new Random();
        float finalX = random.nextFloat() * (maxX - minX) + minX;
        return finalX;
    }

    public int getX() {
        return x;
    }

    public int getY() {
        return y;
    }
}
