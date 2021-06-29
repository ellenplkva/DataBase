package com.androidexample.demopingvimath;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.media.Image;
import android.os.Build;
import android.os.Bundle;
import android.view.Gravity;
import android.view.MotionEvent;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashSet;
import java.util.Random;
import java.util.Set;
import java.util.concurrent.ThreadLocalRandom;

import static java.lang.Math.random;

public class FoldLevel1 extends AppCompatActivity {

    public int numLeft;
    public int numRight;
    public int count = 0;

    Array fold_array = new Array();
    Array fold_array_answ = new Array();
    Random fold_random = new Random();//для генерации случайных чисел
    ArrayList<Integer> list = new ArrayList<Integer>();


    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.fold_level1);

        // развернуть игру на весь экран
        Window w = getWindow();
        w.setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);

        ImageView img_left = (ImageView) findViewById(R.id.img_left_fold);
        ImageView img_right = (ImageView)findViewById(R.id.img_right_fold);

        Button btn1_fold_answ = (Button)findViewById(R.id.btn1_answ_fold);
        Button btn2_fold_answ = (Button)findViewById(R.id.btn2_answ_fold);
        Button btn3_fold_answ = (Button)findViewById(R.id.btn3_answ_fold);

        // кнопка назад в приложении
        Button fold_btn_back1 = (Button) findViewById(R.id.button_back1);



        fold_btn_back1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                try {
                    Intent intent = new Intent(FoldLevel1.this, FoldLevels.class);
                    startActivity(intent);
                    finish();
                } catch (Exception e) {

                }
            }
        });
        // Для левой картинки цифры
        numLeft = fold_random.nextInt(10);// генерирую случайное число
        img_left.setImageResource(fold_array.images1_fold[numLeft]);// достаю картинку


        //Для правой картинки цифры
        numRight = fold_random.nextInt(10);
        img_right.setImageResource(fold_array.images1_fold[numRight]);

        ArrayList<Integer> list = new ArrayList<Integer>();
        for (int i = (numLeft+numRight) - 1 ; i<=(numLeft+numRight) +1 ; i++) {
            list.add(new Integer(i));
        }
        Collections.shuffle(list);
        btn1_fold_answ.setText(list.get(0));
        btn2_fold_answ.setText(list.get(1));
        btn3_fold_answ.setText(list.get(2));





         final int[] fold_progress = {
                R.id.point1,R.id.point2,R.id.point3,R.id.point4,R.id.point5,
                R.id.point6,R.id.point7,R.id.point8,R.id.point9,R.id.point10,
                R.id.point11,R.id.point12,R.id.point13,R.id.point14,R.id.point15,
                R.id.point16,R.id.point17,R.id.point18,R.id.point19,R.id.point20,
        };






        btn1_fold_answ.setOnTouchListener(new View.OnTouchListener() {
            @SuppressLint("ClickableViewAccessibility")
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                //блокирую другие кнопки

                if (event.getAction() == MotionEvent.ACTION_DOWN) {
                    btn2_fold_answ.setEnabled(false);
                    btn3_fold_answ.setEnabled(false);

                } else if (event.getAction() == MotionEvent.ACTION_UP) {
                    // если отпустили палец
                    if(numLeft + numRight == list.get(0)){
                        if(count < 20){
                            count+=1;
                        }
                        // закрашываю прогресс серым цветом
                        for(int i = 0; i < 20;i++){
                            TextView tv = findViewById(fold_progress[i]);
                            tv.setBackgroundResource(R.drawable.style_points);
                        }
                        // определяю правильные ответы и закрышиваю зелёным
                        for(int i = 0; i < count; i ++){
                            TextView tv = findViewById(fold_progress[i]);
                            tv.setBackgroundResource(R.drawable.style_points_green);
                        }
                    }else
                    {
                        // если левая картинка меньше и ответ неверный
                        if(count > 0){
                            count -= 1;
                        }
                    }
                    // при неверном ответе отнимается от шкалы одна единица и закрашивается обратно в серый
                    for(int i = 0; i < 19;i++){
                        TextView tv = findViewById(fold_progress[i]);
                        tv.setBackgroundResource(R.drawable.style_points);
                    }
                    // определяю правильные ответы и закрышиваю зелёным
                    for(int i = 0; i < count; i ++){
                        TextView tv = findViewById(fold_progress[i]);
                        tv.setBackgroundResource(R.drawable.style_points_green);
                    }
                    if(count == 20){
                        //выход из уровня
                    }else{
                        // палец отпустили значит надо генерить новые числа, но если ошибка в первом ответе, тогда надо ждать пока будет правильно

                        if(count < 1){
                            btn2_fold_answ.setEnabled(true);
                            btn3_fold_answ.setEnabled(true);
                            // выводится высплывающее сообщение " попробуй ещё раз"
                            int time = 1000;
                            Toast toast = Toast.makeText(getApplicationContext(),"Попробуй ещё раз",Toast.LENGTH_LONG);
                            toast.setGravity(Gravity.CENTER,0,0);
                            toast.show();

                        }
                        else {
                            numLeft = fold_random.nextInt(10);// генерирую случайное число
                            img_left.setImageResource(fold_array.images1[numLeft]);// достаю картинку
                            //text_left.setText(array.texts1[numLeft]); // для изменения текста под картинкой

                            //Для правой картинки цифры
                            numRight = fold_random.nextInt(10);
                            img_right.setImageResource(fold_array.images1[numRight]);

                           /*ArrayList<Integer> list = new ArrayList<Integer>();
                            for (int i = (numLeft+numRight) - 1 ; i<=(numLeft+numRight) +1 ; i++) {
                                list.add(new Integer(i));
                            }
                            Collections.shuffle(list);
                            btn1_fold_answ.setText(fold_array_answ.numbers[list.get(0)]);
                            btn2_fold_answ.setText(fold_array_answ.numbers[list.get(1)]);
                            btn3_fold_answ.setText(fold_array_answ.numbers[list.get(2)]);*/
                            // вкулючаем обратно кнопки
                            btn2_fold_answ.setEnabled(true);
                            btn3_fold_answ.setEnabled(true);
                        }

                    }

                }
                return true;
            }
        });

        btn2_fold_answ.setOnTouchListener(new View.OnTouchListener() {
            @SuppressLint("ClickableViewAccessibility")
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                //блокирую другие кнопки

                if (event.getAction() == MotionEvent.ACTION_DOWN) {
                    btn1_fold_answ.setEnabled(false);
                    btn3_fold_answ.setEnabled(false);

                } else if (event.getAction() == MotionEvent.ACTION_UP) {
                    // если отпустили палец
                    if(numLeft + numRight == list.get(1)){
                        if(count < 20){
                            count+=1;
                        }
                        // закрашываю прогресс серым цветом
                        for(int i = 0; i < 20;i++){
                            TextView tv = findViewById(fold_progress[i]);
                            tv.setBackgroundResource(R.drawable.style_points);
                        }
                        // определяю правильные ответы и закрышиваю зелёным
                        for(int i = 0; i < count; i ++){
                            TextView tv = findViewById(fold_progress[i]);
                            tv.setBackgroundResource(R.drawable.style_points_green);
                        }
                    }else
                    {
                        // если левая картинка меньше и ответ неверный
                        if(count > 0){
                            count -= 1;
                        }
                    }
                    // при неверном ответе отнимается от шкалы одна единица и закрашивается обратно в серый
                    for(int i = 0; i < 19;i++){
                        TextView tv = findViewById(fold_progress[i]);
                        tv.setBackgroundResource(R.drawable.style_points);
                    }
                    // определяю правильные ответы и закрышиваю зелёным
                    for(int i = 0; i < count; i ++){
                        TextView tv = findViewById(fold_progress[i]);
                        tv.setBackgroundResource(R.drawable.style_points_green);
                    }
                    if(count == 20){
                        //выход из уровня
                    }else{
                        // палец отпустили значит надо генерить новые числа, но если ошибка в первом ответе, тогда надо ждать пока будет правильно

                        if(count < 1){
                            btn1_fold_answ.setEnabled(true);
                            btn3_fold_answ.setEnabled(true);
                            // выводится высплывающее сообщение " попробуй ещё раз"
                            int time = 1000;
                            Toast toast = Toast.makeText(getApplicationContext(),"Попробуй ещё раз",Toast.LENGTH_LONG);
                            toast.setGravity(Gravity.CENTER,0,0);
                            toast.show();

                        }
                        else {
                            numLeft = fold_random.nextInt(10);// генерирую случайное число
                            img_left.setImageResource(fold_array.images1[numLeft]);// достаю картинку
                            //text_left.setText(array.texts1[numLeft]); // для изменения текста под картинкой

                            //Для правой картинки цифры
                            numRight = fold_random.nextInt(10);
                            img_right.setImageResource(fold_array.images1[numRight]);

                            /*ArrayList<Integer> list = new ArrayList<Integer>();
                            for (int i = (numLeft+numRight) - 1 ; i<=(numLeft+numRight) +1 ; i++) {
                                list.add(new Integer(i));
                            }
                            Collections.shuffle(list);
                            btn1_fold_answ.setText(fold_array_answ.numbers[list.get(0)]);
                            btn2_fold_answ.setText(fold_array_answ.numbers[list.get(1)]);
                            btn3_fold_answ.setText(fold_array_answ.numbers[list.get(2)]);*/
                            // вкулючаем обратно кнопки
                            btn1_fold_answ.setEnabled(true);
                            btn3_fold_answ.setEnabled(true);
                        }

                    }

                }
                return true;
            }
        });

        btn3_fold_answ.setOnTouchListener(new View.OnTouchListener() {
            @SuppressLint("ClickableViewAccessibility")
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                //блокирую другие кнопки

                if (event.getAction() == MotionEvent.ACTION_DOWN) {
                    btn2_fold_answ.setEnabled(false);
                    btn1_fold_answ.setEnabled(false);

                } else if (event.getAction() == MotionEvent.ACTION_UP) {
                    // если отпустили палец
                    if(numLeft + numRight == list.get(2)){
                        if(count < 20){
                            count+=1;
                        }
                        // закрашываю прогресс серым цветом
                        for(int i = 0; i < 20;i++){
                            TextView tv = findViewById(fold_progress[i]);
                            tv.setBackgroundResource(R.drawable.style_points);
                        }
                        // определяю правильные ответы и закрышиваю зелёным
                        for(int i = 0; i < count; i ++){
                            TextView tv = findViewById(fold_progress[i]);
                            tv.setBackgroundResource(R.drawable.style_points_green);
                        }
                    }else
                    {
                        // если левая картинка меньше и ответ неверный
                        if(count > 0){
                            count -= 1;
                        }
                    }
                    // при неверном ответе отнимается от шкалы одна единица и закрашивается обратно в серый
                    for(int i = 0; i < 19;i++){
                        TextView tv = findViewById(fold_progress[i]);
                        tv.setBackgroundResource(R.drawable.style_points);
                    }
                    // определяю правильные ответы и закрышиваю зелёным
                    for(int i = 0; i < count; i ++){
                        TextView tv = findViewById(fold_progress[i]);
                        tv.setBackgroundResource(R.drawable.style_points_green);
                    }
                    if(count == 20){
                        //выход из уровня
                    }else{
                        // палец отпустили значит надо генерить новые числа, но если ошибка в первом ответе, тогда надо ждать пока будет правильно

                        if(count < 1){
                            btn2_fold_answ.setEnabled(true);
                            btn1_fold_answ.setEnabled(true);
                            // выводится высплывающее сообщение " попробуй ещё раз"
                            int time = 1000;
                            Toast toast = Toast.makeText(getApplicationContext(),"Попробуй ещё раз",Toast.LENGTH_LONG);
                            toast.setGravity(Gravity.CENTER,0,0);
                            toast.show();

                        }
                        else {
                            numLeft = fold_random.nextInt(10);// генерирую случайное число
                            img_left.setImageResource(fold_array.images1[numLeft]);// достаю картинку
                            //text_left.setText(array.texts1[numLeft]); // для изменения текста под картинкой

                            //Для правой картинки цифры
                            numRight = fold_random.nextInt(10);
                            img_right.setImageResource(fold_array.images1[numRight]);

                            /*ArrayList<Integer> list = new ArrayList<Integer>();
                            for (int i = (numLeft+numRight) - 1 ; i<=(numLeft+numRight) +1 ; i++) {
                                list.add(new Integer(i));
                            }
                            Collections.shuffle(list);
                            btn1_fold_answ.setText(fold_array_answ.numbers[list.get(0)]);
                            btn2_fold_answ.setText(fold_array_answ.numbers[list.get(1)]);
                            btn3_fold_answ.setText(fold_array_answ.numbers[list.get(2)]);*/
                            // вкулючаем обратно кнопки
                            btn2_fold_answ.setEnabled(true);
                            btn1_fold_answ.setEnabled(true);
                        }

                    }

                }
                return true;
            }
        });
    }
    // системная кнопка назад
    @Override
    public void onBackPressed () {
        try {
            Intent intent = new Intent(FoldLevel1.this, FoldLevels.class);
            startActivity(intent);
            finish();
        } catch (Exception e) {

        }
    }
}
