package com.msaggik.eighthlessongamepilot11.entity;

import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Rect;

import com.msaggik.eighthlessongamepilot11.GameView;
import com.msaggik.eighthlessongamepilot11.R;

public class Flight {

    // поля
    private int x = 0, y = 0; // смещение самолёта по направлениям осей координат
    private int width, height; // ширина и высота для растрового изображения
    private int wingCounter = 0; // поле счётчика крыла
    private Bitmap flight1, flight2; // поле растровых изображений летящего самолёта
    private boolean isGoingUp = false; // направление движения самолёта (true - вверх, false - вниз)
    private Bitmap shoot1, shoot2, shoot3, shoot4, shoot5; // поле растровых изображений самолёта в момент атаки астероидов
    private int toShoot= 0; // поле выпуска снаряда по астероиду (0 - не выпускается, 1 - выпускается)
    private int shootCounter = 1; // счётчик выпуска снарядов
    private GameView gameView; // поле представления игры
    private Bitmap planeShotDown; // поле растрового изображения подбитового самолёта

    // конструктор (размеры по оси X и Y, ресурс)
    public Flight(GameView gameView, int screenX, int screenY, Resources resources) {
        this.gameView = gameView;

        // считывание изображений летящих самолётов из ресурсов
        flight1 = BitmapFactory.decodeResource(resources, R.drawable.plane_fly1);
        flight2 = BitmapFactory.decodeResource(resources, R.drawable.plane_fly2);

        // считывание изображения подбитого самолёта из ресурсов
        planeShotDown = BitmapFactory.decodeResource(resources, R.drawable.plane_shot_down);

        // считывание изображений атакующих самолётов из ресурсов
        shoot1 = BitmapFactory.decodeResource(resources, R.drawable.plane_shoot1);
        shoot2 = BitmapFactory.decodeResource(resources, R.drawable.plane_shoot2);
        shoot3 = BitmapFactory.decodeResource(resources, R.drawable.plane_shoot3);
        shoot4 = BitmapFactory.decodeResource(resources, R.drawable.plane_shoot4);
        shoot5 = BitmapFactory.decodeResource(resources, R.drawable.plane_shoot5);

        // инициализация размеров самолёта с масштабированием
        width = flight1.getWidth() / 3;
        height = flight1.getHeight() / 3;

        // приведение размера самолёта совместимым с другими экранами
        width = (int)(width * 1920f / screenX);
        height = (int)(height * 1080f / screenY);

        // изменение размера изображения самолёта, где width и height соответственно ширина и высота
        flight1 = Bitmap.createScaledBitmap(flight1, width, height, false);
        flight2 = Bitmap.createScaledBitmap(flight2, width, height, false);
        // изменение размера изображения подбитого самолёта, где width и height соответственно ширина и высота
        planeShotDown = Bitmap.createScaledBitmap(planeShotDown, width, height, false);
        // изменение размера изображения снаряда, где width и height соответственно ширина и высота
        shoot1 = Bitmap.createScaledBitmap(shoot1, width, height, false);
        shoot2 = Bitmap.createScaledBitmap(shoot2, width, height, false);
        shoot3 = Bitmap.createScaledBitmap(shoot3, width, height, false);
        shoot4 = Bitmap.createScaledBitmap(shoot4, width, height, false);
        shoot5 = Bitmap.createScaledBitmap(shoot5, width, height, false);

        // начальное местоположение полёта
        y = screenY / 2; // посередине оси Y
        x = screenX / 21; // практически вначале оси X
    }

    // метод задания очерёдности переключения изображений самолёта при разных условиях
    public Bitmap getFlight() {

        // задание очереди переключения изображений выпуска снаряда
        if (toShoot != 0) {
            if (shootCounter == 1) {
                shootCounter++;
                return shoot1;
            } else if (shootCounter == 2) {
                shootCounter++;
                return shoot2;
            } else if (shootCounter == 3) {
                shootCounter++;
                return shoot3;
            } else if (shootCounter == 4) {
                shootCounter++;
                return shoot4;
            } else {
                shootCounter = 1;
                toShoot--; // задание флага обрыва очереди переключения изображений
                gameView.newBullet(); // метод выпуска снаряда по астероиду
                return shoot5;
            }
        }

        // задание очерёдности переключения изображений полёта
        if (wingCounter == 0) {
            wingCounter++;
            return flight1;
        } else if(wingCounter > 0) {
            wingCounter--;
            return flight2;
        }

        return null;
    }

    // метод задания прямоугольника вокруг самолёта
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

    public boolean isGoingUp() {
        return isGoingUp;
    }

    public void setGoingUp(boolean goingUp) {
        isGoingUp = goingUp;
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

    public int getToShoot() {
        return toShoot;
    }

    public void setToShoot(int toShoot) {
        this.toShoot = toShoot;
    }

    public Bitmap getPlaneShotDown() {
        return planeShotDown;
    }

    public void setPlaneShotDown(Bitmap planeShotDown) {
        this.planeShotDown = planeShotDown;
    }
}
