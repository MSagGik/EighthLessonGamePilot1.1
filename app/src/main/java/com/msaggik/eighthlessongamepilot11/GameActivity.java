package com.msaggik.eighthlessongamepilot11;

import android.graphics.Point;
import android.os.Bundle;
import android.view.WindowManager;

import androidx.appcompat.app.AppCompatActivity;

public class GameActivity extends AppCompatActivity {

    // поля
    private GameView gameView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // определение полноэкранного режима
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);

        // определение размеров экрана
        Point point = new Point(); // с помощью данного объекта можно получить размеры экрана по осям X и Y
        getWindowManager().getDefaultDisplay().getSize(point);

        // создание объекта представления фона игры (контекст, длина представления по оси X, длина представления по оси Y)
        gameView = new GameView(this, point.x, point.y);

        // передача gameView в представление активити
        setContentView(gameView);
    }

    @Override
    protected void onPause() {
        super.onPause();
        // ввод игры в режим паузы
        gameView.pauseThread();
    }

    @Override
    protected void onResume() {
        super.onResume();
        // ввод игры в активный режим
        gameView.resumeThread();
    }
}