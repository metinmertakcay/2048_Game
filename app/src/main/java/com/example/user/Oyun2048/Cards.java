package com.example.user.Oyun2048;

import android.content.Context;
import android.support.annotation.AttrRes;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.view.Gravity;
import android.widget.FrameLayout;
import android.widget.TextView;

/*Bu class'in oluşturulma amaci oyun ekraninda bulunan karolarin oluşturulmasinin sağlanmasidir.Bu karolarin iclerinde kendilerine
ait sayilar bulunmaktadir. FrameLayout isimli ViewGroup'u extends etmistir. FrameLayout tercih edilmesinin sebebi karolarin
ekranda daha duzgun bir sekilde gosterilmek istenmektedir.
 */
public class Cards extends FrameLayout{

    private int number;             //Sayilarin numerik degerleri
    private TextView textView;      //Sayilarin gozukecegi ve bu sayilarin yerlestirilecegi view turevi.

    /*Bu sinifa ait constructor. Constructor icerisinde sayinin yerlestirilecegi textViev ilklendiriliyor ve ozellik ataniyor
    Bu textView'a yerlesilecek sayinin boyutu , arkaplan rengini , sayinin yerlesme seklini ve sayinin rengi gibi ozellikler
    belirlenmis ve atanmistir.*/
    public Cards(Context context) {
        super(context);
        init();
    }

    public Cards(@NonNull Context context, @Nullable AttributeSet attrs, @AttrRes int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init();
    }

    public Cards(@NonNull Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    //İlklendirme islemlerinin gerceklestigi kisimdir*/
    public void init()
    {
        textView = new TextView(getContext());
        textView.setTextSize(32);
        textView.setBackgroundColor(getResources().getColor(R.color.Tahta));
        textView.setGravity(Gravity.CENTER);
        textView.setTextColor(getResources().getColor(R.color.Black));
        LayoutParams layoutParams = new LayoutParams(-1,-1);
        layoutParams.setMargins(10,10,10,10);
        addView(textView,layoutParams);
    }

    /*Diger siniflardan number degiskenine erismek icin yazilmis get metodu*/
    public int getNumber()
    {
        return number;
    }

    /*Degeri diger siniflarda belirlenmis olan karoya ilgili number degerini parametre alarak atama islemini gerceklestiriyor
    Eger atanan deger 0'a esit ise bu sayinin ekranda gorunmemesi icin kontrol islemi de gerceklestirilmistir
    @param number : Karonun icine yerlestirilecek olan deger*/
    public void setNumber(int number) {
        this.number = number;
        if (number == 0) {
            textView.setText("");
        } else {
            textView.setText(number + "");
        }
    }

    /*Bu fonksiyon iki kartta bulunan sayiyi karsilastirma islemini gerceklestirir
     @param o : Bu parametre Cards tipindedir ve icerisinde bulunan getNumbe() metodu ile icerisinde bulunan sayi elde edilir
     @return true: Eger iki sayinin numerik degeri de ayni ise dondurulecek olan deger
     @return false : Eger iki sayinin degeri ayni degil ise dondurulecek olan degerdir.*/
    public boolean equality(Cards o)
    {
        if(getNumber()-o.getNumber() == 0) {
            return true;
        }
        else {
            return false;
        }
    }

    /*Bu fonksiyon aldigi parametreye gore kartın arka plan rengini degistirmeyi saglar
     @param colorCode : Yapilacak boyanin renginin integer degeri*/
    public void setBackgroundColors(int colorCode)
    {
        textView.setBackgroundColor(colorCode);
    }
}