package ru.katagarov.traningintuition;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;

import android.content.Context;
import android.graphics.Color;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {

    private static final String TAG = "MainActivity";

    private TextView randomNum;
    private TextView timer;
    private TextView resultsCount;
    Button option1;
    Button option2;
    Button option3;
    Button option4;

    private int successCount = 0;
    private int errorCount = 0;
    private int percentSuccess = 0;

    private int successIndex = 0;
    private Handler activityHandler = new Handler();
    private IntervalTimer intervalTimer;
    private ArrayList<String> randomList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // Инициализация UI
        randomNum = findViewById(R.id.main__random_number);
        timer = findViewById(R.id.main__timer);
        resultsCount = findViewById(R.id.main__results_count);
        option1 = findViewById(R.id.main__options_1);
        option2 = findViewById(R.id.main__options_2);
        option3 = findViewById(R.id.main__options_3);
        option4 = findViewById(R.id.main__options_4);

        // Скрываем заголовок
        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            actionBar.hide();
        }
        // Загадываем случайный фрукт
        this.setRandomUniqueFruit();

        // Инициализируем и запускаем таймер
        intervalTimer = new IntervalTimer(1, 30, activityHandler);
        timer.setText(String.format(getString(R.string.main__text_timer), 30));
        intervalTimer.start(nextTick(this), cancelTimer());

        // Инициализируем "точность интуиции" и скрываем ее, до первого измерения
        resultsCount.setText(String.format(getString(R.string.main__results_count), percentSuccess));
        resultsCount.setVisibility(View.INVISIBLE);
    }

    Runnable nextTick(Context context) {
        return new Runnable() {
            @Override
            public void run() {
                if (intervalTimer.getCurrentCounter() <= 10) {
                    timer.setTextColor(ContextCompat.getColor(context, R.color.colorMainRandomNumberError));
                }

                timer.setText(String.format(getString(R.string.main__text_timer), intervalTimer.getCurrentCounter()));
            }
        };
    }

    Runnable cancelTimer() {
        return new Runnable() {
            @Override
            public void run() {
                randomNum.setText(R.string.main__random_init);
                randomNum.setBackground(getDrawable(R.drawable.tv__main__random_number));
                randomNum.setTextColor(Color.GRAY);
                timer.setTextColor(Color.GRAY);
                randomList.clear();
                setRandomUniqueFruit();
            }
        };
    }

    ArrayList<String> setListFruit() {
        ArrayList<String> listFruit = new ArrayList<>();
        for (int countIndex = 0; countIndex < 4; ) {
            String fruit = Generator.getRandomFruit();

            if (listFruit.indexOf(fruit) == -1) {
                listFruit.add(fruit);
                countIndex++;
            }
        }
        return listFruit;
    }

    void setRandomUniqueFruit() {
        randomList = setListFruit();
        successIndex = Generator.random(0, 3);

        option1.setText(randomList.get(0));
        option2.setText(randomList.get(1));
        option3.setText(randomList.get(2));
        option4.setText(randomList.get(3));

        option1.setOnClickListener((View v) -> {
            onClickOptions(option1);
        });

        option2.setOnClickListener((View v) -> {
            onClickOptions(option2);
        });

        option3.setOnClickListener((View v) -> {
            onClickOptions(option3);
        });

        option4.setOnClickListener((View v) -> {
            onClickOptions(option4);
        });
    }

    void onClickOptions(Button option) {

        if (resultsCount.getVisibility() == View.INVISIBLE) {
            resultsCount.setVisibility(View.VISIBLE);
        }

        // Проверяем, угадал ли пользователь загаданный фрукт
        if (randomList.get(successIndex).equals(option.getText().toString())) {
            randomNum.setBackground(getDrawable(R.drawable.tv__main__random_number_success));
            randomNum.setText(getString(R.string.main__random_result_success));
            randomNum.setTextColor(ContextCompat.getColor(this, R.color.colorMainRandomNumberSuccess));
            successCount++;
        } else {
            randomNum.setBackground(getDrawable(R.drawable.tv__main__random_number_error));
            randomNum.setText(getString(R.string.main__random_result_error));
            randomNum.setTextColor(ContextCompat.getColor(this, R.color.colorMainRandomNumberError));
            errorCount++;
        }

        // Высчитываем процент точности интуиции и выводим его на экран
        percentSuccess = (int) ((float) successCount / (float)(successCount + errorCount) * 100);
        resultsCount.setText(String.format(getString(R.string.main__results_count), percentSuccess));

        // Сброс стилей, времени, списков
        intervalTimer.resetCounter();
        timer.setText(String.format(getString(R.string.main__text_timer), intervalTimer.getCurrentCounter()));
        timer.setTextColor(Color.GRAY);
        randomList.clear();

        this.setRandomUniqueFruit();
    }
}
