package com.msaggik.eighthlessongamepilot11.entity;

import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Rect;

import com.msaggik.eighthlessongamepilot11.R;

public class Bullet {

    // поля
    private int x, y; // смещение снаряда по направлениям осей координат
    private Bitmap bullet; // поле растрового изображения летящего снаряда
    private int width, height; // ширина и высота для растрового изображения

    // конструктор
    public Bullet(int screenX, int screenY, Resources resources) {

        // считывание изображения летящего снаряда
        bullet = BitmapFactory.decodeResource(resources, R.drawable.bullet);

        // инициализация размеров снаряда с масштабированием
        width = bullet.getWidth() / 3;
        height = bullet.getHeight() / 3;

        // приведение размера снаряда совместимым с другими экранами
        width = (int)(width * 1920f / screenX);
        height = (int)(height * 1080f / screenY);

        // изменение размера изображения снаряда, где width и height соответственно ширина и высота
        bullet = Bitmap.createScaledBitmap(bullet, width, height, false);
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

    public Bitmap getBullet() {
        return bullet;
    }

    public void setBullet(Bitmap bullet) {
        this.bullet = bullet;
    }
}
