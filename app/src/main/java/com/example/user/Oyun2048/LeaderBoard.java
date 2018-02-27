package com.example.user.Oyun2048;

import android.app.Activity;
import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.GridLayout;
import android.widget.Toast;
import java.util.ArrayList;
import java.util.Collections;
import static com.example.user.Oyun2048.MainActivity.NAME;

/*Oyun tablosunun gosterildigi activitidir*/
public class LeaderBoard extends Activity
{
    private GridLayout gLayout;
    private Button backButton;
    private Intent i;
    private Cursor cursor;
    private DatabaseHelpers databaseHelpers;
    private ArrayList<UserInformation> users;
    private String name;
    private long score;
    private int id , clearId;
    private long min , nowLookingScore;
    private Table table;
    private int height,width,curNumber;
    private boolean firstOpen = true;   //Bu degerin tutulmasinin sebebi sadece ekran acildigi zaman onWindowFocusChange metodu icerisinde gerceklestirilmesidir.

    @Override
    public void onCreate(Bundle saveInstanceState) {
        super.onCreate(saveInstanceState);
        setContentView(R.layout.activity_leaderboards);

        i = this.getIntent();
        LeaderBoard.this.setTitle("Leaderboard");

        init();
        backHandler();
    }

    /*Bu metod ile leader tablosuna yerlestirilecek olan karolarin boyutlarinin ayarlanmasi islemi gerceklestiriliyor.
    @param hasFocus : Ekranda bir degisiklik gerceklesmesi sonucu true degeri alir.*/
    @Override
    public void onWindowFocusChanged(boolean hasFocus)
    {
        super.onWindowFocusChanged(hasFocus);
        if((hasFocus)&&(firstOpen))
        {
            width = gLayout.getWidth();
            height = gLayout.getHeight();
            width = (width) / 3;
            height = (height) / 5;
            createAndShowTable();
            firstOpen = false;
        }
    }

    /*Gerekli ilklendirme islemleri gerceklestiriliyor.*/
    public void init()
    {
        gLayout = (GridLayout)findViewById(R.id.gLayout);
        backButton = (Button)findViewById(R.id.backButton);
    }

    /*Leaderboard kisminda bulunan back butonuna basilmasi durumunda oyuncuya ilk gosterilen ekrana dondurme islemi yapiliyor.*/
    public void backHandler()
    {
        backButton.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v)
            {
                /*Kullanicinin her ihtimale karsi yazmis oldugu isim degerinin kaybolmamasi icin activitiler arasi geciste bu deger
                geri donduruluyor.*/
                Intent m = new Intent(LeaderBoard.this,MainActivity.class);
                m.putExtra(NAME,i.getStringExtra(NAME));
                m.putExtra("BACK",false);
                startActivity(m);
            }
        });
    }

    /*Bu metod Tablonun olusturulmasi ve gosterilmesi islemini gerceklestirir*/
    public void createAndShowTable()
    {
        int i , j , k = 0;
        //Database islemleri icin datavbase sinifindan bir nesne uretiliyor.
        databaseHelpers = new DatabaseHelpers(this);
        //Gerekli veriler databaseHelper nesnesi ile elde ediliyor.
        cursor = databaseHelpers.getAllData();

        //Eger oyun daha onceden oynanmamis ise kullaniciya oyunun daha onceden oynanmadigi mesaji veriliyor.
        if(cursor.getCount() == 0)
        {
            Toast.makeText(this,"You have never played the game",Toast.LENGTH_SHORT).show();
        }
        else
        {
            curNumber = cursor.getCount();
            /*Ekranda en yuksek olan 5 skorun gosterilmesinde karar kilinmistir. Bu yuzden 5ten fazla oyunun oynanmasi durumunda
            biten oyunlardan skoru en dusuk olan degerler belirlenerek silma islemi gerceklestiriliyor.*/
            if (curNumber > 5)
            {
                deleteSomeUser(cursor , databaseHelpers);
            }

            users = new ArrayList<>();
            //Cursor degeri istenen noktaya getiriliyor.
            cursor.moveToFirst();
            k = 0;
            /*DtabaseHelper ile elde edilmis olan cursor teker teker gezilerek oyunu daha onceden oynamis ve puani yuksek olan
            kisiler leader tablosu bolumunde gosterilmesi islemi gerceklestiriliyor.*/
            while(k < curNumber)
            {
                id = Integer.parseInt(cursor.getString(0));
                name = cursor.getString(1);
                score = Long.parseLong(cursor.getString(2));
                users.add(new UserInformation(id,name,score));
                k++;
                cursor.moveToNext();
            }
            //Elde edilen veriler bir arrayListe yerlestiriliyor ve bu arrayListin elemanlari Collections.sort yardimiyla siralama islemi gerceklestiriliyor
            Collections.sort(users);
            for (i = 0; i < curNumber; i++)
            {
                for(j=0 ; j<3 ; j++)
                {
                    table = new Table(this);
                    if (j % 3 == 0)
                    {
                        table.getTextView().setText(i + 1 + "");
                    }
                    else if(j % 3 == 1)
                    {
                        table.getTextView().setText(users.get(i).getName());
                    }
                    else if(j % 3 == 2)
                    {
                        table.getTextView().setText(users.get(i).getScore() + "");
                    }
                    gLayout.addView(table,width,height);
                }
            }
        }
        cursor.close();
    }

    /*Bu method leader tablosundaki istenen verinin(puani en dusuk olanın) silinmesi icin olusturulmustur.
    @param cursor : İlgilenilen degerlerin bulundugu nesne
    @param databaseHelpers : Database islemlerinin gerceklestirildigi sinifin turunde bir nesne, silme islemi icn metoda parametre gecilmistir.*/
    public void deleteSomeUser(Cursor cursor , DatabaseHelpers databaseHelpers) {
        //En yuksek skor degeri bu deger oldugu icin yerlestirilmistir.
        min = 3867396;
        /*Teker teker gezme islemi gerceklestirilerek silinecek olan degerin id degeri elde ediliyor ve bu id degeri ve databaseHelper
        nesnesi kullanilarak silme islemi gerceklestiriliyor.*/
        while (cursor.moveToNext()) {
            id = Integer.parseInt(cursor.getString(0));
            nowLookingScore = Long.parseLong(cursor.getString(2));
            if (min >= nowLookingScore) {
                min = nowLookingScore;
                clearId = id;
            }
        }
        databaseHelpers.deleteData(clearId+"");
    }
}