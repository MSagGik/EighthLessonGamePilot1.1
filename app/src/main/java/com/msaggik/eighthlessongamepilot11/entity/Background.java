package com.msaggik.eighthlessongamepilot11.entity;

import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;

import com.msaggik.eighthlessongamepilot11.R;

public class Background {

    // поля
    private int x = 0, y = 0; // смещение фона по направлениям осей координат
    private Bitmap background; // поле растрового изображения

    // конструктор (размеры по оси X и Y, ресурс)
    public Background(int screenX, int screenY, Resources resources) {
        // считывание фонового изображения из ресурсов
        background = BitmapFactory.decodeResource(resources, R.drawable.background);
        /** метод createScaledBitmap() меняет размер фонового изображения, где screenX и screenY соответственно ширина и высота,
         *  последняя булевская переменная отвечает за сглаживание пикселей (актуальна при увеличении размера изображения)
         */
        background = Bitmap.createScaledBitmap(background, screenX, screenY, false);
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

    public Bitmap getBackground() {
        return background;
    }

    public void setBackground(Bitmap background) {
        this.background = background;
    }
}
