package com.example.user.Oyun2048;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

/*Bu sinif database islemleri icin olusturulmustur*/
public class DatabaseHelpers extends SQLiteOpenHelper {

    private static final String DATABASE_NAME = "leader";       //Olusturulacak olan database'in adi
    private static final String TABLE_NAME = "leader_table";    //Database'de bulunan tablonun ismi
    private static final String COL_1 = "ID";       //Tabloda bulunan birinci kolonu gosterecek olan kolonun adı
    private static final String COL_2 = "NAME";     //Tabloda bulunan ikinci kolonu gosterecek olan kolonun adı
    private static final String COL_3 = "SCORE";    //Tabloda bulunan ucuncu kolonu gosterecek olan kolonun adı
    private SQLiteDatabase sql;
    private Cursor cursor;
    private ContentValues contentValues;

    /*Bu sinifa ait olan costructor. Constructor cagrilmasi ile birlikte belirlenen isimde ve versiyon degeri 1 olarak belirlenmis
    degerdir.*/
    public DatabaseHelpers(Context context)
    {
        super(context,DATABASE_NAME,null,1);
    }

    @Override
    public void onCreate(SQLiteDatabase sqLiteDatabase) {
        sqLiteDatabase.execSQL("Create Table "+ TABLE_NAME +" ( "+ COL_1 +" INTEGER PRIMARY KEY AUTOINCREMENT , "+ COL_2
                + " TEXT, "+ COL_3 + " LONG ); ");
    }

    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int i, int i1) {
        sqLiteDatabase.execSQL("DROP TABLE IF EXISTS "+ TABLE_NAME);
        this.onCreate(sqLiteDatabase);
    }

    /*Database'e yeni bir eleman eklemek icin olusturulmus olan fonksiyondur.
    @param name : Eklenen kisinin ismini String biciminde alan degerdir.
    @param score : Kisinin skorunu gosteren degisken
     */
    public void insertData(String name,String score) {
        sql = this.getWritableDatabase();                //Yazmak icin olusturulmus olan database
        contentValues = new ContentValues();             //Database icine yazilacak deger icin olusturulmus degisken
        contentValues.put(COL_2, name);
        contentValues.put(COL_3, score);
        sql.insert(TABLE_NAME, null, contentValues);     //ContentValues sinifindan olusturulan deger database'e insert ediliyor.
    }

    /*Bu metod database'de bulunan butun verilerin elde edilmesini saglar
    return cursor : Butun verilerin elde edildigi bir yapi geri dondurur.*/
    public Cursor getAllData()
    {
        sql = this.getWritableDatabase();
        cursor = sql.rawQuery("SELECT * FROM "+TABLE_NAME,null);
        return cursor;
    }

    /*Database'den bir elaman silmek amaciyla olusturulmustur.
    @param id : Silinecek olan elemanin id degeri*/
    public void deleteData(String id)
    {
        sql = this.getWritableDatabase();
        sql.delete(TABLE_NAME,"ID = ?",new String[]{id});
    }
}