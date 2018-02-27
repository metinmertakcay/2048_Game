package com.example.user.Oyun2048;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Point;
import android.os.Build;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.view.Gravity;
import android.view.View;
import android.widget.Button;
import android.widget.GridLayout;
import android.widget.LinearLayout;
import android.widget.TextView;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import static com.example.user.Oyun2048.MainActivity.NAME;

public class GameScreen extends Activity {
    private static final String FILE_NAME = "Undo.txt", BEST_SCORE = "Best score" , PAUSED_FILE = "paused_file.txt", STORE = "store.txt";
    private static boolean ISBACK = false;
    private static GameScreen gameScreen = null;
    public static boolean MESSAGE = false;
    private Button buttonReset , buttonBackToMenu , buttonUndo;
    private static ArrayList<String> arrayList;
    private GridLayout gridLayout;
    private TextView tvScore, tvBestScore;
    private Intent in , intent;
    private long score, backScore;
    private String name ;
    private DatabaseHelpers databaseHelpers;
    private File file;
    private LinearLayout linearLayoutGame;
    private int width,height;
    private Cards cards[][] = new Cards[4][4] , card;
    private List<Point> point = new ArrayList<Point>();
    private int i , j , k , x , y , number;
    private Random random;
    private Point usingPoint;
    private boolean isContinue , isMove , isMovementOccur , isFirst = true;
    private Boya boya;
    private SharedPreferences sharedPreferences;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_game);

        in = getIntent();
        //Diger activityiden egerisim degeri gonderilmis ise isim degeri aliniyor.
        if(in.getStringExtra(NAME) != null)
        {
            name = in.getStringExtra(NAME);
        }
        else
        {
            name = "";
        }

        init();
        resetHandler();
        backToMenuHandler();
        undoHandler();
    }

    public GameScreen()
    {
        gameScreen = this;
    }

    public static GameScreen getGameScreen()
    {
        return gameScreen;
    }

    /*İlklendirme islemleri gerceklestiriliyor*/
    public void init()
    {
        buttonReset = (Button)findViewById(R.id.buttonReset);
        buttonBackToMenu = (Button)findViewById(R.id.buttonBackToMenu);
        buttonUndo = (Button)findViewById(R.id.buttonUndo);
        tvScore = (TextView)findViewById(R.id.Scor);
        tvBestScore = (TextView)findViewById(R.id.bScore);
        gridLayout = (GridLayout) findViewById(R.id.gridLayout);
        linearLayoutGame = (LinearLayout)findViewById(R.id.linearLayoutGame);
        boya = new Boya(this);
        sharedPreferences = PreferenceManager.getDefaultSharedPreferences(this);
    }

    /*Oyuncunun oyunu tekrar baslatmasi durumunda yaptigi skor sifirlaniyor*/
    public void clearScore()
    {
        score = 0;
        showScore();
    }

    /*Oyuncunun o anda sahip oldugu skor degeri gosteriliyor*/
    public void showScore()
    {
        tvScore.setTextSize(15);
        tvScore.setGravity(Gravity.CENTER);
        tvScore.setText(score+"");
    }

    /*Oyunda gerceklesen her puan durumu oyuncunun sahip oldugu puan durumuna ekleniyor.Ayrıca bu ekleme islemi sonucu
    yapilan en yuksek skor ile kullanicinin sahip oldugu skor karsilastirilir ve niyahetinde skor ekrana gosterilir.
    @param scr : Kullanicinin yaptigi hamle karsiliginda elde ettigi scoru temsil eder.*/
    public void addScore(long scr)
    {
        score += scr;
        bestScore();
        showScore();
    }

    /*Oyunu oynayan kisinin hamle geri almasi durumunda elde ettigi skor bir onceki hamlede kazandıgı deger kadar azaltilir ve
    ekranda dogru skor gosterilir.*/
    public void showCorrectScore()
    {
        this.score = backScore;
        showScore();
    }

    /*Oyuncunun yapmis oldugu en yuksek deger android programlamadaki shared preferences kavrami ile saklanmistir. Bu yapida
    saklanmasinin sebebi surekli olarak en iyi skora erisilip kontrol edilmesi ve gerekirse degistirilmesi gereklidir.
    Bu islem bu metodda shared preferences yapisinin kullanilmasi ile gerceklestirilmistir.*/
    public void bestScore()
    {
        long insideScore;   //En yuksek skoru ifade eder.
        //Shared preferences yapisi olusturuluyor.
        SharedPreferences sharedPreferences = this.getPreferences(Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        //Eger oyun daha onceden oynanmis ise if blogunun  icerisine girilecektir. Ancak oyunun ill defa baslamasi durumunda else blogu calisir.
        if(sharedPreferences.contains(BEST_SCORE)) {
            insideScore = sharedPreferences.getLong(BEST_SCORE, 0);
        }
        else
        {
            insideScore = 0;
        }
        //Suanki skorla bizim daha once elde ettigimiz skor karsilastiriliyor. Daha onceden elde edilen skor dusuk ise yeni skor yerlestiriliyor.
        if(insideScore < score)
        {
            editor.putLong(BEST_SCORE,score);
            tvBestScore.setText(score+"");
        }
        else
        {
            tvBestScore.setText(insideScore+"");
        }
        tvBestScore.setTextSize(15);
        tvBestScore.setGravity(Gravity.CENTER);
        editor.commit();
    }

    //Oyun bitiminden sonra kisinin isim bilgisi ve elde ettigi skor databse'e yaziliyor.
    public void setScoreDatabase()
    {
        databaseHelpers = new DatabaseHelpers(this);
        databaseHelpers.insertData(name,score+"");
    }

    //Bu metod oyunun tekrardan baslatilmak istenip istenmedigi sorusu kullaniciya veriliyor ve islem gerceklestiriliyor.
    public void resetHandler()
    {
        buttonReset.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v)
            {
                AlertDialog.Builder alert = new AlertDialog.Builder(GameScreen.this,AlertDialog.THEME_TRADITIONAL);
                alert.setMessage("Do you want to resart this game?");

                alert.setNegativeButton("No", new DialogInterface.OnClickListener(){
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {

                    }
                });
                alert.setPositiveButton("Yes",new DialogInterface.OnClickListener(){
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        //Burada aynı kod parçasının herhangi bir değişiklik olmadan yani en başından başlamasını sağlar.
                        //Gerekli ilklendirmeler tekrardan yapiliyor.
                        ISBACK = false;
                        isFirst = true;
                        isContinue = false;
                        score = 0;
                        recreateActivity(GameScreen.this);
                    }
                });
                alert.setCancelable(false);
                alert.show();
            }
        });
    }

    /*Oyunun tekrardan baslatilmasini saglayan metoddur.
    @param activity : Tekrardan baslayacak olan activityi ifade eder.*/
    public void recreateActivity(Activity activity) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB) {
            activity.recreate();
        }
        else {
            Intent intent = activity.getIntent();
            intent.addFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION);
            activity.finish();
            activity.overridePendingTransition(0, 0);
            activity.startActivity(intent);
            activity.overridePendingTransition(0, 0);
        }
    }

    //Ana menuye donmek istenmedigini soran bir diyalog ekrani gosterilir.
    public void backToMenuHandler()
    {
        buttonBackToMenu.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v)
            {
                AlertDialog.Builder alert = new AlertDialog.Builder(GameScreen.this,AlertDialog.THEME_TRADITIONAL);
                alert.setMessage("Do you want to back to menu?");

                alert.setNegativeButton("No", new DialogInterface.OnClickListener(){
                    @Override
                    public void onClick(DialogInterface dialogInterface, int t) {

                    }
                });

                alert.setPositiveButton("Yes",new DialogInterface.OnClickListener(){
                    @Override
                    public void onClick(DialogInterface dialogInterface, int t) {
                        //Burada aynı kod parçasının herhangi bir değişiklik olmadan yani en başından başlamasını sağlar.
                        ISBACK = false;
                        intent = new Intent(GameScreen.this,MainActivity.class);
                        intent.putExtra(NAME,name);
                        intent.putExtra("Al",false);
                        startActivity(intent);
                    }
                });
                alert.setCancelable(false);
                alert.show();
            }
        });
    }

    /*Bir hamle geri alinmak istenmesi durumunda harekete gececek kod parcasidir.*/
    public void undoHandler()
    {
        buttonUndo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if((ISBACK)&&(!isFirst)) {
                    ISBACK = false;
                    MESSAGE = true;
                    //Oyun ekraninin bir onceki hamleye gelebilmesi icin dosyaya kaydedilen bir onceki hamle oyun ekranina yerlestirilir.
                    loadFile(FILE_NAME);
                    startGame();
                }
            }
        });
    }

    /*Bu metod oyun ekraninin dosyaya kaydedilmesi islemi gerceklestirilir.
    @param cards : Oyun ekraninda bulunan kartlar
    @param file_name : Kartlarin durumunun kayit edilecegi dosyanin adidir.*/
    public void saveFile(Cards[][] cards,String file_name)
    {
        //Geriye alma isleminin yapilabilecegini ifade eden degisken true degerine aktarildi.
        ISBACK = true;
        file = new File(this.getFilesDir(), file_name);
        try
        {
            FileOutputStream fos = new FileOutputStream(file);
            OutputStreamWriter osw = new OutputStreamWriter(fos);
            for(int i=0 ; i<4 ; i++)
            {
                for(int j=0 ; j<4 ; j++)
                {
                    //Dosyaya yazma islemi gerceklestiriliyor.
                    osw.write(cards[i][j].getNumber()+"\n");
                }
            }
            osw.write(score+"\n");
            osw.close();
            fos.close();
        }
        catch (IOException e)
        {
            e.printStackTrace();
        }
    }

    /*Dosyaya yazilma islemi gerceklestirilen kartlarin durumu tekrardan oyun tahtasina alinabilmesi icin bir arrayliste yerlestirilme
    islemi gerceklestiriliyor.
    @param file_name : Kartlarin durumunun yuklu oldugu dosyanin ismi
    @return arrayList : Kartlarin durum bilgisini icermektedir.*/
    public ArrayList<String> loadFile(String file_name)
    {
        //Load isleminden sonra bir kez daha geri alma ozelligi ortadan kaldiriliyor.
        ISBACK = false;
        String line = "";
        arrayList = new ArrayList<String>();
        file = new File(this.getFilesDir(), file_name);

        try
        {
            FileInputStream fos = new FileInputStream(file);
            InputStreamReader isr = new InputStreamReader(fos);
            BufferedReader br = new BufferedReader(isr);
            //Satir satir arrayListe ekleme islemi gerceklestiriliyor okunacak deger kalmayıncaya kadar.
            while(line!=null)
            {
                line = br.readLine();
                if (line != null)
                {
                    arrayList.add(line);
                }
            }
            backScore = Long.parseLong(arrayList.get(16));
            br.close();
            isr.close();
            fos.close();
        }
        catch (IOException e)
        {
            e.printStackTrace();
        }
        return arrayList;
    }

    /*Ekranda gerceklesecek degisiklik ile kartlarin olmasi gereken genislik ve yukseklik degeri belirleniyor.
    @param hasFocus : Ekranin degisip degismedigini bildirecek boolean degisken*/
    @Override
    public void onWindowFocusChanged(boolean hasFocus)
    {
        if(hasFocus) {
            super.onWindowFocusChanged(hasFocus);
            width = linearLayoutGame.getWidth();
            height = linearLayoutGame.getWidth();
            width = (width) / 4;
            height = (height) / 4;
            //Oyunun ilk acilmasi durumunu ifade eder. Eger ilk defa acildiysa asagidaki islemlerle devam edilir.
            if (isFirst) {
                addCards();
                startGame();
                isFirst = false;
            }
        }
    }

    /*Bu fonksiyonda daha onceden belirlenen kart genisligi ve yuksekliginde kartlar olusturuluyor.*/
    public void addCards()
    {
        /*Toplamda 16 tane kart olusturulacaktir*/
        for(i=0 ; i<4 ; i++)
        {
            for(j=0 ; j<4 ; j++)
            {
                card = new Cards(this);
                gridLayout.addView(card,width,height);  //Gridlayouta veilen boyutlarda olusturulan kart nesneleri ekleniyor.
                cards[i][j] = card;                     //Olusturulan kart nesneleri kart matrisine ekleniyor.
            }
        }
    }

    /*Bu fonksiyonda belirlenmis oranlarda 2 ve 4 sayilarinin eklenmesi saglanilir.*/
    public void addRandomNumber()
    {
        point.clear();
        for(x=0 ; x<4 ; x++)
        {
            for(y=0 ; y<4 ; y++)
            {
                if(cards[x][y].getNumber() == 0)
                {
                    //Butun kart ciftleri pointe yani arrayListe ekleniyor.
                    point.add(new Point(x,y));
                }
            }
        }
        random = new Random();
        //Random sayi atama islemi gerceklestiriliyor.
        number = random.nextInt(point.size());
        //Random olarak belirlenen sayinin yer aldigi karo listeden cikariliyor.
        usingPoint = point.remove(number);
        //Random olarak atanacak olan sayi degerinin belirlenmesi islemi gerceklestiriliyor. 2 veya 4 degeri.
        cards[usingPoint.x][usingPoint.y].setNumber(Math.random() > 0.1 ? 2:4);
        //Belirlenen sayi degerinin ve yerlestirilen karonun boyama islemi gerceklestiriliyor.
        boya.boya(cards[usingPoint.x][usingPoint.y]);
    }

    //Bu metod oyunun baslatilmasini saglamaktadir.
    public void startGame()
    {
        /*Oyun daha onceden oynanmis ise en yuksek elde edilen sayi ekrana best skore altina yazilir.*/
        bestScore();
        //Eger daha onceden oyun oynanmis ise oyun kaldigi yerden oynanmaya devam ediyor.
        if((sharedPreferences.getBoolean("isContinue",false))&&((sharedPreferences.getString("name",null).equals(name))||(name.equals(""))))
        {
            //Saklanmis olan karo degerleri tekrardan yukleniyor.
            ArrayList<String> list = loadFile(STORE);
            int k = 0;
            for (i=0; i<4; i++) {
                for (j=0; j<4; j++) {
                    cards[i][j].setNumber(Integer.parseInt(list.get(k)));
                    boya.boya(cards[i][j]);
                    k++;
                }
            }
            score = sharedPreferences.getLong("score",0);
            name = sharedPreferences.getString("name",null);
            showScore();
        }
        //Oyun ilk defa oynanmaya baslandigi zaman calisacak olan kisimdir.
        else if(!(MESSAGE)) {
            clearScore();
            for (i=0; i<4; i++) {
                for (j=0; j<4; j++) {
                    //Butun kartlarin baslangic degeri olarak 0 olarak atandi.
                    cards[i][j].setNumber(0);
                    boya.boya(cards[i][j]);
                }
            }
            /*Oyun basladigi zaman 2 adet bilgisayar tarafından atanmış olan sayılar oluşturulur*/
            addRandomNumber();
            addRandomNumber();
        }
        else
        {
            showCorrectScore();
            k = 0;
            for(i=0 ; i<4 ; i++)
            {
                for(j=0 ; j<4 ; j++)
                {
                    cards[i][j].setNumber(Integer.parseInt(arrayList.get(k)));
                    boya.boya(cards[i][j]);
                    k++;
                }
            }
            MESSAGE = false;
            checkGameFinish();
        }
        isContinue = true;
    }

    /*Oyunda yapılabilecek baska bir hamlenin olup olmadigini bu kisimda kontrol ediyoruz*/
    public void checkGameFinish()
    {
        //Devam edip etmediginin kontrolunun yapilacagi degiskendir.
        isContinue = false;
        i=0;
        j=0;

        //Yapilacak bir hamlenin olup olmadigininkontrolu gerceklestiriliyor.
        while((i<4)&&(!isContinue))
        {
            j = 0;
            while((j<4)&&(!isContinue))
            {
                if ((cards[i][j].getNumber() == 0)||((i<3) && (cards[i][j].equality(cards[i+1][j]))) ||
                        ((i>0) && (cards[i][j].equality(cards[i-1][j]))) || ((j>0) && (cards[i][j].equality(cards[i][j-1])))
                        || ((j<3)&&(cards[i][j].equality(cards[i][j+1])))) {

                    isContinue = true;
                }
                j++;
            }
            i++;
        }
        /*Eger hareket edecek yer kalmamissa kullaniciya hareket edemeyecegini belirten bir diyalog ekrani belirir.*/
        if(!isContinue)
        {
            setScoreDatabase();
            ISBACK = false;
            AlertDialog.Builder alert = new AlertDialog.Builder(this);
            alert.setMessage("You do not move anymore");
            alert.setCancelable(false);

            //Tekrardan yeni bir oyunun baslatilmasi icin kullanilir.
            alert.setPositiveButton("Start a new game",new DialogInterface.OnClickListener(){
                @Override
                public void onClick(DialogInterface dialogInterface,int x)
                {
                    intent = getIntent();
                    finish();
                    startActivity(intent);
                }
            });

            //Ana menuye geri donme islemi gerceklestirilir.
            alert.setNegativeButton("Back menu",new DialogInterface.OnClickListener(){
                @Override
                public void onClick(DialogInterface dialogInterface,int x)
                {
                    intent = new Intent(GameScreen.this,MainActivity.class);
                    intent.putExtra(NAME,name);
                    startActivity(intent);
                }
            });
            alert.show();
        }
    }

    /*Sola hareket sonucunda yapilacak olan islemler bu kisimda gerceklestirilir.*/
    public void left() {
        i = 0;
        j = 0;
        isMove = false;

        //Hareket islemi gerceklesmeden once o anki kartlarin durumu dosyada tutuluyor.
        saveFile(cards,FILE_NAME);
        while (i < 4) {
            j = 0;
            while (j < 4) {
                k = j + 1;
                isMovementOccur = false;
                while ((k < 4) && (!isMovementOccur)) {
                    if (cards[i][j].getNumber() != 0) {
                        if (cards[i][k].getNumber() != 0) {
                            if (cards[i][j].getNumber() == cards[i][k].getNumber()) {
                                cards[i][j].setNumber(cards[i][k].getNumber() * 2);
                                addScore(cards[i][j].getNumber());
                                cards[i][k].setNumber(0);
                                boya.boya(cards[i][j]);
                                boya.boya(cards[i][k]);
                                isMovementOccur = true;
                                isMove = true;
                            } else {
                                isMovementOccur = true;
                            }
                        }
                    } else {
                        if (cards[i][k].getNumber() != 0) {
                            cards[i][j].setNumber(cards[i][k].getNumber());
                            cards[i][k].setNumber(0);
                            boya.boya(cards[i][j]);
                            boya.boya(cards[i][k]);
                            j--;
                            isMovementOccur= true;
                            isMove = true;
                        }
                    }
                    k++;
                }
                j++;
            }
            i++;
        }
        /*Hareket islemi gerceklestirildikten sonra bir tane random olarak deger karolara yerlestiriliyor. Daha sonra oyunun
        bitip bitmedigi yani hareket edilip edilmeyecegi kontrol ediliyor. Bu hareket sonunda score degeri en yuksek skor degerini
        gecmis ise en yuksek skor kismina yerlestirme islemi gerceklesiyor.*/
        if (isMove) {
            addRandomNumber();
            checkGameFinish();
            bestScore();
        }
    }

    //Oyuncunun saga hareket yapmasi sonucu calisacak olan kod kismi
    public void right() {
        i = 0;
        j = 3;
        isMove = false;

        saveFile(cards,FILE_NAME);
        while (i < 4) {
            j = 3;
            while (j >= 0) {
                k = j - 1;
                isMovementOccur = false;
                while ((k >= 0) && (!isMovementOccur)) {
                    if (cards[i][j].getNumber() != 0) {
                        if (cards[i][k].getNumber() != 0) {
                            if (cards[i][j].getNumber() == cards[i][k].getNumber()) {
                                cards[i][j].setNumber(cards[i][k].getNumber() * 2);
                                addScore(cards[i][j].getNumber());
                                cards[i][k].setNumber(0);
                                boya.boya(cards[i][j]);
                                boya.boya(cards[i][k]);
                                isMovementOccur = true;
                                isMove = true;
                            }
                            else {
                                isMovementOccur = true;
                            }
                        }
                    } else {
                        if (cards[i][k].getNumber() != 0) {
                            cards[i][j].setNumber(cards[i][k].getNumber());
                            cards[i][k].setNumber(0);
                            boya.boya(cards[i][j]);
                            boya.boya(cards[i][k]);
                            j++;
                            isMovementOccur = true;
                            isMove = true;
                        }
                    }
                    k--;
                }
                j--;
            }
            i++;
        }
        if (isMove) {
            addRandomNumber();
            checkGameFinish();
            bestScore();
        }
    }

    //Oyuncunun yukari hareket yapmasi sonucu calisacak olan kod kismi
    public void up() {
        i = 0;
        j = 0;
        isMove = false;

        saveFile(cards,FILE_NAME);
        while (j < 4) {
            i = 0;
            while (i < 4) {
                k = i + 1;
                isMovementOccur = false;
                while ((k < 4) && (!isMovementOccur)) {
                    if (cards[i][j].getNumber() != 0) {
                        if (cards[k][j].getNumber() != 0) {
                            if (cards[i][j].getNumber() == cards[k][j].getNumber()) {
                                cards[i][j].setNumber(cards[k][j].getNumber() * 2);
                                addScore(cards[i][j].getNumber());
                                cards[k][j].setNumber(0);
                                boya.boya(cards[i][j]);
                                boya.boya(cards[k][j]);
                                isMovementOccur = true;
                                isMove = true;
                            } else {
                                isMovementOccur = true;
                            }
                        }
                    } else {
                        if (cards[k][j].getNumber() != 0) {
                            cards[i][j].setNumber(cards[k][j].getNumber());
                            cards[k][j].setNumber(0);
                            boya.boya(cards[i][j]);
                            boya.boya(cards[k][j]);
                            i--;
                            isMovementOccur = true;
                            isMove = true;
                        }
                    }
                    k++;
                }
                i++;
            }
            j++;
        }
        if (isMove) {
            addRandomNumber();
            checkGameFinish();
            bestScore();
        }
    }

    /*Oyun ekraninda asagiya dogru hareketin yapilmasi durumunda calisacak olan kisimdir.*/
    public void down()
    {
        i = 0;
        j = 0;
        isMove = false;

        saveFile(cards,FILE_NAME);
        while (j < 4) {
            i = 3;
            while (i >= 0) {
                k = i - 1;
                isMovementOccur = false;
                while ((k >= 0) && (!isMovementOccur)) {
                    if (cards[i][j].getNumber() != 0) {
                        if (cards[k][j].getNumber() != 0) {
                            if (cards[i][j].getNumber() == cards[k][j].getNumber()) {
                                cards[i][j].setNumber(cards[k][j].getNumber() * 2);
                                addScore(cards[i][j].getNumber());
                                cards[k][j].setNumber(0);
                                boya.boya(cards[i][j]);
                                boya.boya(cards[k][j]);
                                isMovementOccur = true;
                                isMove = true;
                            } else {
                                isMovementOccur = true;
                            }
                        }
                    }
                    else
                    {
                        if (cards[k][j].getNumber() != 0) {
                            cards[i][j].setNumber(cards[k][j].getNumber());
                            cards[k][j].setNumber(0);
                            boya.boya(cards[i][j]);
                            boya.boya(cards[k][j]);
                            i++;
                            isMovementOccur = true;
                            isMove = true;
                        }
                    }
                    k--;
                }
                i--;
            }
            j++;
        }
        if (isMove) {
            addRandomNumber();
            checkGameFinish();
            bestScore();
        }
    }

    @Override
    protected void onPause() {
        super.onPause();
        saveFile(cards,PAUSED_FILE);
        saveGame();
    }

    @Override
    protected void onResume() {
        super.onResume();
        loadFile(PAUSED_FILE);
    }

    /*Oyundaki degerlerin oyundan cikilmasi durumuna karsi kaydetme islemi gerceklestiriliyor. Kaydetme isleminden sonra
    oyunda bulunan kartlarin durumuda dosyaya kaydetme islemi gerceklesiyor.*/
    public void saveGame()
    {
        sharedPreferences.edit().putBoolean("isContinue",isContinue).commit();
        sharedPreferences.edit().putString("name",name).commit();
        sharedPreferences.edit().putLong("score",score).commit();
        saveFile(cards,STORE);
    }

    /*Android device'da bulunan back tusunun islevi ortadan kaldiriliyor.*/
    @Override
    public void onBackPressed() {}
}