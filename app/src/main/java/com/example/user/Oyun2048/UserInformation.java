package com.example.user.Oyun2048;

import android.support.annotation.NonNull;

/*Bu sinif oyunu oynayacak kisinin bilgilerini olusturmak ve saklamak amaciyla olusturulmustur*/
public class UserInformation implements Comparable<UserInformation> {

    private int id;
    private String name;
    private long score;

    /*UserInformation sinifina ait olan constructor*/
    public UserInformation(int id,String name,long score)
    {
        this.id = id;
        this.name = name;
        this.score = score;
    }

    /*private turlere ait olan get metotlari*/
    public Long getScore()
    {
        return score;
    }

    public int getId()
    {
        return id;
    }

    public String getName()
    {
        return name;
    }

    /*Karsilastirma islemini yapmak icin kullanilan siniftir*/
    @Override
    public int compareTo(@NonNull UserInformation userInformation) {
        return (int) (userInformation.getScore() - this.getScore());
    }
}