package com.example.user.Oyun2048;

import android.content.Context;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;
import android.widget.GridLayout;

//Bu class oyun ekraninda bulunan ve oyunun oynanacagi bolum icin olusturulmustur.
public class Screen extends GridLayout {

    private float startX, startY, offsetX, offsetY;

    public Screen(Context context) {
        super(context);
        screenView();
    }

    public Screen(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        screenView();
    }

    public Screen(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        screenView();
    }

    /*Bu kisim kullanicinin saga sola yukari asagi gibi durumlarini belirlemek icin olusturulmustur.Dokunma islemlerini gerceklestirmek
    icin onTouchListener kullanilmistir. Ayrıca bu metodun icerisinde kac tane sutunun olacagi bilgisi de yerlestiriliyor.*/
    private void screenView() {
        //Oyun tahtasinin toplamda 4 adet sutundan olusacagi belirleniyor.
        setColumnCount(4);
        setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                switch (event.getAction()) {
                    case MotionEvent.ACTION_DOWN:
                        startX = event.getX();
                        startY = event.getY();
                        break;
                    case MotionEvent.ACTION_UP:
                        offsetX = event.getX() - startX;
                        offsetY = event.getY() - startY;
                        if (Math.abs(offsetX) > Math.abs(offsetY)) {
                            //Hareketin sola oldugunu gosteren kisim.
                            if (offsetX < -5) {
                                GameScreen.getGameScreen().left();
                            }
                            //Hareketin saga olacagini gosteren kisim
                            else if (offsetX > 5) {
                                GameScreen.getGameScreen().right();
                            }
                        } else {
                            //Hareketin yukari olacagini gosteren kisim
                            if (offsetY < -5) {
                                GameScreen.getGameScreen().up();
                            }
                            //Hareketin asagi olacagini gosteren kisim
                            else if (offsetY > 5) {
                                GameScreen.getGameScreen().down();
                            }
                        }
                        break;
                }
                return true;
            }
        });
    }
}