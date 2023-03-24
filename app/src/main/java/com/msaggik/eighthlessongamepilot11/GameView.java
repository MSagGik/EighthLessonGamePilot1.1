package com.msaggik.eighthlessongamepilot11;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Rect;
import android.media.AudioAttributes;
import android.media.AudioManager;
import android.media.SoundPool;
import android.os.Build;
import android.view.MotionEvent;
import android.view.SurfaceView;

import com.msaggik.eighthlessongamepilot11.entity.Asteroid;
import com.msaggik.eighthlessongamepilot11.entity.Background;
import com.msaggik.eighthlessongamepilot11.entity.Bullet;
import com.msaggik.eighthlessongamepilot11.entity.Flight;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class GameView extends SurfaceView implements Runnable{

    // поля
    private  Thread thread; // поле нового потока
    private boolean isPlaying; // поле запуска и приостановления игры
    private Background background1, background2; // поля работы с фоном (необходимо два, что было непрерывное движение фона)
    private int screenX, screenY; // поля размеров экрана по осям X и Y
    private Paint paint; // поле стилей рисования
    private float screenRatioX, screenRatioY; // поля размеров экрана для совместимости разных размеров экрана
    private Flight flight; // создание поля самолёта
    private List<Bullet> bulletList; // поле контейнера для списка снаряда
    private Asteroid[] asteroids; // создание поля массива объектов класса астероида
    private Random random; // создание поля объекта класса рандом
    private boolean isGameOver = false; // поле флага окончания игры
    private int score = 0; // счётчик сбитых астероидов
    private SharedPreferences preferences; // поле для переменной настроек, находящееся в хранилище платформы Android
    private GameActivity activity; // создание поля класса GameActivity
    private SoundPool soundPool; // поле для проигрывания маленьких аудиоклипов
    private int sound; // целочисленная переменная для звука

    // конструктор на основе SurfaceView
    public GameView(GameActivity activity, int screenX, int screenY) {
        super(activity);
        this.activity = activity;

        // получение доступа к настройкам андроида
        // MODE_PRIVATE – используется в большинстве случаев для приватного доступа к данным приложением-владельцем
        preferences = activity.getSharedPreferences("game", Context.MODE_PRIVATE);

        // если сборка проекта новая, то
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            // 1 вариант настройки воспроизведения аудио
            AudioAttributes audioAttributes = new AudioAttributes.Builder()
                    .setContentType(AudioAttributes.CONTENT_TYPE_MUSIC)
                    .setUsage(AudioAttributes.USAGE_GAME)
                    .build();
            // загрузка настроек
            soundPool = new SoundPool.Builder()
                    .setAudioAttributes(audioAttributes)
                    .build();
        }  else { // иначе если старая сборка, то
            // 2 вариант настройки воспроизведения аудио
            soundPool = new SoundPool(1, AudioManager.STREAM_MUSIC, 0);
        }
        // загрузка нужного аудиофайла из папки res/raw
        sound = soundPool.load(activity, R.raw.shoot, 1);

        this.screenX = screenX;
        this.screenY = screenY;
        screenRatioX = 1920f / screenX; // калибровка совместимости оси X
        screenRatioY = 1080f / screenY; // калибровка совместимости оси Y

        // создание объектов фонов (размеры, ранее созданный ресурс)
        background1 = new Background(screenX, screenY, getResources());
        background2 = new Background(screenX, screenY, getResources());

        // присваивание полю x класса Background переменной ширины screenX
        background2.setX(screenX); // второй фон мы сдвигаем по оси Х с нуля на размер ширины изображения

        paint = new Paint(); // создание объекта стиля рисования
        paint.setTextSize(128); // размер текста отображения на экране счёта игры
        paint.setColor(Color.WHITE); // установление белого цвета текста

        // создание объекта самолёта
        flight = new Flight(this, screenX, screenY, getResources());

        // инициализация контейнера списка для снаряда
        bulletList = new ArrayList<>();

        // инициализация массива объектов класса астероида
        asteroids = new Asteroid[4]; // обновременно на экране будет 4 астероида
        // создание 4 объектов астероида и помещение их в массив asteroids
        for (int i = 0; i < 4; i++) {
            Asteroid asteroid = new Asteroid(screenX, screenY, getResources());
            asteroids[i] = asteroid;
        }
        // создание объекта класса рандом
        random = new Random();
    }

    // реализация метода run() дополнительного потока
    @Override
    public void run() {
        // операции в потока
        while (isPlaying) {
            // методы запускаемые в потоке
            update();
            draw();
            sleep();
        }
    }
    // метод обновления потока
    private void update() {
        // сдвиг фона по оси X на 10 пикселей и преобразование для совместимости разных экранов
        background1.setX(background1.getX() - (int)(10 * screenRatioX));
        background2.setX(background2.getX() - (int)(10 * screenRatioX));

        if ((background1.getX() + background1.getBackground().getWidth()) <= 0) { // если фон 1 полностью исчез с экрана
            background1.setX(screenX); // то обновление x до размера ширины фона
        }
        if ((background2.getX() + background2.getBackground().getWidth()) <= 0) { // если фон 2 полностью исчез с экрана
            background2.setX(screenX); // то обновление x до размера ширины фона
        }

        // задание скорости подъёма и снижения самолёта
        if (flight.isGoingUp()) { // условие подъёма
            flight.setY(flight.getY() - (int)(30 * screenRatioY));
        } else { // условие снижения
            flight.setY(flight.getY() + (int)(30 * screenRatioY));
        }
        // задание порога значений местоположения самолёта
        if (flight.getY() < 0) { // запрет на снижение меньше нуля
            flight.setY(0);
        } else if (flight.getY() >= screenY - flight.getHeight()) { // запрет на подъём выше экрана за минусом высоты самолёта
            flight.setY(screenY - flight.getHeight());
        }

        // задание мусорной корзины для снарядов
        List<Bullet> trash = new ArrayList<>();

        // задание цикла для каждого снаряда
        for (Bullet bullet : bulletList) {
            if (bullet.getX() > screenX) { // если снаряд ушёл за конец экрана по оси X
                trash.add(bullet); // помещение в корзину данного снаряда
                break; // выход из цикла
            }
            // сдвиг снаряда по оси X на 50 пикселей и преобразование для совместимости разных экранов
            bullet.setX(bullet.getX() + (int)(50 * screenRatioX));

            // обработка условия столкновения снаряда и астероида
            for (Asteroid asteroid : asteroids) {
                // обработка условия столкновения снаряда и астероида
                if (Rect.intersects(asteroid.getCollisionShape(), bullet.getCollisionShape())) {
                    score++; // добавление очка за сбитый астероид
                    asteroid.setX(-500); // перемещение астероида в конец экрана, чтобы астероид появился снова
                    bullet.setX(screenX + 500); // перемещение снаряда за экран и добавление в корзину для удаления
                    asteroid.setWasShoot(true); // флаг деактивирования астероида
                }
            }
        }

        // очистка контейнера для хранения снарядов от снарядов находящихся параллельно в мусорной корзине
        for (Bullet bullet : trash) {
            bulletList.remove(bullet);
        }

        // цикл изменения координат для каждого астероида
        for (Asteroid asteroid : asteroids) {
            // изменение координаты X астероида с определённой скоростью
            asteroid.setX(asteroid.getX() - asteroid.getSpeed());
            // если астероид оказался за пределами экрана слевой стороны
            if ((asteroid.getX() + asteroid.getWidth()) < 0) {

                // если астероид опасен (в него не попадал снаряд)
                if (!asteroid.isWasShoot()) {
                    isGameOver = true; // установление флага окончания игры
                    return;
                }

                int bound = (int) (23 * screenRatioX); // ограничение скорости астероида и её масштабирование
                asteroid.setSpeed(random.nextInt(bound) + 1); // задание случайной скорости для астероидов (в пределах от 1 до 30)
                // если скорость астероида меньше 10
                if (asteroid.getSpeed() < (5 * screenRatioX)) {
                    asteroid.setSpeed((int) (5 * screenRatioX)); // то установим ограничение минимальной скорости 10
                }
                // меняем положение астероида в самое начало справа
                asteroid.setX(screenX); // крайная правая точка оси X
                asteroid.setY(random.nextInt(screenY - asteroid.getHeight())); // случайное положение относительно оси Y

                asteroid.setWasShoot(false); // установления флага опасности при перемещении астероида снова в начало пути
            }
            // обработка условия столкновения самолёта и астероида
            if (Rect.intersects(asteroid.getCollisionShape(), flight.getCollisionShape())) {
                isGameOver = true; // установление флага окончания игры
                return;
            }
        }
    }

    // метод рисования в потоке
    private void draw() {

        if (getHolder().getSurface().isValid()) { // проверка валидности объекта surface

            Canvas canvas = getHolder().lockCanvas(); // метод lockCanvas() возвращает объект Canvas (холст для рисования)
            // метод drawBitmap() рисует растровое изображение фона на холсте (изображение, координаты X и Y, стиль для рисования)
            canvas.drawBitmap(background1.getBackground(), background1.getX(), background1.getY(), paint);
            canvas.drawBitmap(background2.getBackground(), background2.getX(), background2.getY(), paint);

            // отрисовка растровых изображений астероидов
            for (Asteroid asteroid : asteroids) {
                canvas.drawBitmap(asteroid.getAsteroid(), asteroid.getX(), asteroid.getY(), paint);
            }

            // отрисовка текста на экране (сообщение, координаты, стиль для рисования)
            canvas.drawText(score + "", screenX / 2f, screenY / 8f, paint);

            // отрисовка растрового изображения подбитого самолёта при окончании игры
            if (isGameOver) {
                isPlaying = false; // установление флага приостановления дополнительного потока
                // отрисовка растрового изображения подбитого самолёта
                canvas.drawBitmap(flight.getPlaneShotDown(), flight.getX(), flight.getY(), paint);
                // вывод нарисованных изображений на экран
                getHolder().unlockCanvasAndPost(canvas);
                // сохранение рекордного результата
                saveIfHighScore();
                // считывание рекордного результата при загрузке приложения
                waitBeforeExiting();
                return;
            }

            // отрисовка растрового изображения самолёта
            canvas.drawBitmap(flight.getFlight(), flight.getX(), flight.getY(), paint);

            // отрисовка движения снаряда
            for (Bullet bullet : bulletList) {
                canvas.drawBitmap(bullet.getBullet(), bullet.getX(), bullet.getY(), paint);
            }

            // вывод нарисованных изображений на экран
            getHolder().unlockCanvasAndPost(canvas);
        }
    }

    // метод засыпания потока
    private void sleep() {
        try {
            // засыпание потока на 16 милисекунд
            Thread.sleep(16);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    // метод запуска потока
    public void resumeThread() {
        // установление флага запуска игры
        isPlaying = true;
        // создание объекта потока
        thread = new Thread(this);
        // запуск потока
        thread.start();
    }
    // метод паузы потока
    public void pauseThread() {
        try {
            // установление флага приостановления игры
            isPlaying = false;
            // приостановление потока
            thread.join();
            /**
             * метод join() — используется для того,
             * чтобы приостановить выполнение текущего потока до тех пор,
             * пока другой поток не закончит свое выполнение
             */
        } catch (InterruptedException e) { // исключение на случай зависания потока
            e.printStackTrace();
        }
    }

    // метод обработки касания экрана (для управления самолётом)
    @Override
    public boolean onTouchEvent(MotionEvent event) {

        // обработка событий касания экрана
        switch (event.getAction()) {
            case MotionEvent.ACTION_DOWN: // нажатие
                // если пользователь нажал на левую сторону экрана
                if (event.getX() < (screenX / 2)) {
                    // то движение самолёта вверх
                    flight.setGoingUp(true);
                }

                break;
            case MotionEvent.ACTION_MOVE: // движение по экрану

                break;
            case MotionEvent.ACTION_UP: // отпускание
                // при отпускании экрана самолёт начнёт снижаться
                flight.setGoingUp(false);
                // если пользователь отпустил экран на правой стороне экрана
                if (event.getX() > (screenX / 2)) {
                    // то установится флаг выпуска снаряда по астероиду
                    flight.setToShoot(flight.getToShoot() + 1);
                }
                break;
        }

        return true; // активация обработки касания экрана
    }

    // метод выпуска снаряда по астероиду
    public void newBullet() {

        // если звук в настройках включен, то
        if (!preferences.getBoolean("isMute", false)) {
            // при выпуске снаряда воспроизводится звук (индекс, стерео, приоритет, повтор - нет, порядок)
            soundPool.play(sound, 1,1, 0, 0, 1);
        }

        // создание объекта снаряда
        Bullet bullet = new Bullet(screenX, screenY, getResources());
        // определение начальных координат положения снаряда на экране
        bullet.setX(flight.getX() + flight.getWidth()); // координата X (положения самолёта + ширина самолёта)
        bullet.setY(flight.getY() + (flight.getHeight() / 2)); // координата Y (положения самолёта + половина высоты самолёта)
        // добавление объекта снаряда в список снарядов
        bulletList.add(bullet);
    }

    // метод определения рекордного результата
    private void saveIfHighScore() {
        // если количество сбитых астероидов больше рекордного, то
        if (preferences.getInt("highscore", 0) < score) {
            // сохраним рекордное значение
            SharedPreferences.Editor editor = preferences.edit(); // изменение настроек
            editor.putInt("highscore", score); // внесение нового рекордного значения
            editor.apply(); // сохранение результатов
        }
    }
    // метод считывания рекордного результата при загрузке приложения
    private void waitBeforeExiting() {
        // ввод вспомогательного потока в сон на 3 секунды
        try {
            Thread.sleep(3000);
            // переход к основной активити
            activity.startActivity(new Intent(activity, MainActivity.class));
            activity.finish(); // закрытие данной активити
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }
}