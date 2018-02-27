package com.example.user.Oyun2048;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.preference.PreferenceManager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Toast;

public class MainActivity extends AppCompatActivity {

    public static final String NAME = "Name";
    private Button startButton , leaderboardButton , exitButton;
    private EditText edtTxt;
    private String name = null;
    private LinearLayout lLayout;
    private ImageView imageView;
    private int width,height;
    private SharedPreferences sharedPreferences;
    private Intent intent;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        init();
        intent = this.getIntent();
        edtTxt.setText(intent.getStringExtra(NAME));

        startHandler();
        leaderBoardHandler();
        exitHandler();

        /*Bu kisimda oyunun daha onceden oynanip oynanmadiginin kontrolu gerceklestiriliyor ve eger oyun daha onceden oyun oynanmıs
        ise direk oyun ekranina gecis islemi gerceklestiriliyor.*/
        if((sharedPreferences.getBoolean("isContinue",false))&&(intent.getBooleanExtra("Al",true))&&(intent.getBooleanExtra("BACK",true)))
        {
            intent = new Intent(MainActivity.this,GameScreen.class);
            startActivity(intent);
        }
    }

    /*İlklendirme islemlerinin gerceklestigi yerdir.*/
    public void init()
    {
        startButton = (Button)findViewById(R.id.start_button);
        leaderboardButton = (Button)findViewById(R.id.leaderboard);
        exitButton = (Button)findViewById(R.id.exit_button);
        edtTxt = (EditText)findViewById(R.id.editText);
        lLayout = (LinearLayout) findViewById(R.id.lLayout);
        imageView = (ImageView)findViewById(R.id.imgView);
        sharedPreferences = PreferenceManager.getDefaultSharedPreferences(this);
    }

    /*Program calisirken yerlestirilecek olan image'in boyutlarinin belirlenmesi amaciyla bu method kullanilmistir.
    @param hasFocus : Ekranda gerceklesen degisiklikler sonucu true degeri alir.*/
    @Override
    public void onWindowFocusChanged(boolean hasFocus) {
        super.onWindowFocusChanged(hasFocus);
        width = imageView.getWidth();
        height = imageView.getHeight();
        Drawable drawable = getResources().getDrawable(R.drawable.ana_ekran);
        Bitmap b = ((BitmapDrawable)drawable).getBitmap();
        Bitmap bitmapResized = Bitmap.createScaledBitmap(b, width, height, false);
        drawable = new BitmapDrawable(getResources(), bitmapResized);
        imageView.setImageDrawable(drawable);
    }

    public Context MainActivity()
    {
        return this;
    }

    /*Oyunu oynayan kisinin start butona basmasi durumunda oyun calismaya baslar ve oyun ekrani goruntulenir.*/
    public void startHandler()
    {
        startButton.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v)
            {
                name = edtTxt.getText().toString();
                if((name == null)||(name.equals("")))
                {
                    //Kisi eger isim girmemisse kullaniciya bir toast mesaji verdirilir.
                    Toast.makeText(MainActivity.this,R.string.Warn,Toast.LENGTH_SHORT).show();
                }
                else
                {
                    //Oyunun oynanacagi activitye gecis islemi gerceklestiriliyor.
                    Intent i = new Intent(MainActivity.this,GameScreen.class);
                    i.putExtra(NAME,name);
                    startActivity(i);
                }
            }
        });
    }

    /*Kullanicinin puan tablosu yani leaderboard butonuna basmasi sonucunda calisacak olan kisimdir*/
    public void leaderBoardHandler()
    {
        leaderboardButton.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v)
            {
                /*Puan tablosunun yer aldigi activitye gecis islemi gerceklestiriliyor.Bu islem sirasinda kullanici eger bir
                isim girmis ise bu degerin korunmasi amaciyla diger activitye gonderiliyor.*/
                name = edtTxt.getText().toString();
                intent = new Intent(MainActivity.this,LeaderBoard.class);
                intent.putExtra(NAME,name);
                startActivity(intent);
            }
        });
    }

    /*Giris ekraninda bulunan exit tusuna basilmasi durumunda gerceklesecek olan kisimdir*/
    public void exitHandler()
    {
        exitButton.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v)
            {
                AlertDialog.Builder alert = new AlertDialog.Builder(MainActivity.this, AlertDialog.THEME_TRADITIONAL);
                alert.setMessage("Do you want to exit the game?");

                alert.setNegativeButton("No",new DialogInterface.OnClickListener(){
                    @Override
                    public void onClick(DialogInterface dialog,int which)
                    {
                        //Kullanıcı çıkmak istemediğini bildiren tuşa bastığı zaman kullanıcı ana ekranda kalmaya devam edecek.
                    }
                });
                //Kullanici cikmak istemesi durumunda uygulama kapatilarak(arka plandada)cikis islemi gerceklestirilecek.
                alert.setPositiveButton("Yes",new DialogInterface.OnClickListener(){
                    @Override
                    public void onClick(DialogInterface dialog,int which)
                    {
                        pressExit();
                    }
                });
                alert.setCancelable(false);
                alert.show();
            }
        });
    }

    /*Android device'da bulunan back buttona basilmasi durumunda gerceklesecek olan durumu gosteren method*/
    @Override
    public void onBackPressed()
    {
        pressExit();
    }

    /*Back button'a basilmasi durumunda kullanilacak olan method. Bu method ile programin arka plandanda silinmesi gerceklestirilir*/
    public void pressExit()
    {
        intent = new Intent(Intent.ACTION_MAIN);
        intent.addCategory(Intent.CATEGORY_HOME);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        startActivity(intent);
    }
}