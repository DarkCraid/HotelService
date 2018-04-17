package com.hotelservice.hotelservice;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

/**
 * Created by DarkCraid on 16/04/2018.
 */

public class dbSQLite extends SQLiteOpenHelper {
    public dbSQLite(Context cont, String name, SQLiteDatabase.CursorFactory factory, int version){
        super(cont,name,factory,version);
    }
    @Override
    public void onCreate(SQLiteDatabase db) {           //   0                         1                        2           3
        db.execSQL("create table IF NOT EXISTS tabletdata(numeroh text primary key, estado boolean default 0, ruta text, ipserver text)");
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

    }
}