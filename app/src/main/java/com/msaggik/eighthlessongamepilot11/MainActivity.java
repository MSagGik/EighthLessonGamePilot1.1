package com.msaggik.eighthlessongamepilot11;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

public class MainActivity extends AppCompatActivity {

    // поля
    private TextView highScore; // поле вывода рекордных значений
    private boolean isMute; // поле включения и выключения звука приложения
    private ImageView volumeOn; // поле изображения включения и выключения звука

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // определение полноэкранного режима
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);

        // обработка нажатия на TextView с id play
        findViewById(R.id.play).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // переключение активити с помощью намерения
                startActivity(new Intent(MainActivity.this, GameActivity.class));
            }
        });

        // присваивание id созданным переменным
        highScore = findViewById(R.id.highScore);
        volumeOn = findViewById(R.id.volumeOn);

        // получение доступа к настройкам
        SharedPreferences preferences = getSharedPreferences("game", MODE_PRIVATE);
        // вывод на экран рекордного значения
        highScore.setText("Рекорд: " + preferences.getInt("highscore",0));

        // включение и выключение звука в настройках
        // метод getBoolean("isMute", false) возвращает булевское значение по ключу "isMute", если такого нет то возвращает false
        isMute = preferences.getBoolean("isMute", false);
        // загрузка иконок на картинку звука
        if (isMute) {
            volumeOn.setImageResource(R.drawable.ic_baseline_volume_off_24);
        } else {
            volumeOn.setImageResource(R.drawable.ic_baseline_volume_up_24);
        }
        // обработка нажатия на картинку звука для включения и выключения звука
        volumeOn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                isMute = !isMute;
                // загрузка иконок на картинку звука
                if (isMute) {
                    volumeOn.setImageResource(R.drawable.ic_baseline_volume_off_24);
                } else {
                    volumeOn.setImageResource(R.drawable.ic_baseline_volume_up_24);
                }
                // внесение данных в настройки приложения
                SharedPreferences.Editor editor = preferences.edit();
                editor.putBoolean("isMute", isMute);
                editor.apply();
            }
        });
    }
}