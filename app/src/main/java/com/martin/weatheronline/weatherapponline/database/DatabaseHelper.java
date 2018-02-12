package com.martin.weatheronline.weatherapponline.database;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class DatabaseHelper extends SQLiteOpenHelper {

    public static final String TABLE_WEATHER = "city_weather";
    //столбцы в табличке
    public static final String ID = "id";
    public static final String CITY_NAME = "city_name";
    public static final String WEATHER_JSON = "weather_json";
    private static final int DB_VERSION = 1;
    private static final String DB_NAME = "weatherapp.db";

    public DatabaseHelper(Context context) {
        super(context, DB_NAME, null, DB_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase sqLiteDatabase) {
        sqLiteDatabase.execSQL("CREATE TABLE " + TABLE_WEATHER + " (\n" +
                "    " + ID + "           INTEGER     PRIMARY KEY\n" +
                "                             UNIQUE\n" +
                "                             NOT NULL,\n" +
                "    " + CITY_NAME + "    TEXT (150)  NOT NULL,\n" +
                "    " + WEATHER_JSON + " TEXT (5000) NOT NULL\n" +
                ");\n");
    }

    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int i, int i1) {
        // если добавил новые столбцы в таблицу, то надо написать что делать
    }
}
