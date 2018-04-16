package com.hotelservice.hotelservice;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

/**
 * Created by DarkCraid on 16/04/2018.
 */

public class dbConexion {
    public SQLiteDatabase db;

    public void start(Context cont){
        dbSQLite mod=new dbSQLite(cont,"hotelserv",null,1);
        SQLiteDatabase dbs=mod.getWritableDatabase();
        this.db = dbs;
    }

    public Boolean changeStatus(String nh,int est){
        ContentValues reg=new ContentValues();
        reg.put("status",est);
        if(db.update("tabletdata",reg,"numeroh='"+nh+"'",null)!=0)
            return true;
        else
            return false;
    }

    public long InsertTable(String table, ContentValues reg){
        return db.insert(table, null, reg);
    }//-1 indica que no se pudo insertar.

    public Cursor readData(){
        Cursor cursor=db.rawQuery("select * from tabletdata",null);
        return cursor;
    }
}
