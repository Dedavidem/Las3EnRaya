package com.otr.tres_en_raya;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.support.annotation.Nullable;

public class SQLite extends SQLiteOpenHelper {

        public static final int DATABASE_VERSION = 1;
        public static final String DATABASE_NAME = "JugadoresYStats.db";

        public SQLite(@Nullable Context context) {
            super(context, DATABASE_NAME, null, DATABASE_VERSION);
        }

        @Override
        public void onCreate(SQLiteDatabase db) {
            db.execSQL(Estructura.SQL_CREATE_DATOS_PARTIDAS);
            db.execSQL(Estructura.SQL_CREATE_DATOS_USUARIOS);
        }

        @Override
        public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

            db.execSQL(Estructura.SQL_CREATE_DATOS_PARTIDAS);
            db.execSQL(Estructura.SQL_DELETE_DATOS_PARTIDAS);
            db.execSQL(Estructura.SQL_CREATE_DATOS_USUARIOS);
            db.execSQL(Estructura.SQL_DELETE_DATOS_USUARIOS);


        }


}
