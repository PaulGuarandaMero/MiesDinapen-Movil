package com.example.mies_dinapen.BDSQLITE;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;



import com.google.type.DateTime;

import java.sql.Blob;
import java.util.Date;

public class BaseDeDatos extends SQLiteOpenHelper {
    public static final String DBNAME="MiesDinapen.db";

    public BaseDeDatos(Context context) {
        super(context,"MiesDinapen.db", null, 1);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL("create table Operadores(idOperador text primary key, Nombre text, Apellido text, Clave text)");
        db.execSQL("create table Intervenciones(idIntervencion integer primary key autoincrement,  latitud text, logitud text, hora text,idoperador text)");
        db.execSQL("create table IntervencionesAudios(idAudio integer primary key autoincrement,Audio longblob , FechaRegistro timestamp)");
        db.execSQL("create table IntervencionesFotos(idFoto integer primary key autoincrement, IDIntervencion Integer , FotoIncidente longblob, FechaRegistro timestamp)");
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("drop table if exists IntervencionesAudios");
        db.execSQL("drop table if exists Operadores");
        db.execSQL("drop table if exists Intervenciones");
        db.execSQL("drop table if exists IntervencionesFotos");
    }

    public Boolean insertDataAudios (byte [] audio, Date FechaRegistro){
        SQLiteDatabase db= this.getWritableDatabase();
        ContentValues values = new ContentValues();

        values.put("Audio", String.valueOf(audio));
        values.put("FechaRegistro", String.valueOf(FechaRegistro));

        long result= db.insert("IntervencionesAudios", null, values);
        if(result==1)
            return false;
        else
            return true;
    }
    public Boolean insertDataFotos (byte [] fotos, Date FechaRegistro){
        SQLiteDatabase db= this.getWritableDatabase();
        ContentValues values = new ContentValues();

        values.put("FotoIncidente", String.valueOf(fotos));
        values.put("FechaRegistro", String.valueOf(FechaRegistro));

        long result= db.insert("IntervencionesFotos", null, values);
        if(result==1)
            return false;
        else
            return true;
    }


/*1
    public Boolean checklatitud (String latitud, String longitud){
        SQLiteDatabase db= this.getWritableDatabase();
        Cursor cursor = db.rawQuery("select * from ListaPoligonosLugares where latitud=? and longitud=? ", new String[]{latitud, longitud});

        if(cursor.getCount()>0)
            return true;
        else
            return false;

    }

    public Boolean checklogin(String cedula, String clave){
        SQLiteDatabase db = this.getWritableDatabase();
        Cursor cursor = db.rawQuery("select * from Operador where cedula=? and clave= ?", new String[]{cedula, clave});
        if (cursor.getCount()>0)
            return true;
        else
            return false;
    }

*/
}
