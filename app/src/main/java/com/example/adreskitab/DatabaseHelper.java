package com.example.adreskitab;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.os.strictmode.SqliteObjectLeakedViolation;

import androidx.annotation.Nullable;

import java.util.ArrayList;

public class DatabaseHelper extends SQLiteOpenHelper {

    public static final String databaseName = "adres.db";
    public static final int databaseVersion = 1;
    public static final String table_Name = "adres";
    public static final String row_ID = "row_id";
    public static final String row_Baslik = "row_baslik";
    public static final String row_Adres = "row_adres";
    public static final String row_Kordinat = "row_kordinat";
    public static final String row_Image = "row_image";



    public DatabaseHelper(@Nullable Context context) {
        super(context, databaseName, null, databaseVersion);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL("CREATE TABLE "+ table_Name + "(" + row_ID + " INTEGER PRIMARY KEY,"
                + row_Baslik + " TEXT,"
                + row_Adres + " TEXT, "
                + row_Kordinat + " TEXT, "
                + row_Image + " BLOB " + ")");
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

    }

    public void dataPut(Adres adres){

        SQLiteDatabase db = this.getWritableDatabase();


        ContentValues cv = new ContentValues();

        cv.put(row_Baslik,adres.adresBaslik);
        cv.put(row_Adres,adres.adresDetay);
        cv.put(row_Kordinat,adres.adresKordinat);
        cv.put(row_Image,adres.adresImage);

        db.insert(table_Name,null,cv);

        db.close();

    }

    public void dataDelete(int id){

        SQLiteDatabase db = this.getWritableDatabase();

        String sid = String.valueOf(id);

        try {

            db.delete(table_Name,row_ID + " =? ",new String[]{sid});

        }catch (Exception e){

        }
        db.close();

    }

    public ArrayList<Adres> dataList(){

        SQLiteDatabase db = this.getReadableDatabase();



        ArrayList<Adres> dataList = new ArrayList<Adres>();

        Cursor cursor = db.query(table_Name,null,null,null,null,null,null);

        while (cursor.moveToNext()){
            Adres data = new Adres(cursor.getInt(0),cursor.getString(1),cursor.getString(2),cursor.getString(3),cursor.getBlob(4));
            dataList.add(data);
        }

        cursor.close();
        db.close();

        return dataList;

    }

    public Adres dataGet(int id){

        SQLiteDatabase db = this.getReadableDatabase();

        ArrayList<Adres> dataList = new ArrayList<Adres>();
        String ids = String.valueOf(id);

        String selection = row_ID + " =? ";
        String selectionArgs[] = { ids };

        Cursor cursor = db.query(table_Name,new String[] {row_ID,row_Baslik,row_Adres,row_Kordinat,row_Image},selection,selectionArgs,null,null,null);

        while (cursor.moveToNext()){

          Adres data = new Adres(cursor.getInt(0),cursor.getString(1),cursor.getString(2),cursor.getString(3),cursor.getBlob(4));
          dataList.add(data);
        }

        cursor.close();
        db.close();

        return dataList.get(0);

    }

}
