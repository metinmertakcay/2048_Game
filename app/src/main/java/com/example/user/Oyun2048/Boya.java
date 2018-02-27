package com.example.user.Oyun2048;

import android.content.Context;

/*Bu sinif oyun ekraninda bulunan karolarin boyanmasi icin olusturulmus olan siniftir*/
public class Boya {

    private Context context;
    private int codeNumber;

    public Boya(Context context)
    {
        this.context = context;
    }

    /*Bu metod cardlara ait renklerin degistirilmesi icin kullanilmistir
    @param card : Rengi kontrol edilecek olan nesne. Bu nesnenin sahip oldugu sayisal deger karsilastirmada kullanilir.
    @return codenumber : Bu deger karonun boyanacak olan renginin id degeri dondurulur*/
    public void boya(Cards card)
    {
        codeNumber = 0;
        if(card.getNumber() == 0)
        {
            codeNumber = context.getResources().getColor(R.color.Tahta);
        }
        else if(card.getNumber() == 2)
        {
            codeNumber = context.getResources().getColor(R.color.Two);
        }
        else if(card.getNumber() == 4)
        {
            codeNumber = context.getResources().getColor(R.color.Four);
        }
        else if(card.getNumber() == 8)
        {
            codeNumber = context.getResources().getColor(R.color.Eight);
        }
        else if(card.getNumber() == 16)
        {
            codeNumber = context.getResources().getColor(R.color.sixteen);
        }
        else if(card.getNumber() == 32)
        {
            codeNumber = context.getResources().getColor(R.color.thirty_two);
        }
        else if(card.getNumber() == 64)
        {
            codeNumber = context.getResources().getColor(R.color.sixty_four);
        }
        else if(card.getNumber() == 128)
        {
            codeNumber = context.getResources().getColor(R.color.one_hundred_twenty_eight);
        }
        else if(card.getNumber() == 256)
        {
            codeNumber = context.getResources().getColor(R.color.two_hundred_fifty_six);
        }
        else if(card.getNumber() == 512)
        {
            codeNumber = context.getResources().getColor(R.color.five_hundred_twelve);
        }
        else if(card.getNumber() == 1024)
        {
            codeNumber = context.getResources().getColor(R.color.one_tousand_twenty_four);
        }
        else if(card.getNumber() == 2048)
        {
            codeNumber = context.getResources().getColor(R.color.two_tausand_forty_eight);
        }
        else if(card.getNumber() == 4096)
        {
            codeNumber = context.getResources().getColor(R.color.four_tausand_ninety_six);
        }
        else if(card.getNumber() == 8192)
        {
            codeNumber = context.getResources().getColor(R.color.s8192);
        }
        else if(card.getNumber() == 16384)
        {
            codeNumber = context.getResources().getColor(R.color.s16384);
        }
        else if(card.getNumber() == 32768)
        {
            codeNumber = context.getResources().getColor(R.color.s32768);
        }
        else if(card.getNumber() == 65536)
        {
            codeNumber = context.getResources().getColor(R.color.s65536);
        }
        else if(card.getNumber() == 131072)
        {
            codeNumber = context.getResources().getColor(R.color.s131072);
        }
        card.setBackgroundColors(codeNumber);
    }
}