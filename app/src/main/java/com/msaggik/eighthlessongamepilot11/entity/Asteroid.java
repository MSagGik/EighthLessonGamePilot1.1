package com.msaggik.eighthlessongamepilot11.entity;

import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Rect;

import com.msaggik.eighthlessongamepilot11.R;

public class Asteroid {

    // поля
    private int x, y; // смещение астероида по направлениям осей координат
    private int width, height; // ширина и высота для растрового изображения
    private Bitmap asteroid1, asteroid2, asteroid3, asteroid4; // поля растровых изображений летящего астероида
    private int asteroidCounter = 1; // поле счётчика вывода изображений астероида
    private int speed = 5; // скорость движения астероида
    private boolean wasShoot = true; // поле флага активности астероида (true - деактивирован, false - опасен)

    // конструктор
    public Asteroid(int screenX, int screenY, Resources resources) {

        // считывание изображений летящего астероида
        asteroid1 = BitmapFactory.decodeResource(resources, R.drawable.asteroid1);
        asteroid2 = BitmapFactory.decodeResource(resources, R.drawable.asteroid2);
        asteroid3 = BitmapFactory.decodeResource(resources, R.drawable.asteroid3);
        asteroid4 = BitmapFactory.decodeResource(resources, R.drawable.asteroid4);

        // инициализация размеров астероида с масштабированием
        width = asteroid1.getWidth() / 3;
        height = asteroid1.getHeight() / 3;

        // приведение размера астероида совместимым с другими экранами
        width = (int)(width * 1920f / screenX);
        height = (int)(height * 1080f / screenY);

        // изменение размера изображения астероида, где width и height соответственно ширина и высота
        asteroid1 = Bitmap.createScaledBitmap(asteroid1, width, height, false);
        asteroid2 = Bitmap.createScaledBitmap(asteroid2, width, height, false);
        asteroid3 = Bitmap.createScaledBitmap(asteroid3, width, height, false);
        asteroid4 = Bitmap.createScaledBitmap(asteroid4, width, height, false);

        // задание изначальных координат (будет скрыт за экраном в самом начале)
        x = 0;
        y = - height;
    }

    // метод задания очерёдности переключения изображений астероида при разных условиях
    public Bitmap getAsteroid() {

        // задание очереди переключения изображений движения астероида
        if (asteroidCounter == 1) {
            asteroidCounter++;
            return asteroid1;
        } else if (asteroidCounter == 2) {
            asteroidCounter++;
            return asteroid2;
        } else if (asteroidCounter == 3) {
            asteroidCounter++;
            return asteroid3;
        } else {
            asteroidCounter = 1;
            return asteroid4;
        }
    }

    // метод задания прямоугольника вокруг астероида
    public Rect getCollisionShape() {
        // вывод квадрата с координатами краёв астероида
        return new Rect(x, y, x + width, y + height);
    }

    // геттеры и сеттеры
    public int getX() {
        return x;
    }

    public void setX(int x) {
        this.x = x;
    }

    public int getY() {
        return y;
    }

    public void setY(int y) {
        this.y = y;
    }

    public int getSpeed() {
        return speed;
    }

    public void setSpeed(int speed) {
        this.speed = speed;
    }

    public int getWidth() {
        return width;
    }

    public void setWidth(int width) {
        this.width = width;
    }

    public int getHeight() {
        return height;
    }

    public void setHeight(int height) {
        this.height = height;
    }

    public boolean isWasShoot() {
        return wasShoot;
    }

    public void setWasShoot(boolean wasShoot) {
        this.wasShoot = wasShoot;
    }
}
