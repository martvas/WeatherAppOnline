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
    public static final String FORECAST_JSON = "forecast_json";

    private static final int DB_VERSION = 2;
    private static final String DB_NAME = "weatherapp.db";

    private static final String ALTER_FORECAST_JSON_V2 = "ALTER TABLE " + TABLE_WEATHER + " ADD COLUMN " + FORECAST_JSON + " TEXT";

    public DatabaseHelper(Context context) {
        super(context, DB_NAME, null, DB_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase sqLiteDatabase) {
        sqLiteDatabase.execSQL(new StringBuilder()
                .append("CREATE TABLE ")
                .append(TABLE_WEATHER)
                .append(" (\n")
                .append(" ")
                .append(ID)
                .append(" INTEGER PRIMARY KEY\n")
                .append("UNIQUE\n")
                .append("NOT NULL,\n")
                .append(" ")
                .append(CITY_NAME)
                .append(" TEXT (150)  NOT NULL,\n")
                .append(" ")
                .append(WEATHER_JSON)
                .append(" TEXT (5000) NOT NULL\n")
                .append(");\n")
                .toString());
        ;
    }

    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int oldVersion, int newVersion) {
        if (oldVersion < 2) sqLiteDatabase.execSQL(ALTER_FORECAST_JSON_V2);
    }
}
