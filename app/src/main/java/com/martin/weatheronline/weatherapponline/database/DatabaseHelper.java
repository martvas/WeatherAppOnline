package com.martin.weatheronline.weatherapponline.database;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class DatabaseHelper extends SQLiteOpenHelper {

    static final String TABLE_WEATHER = "city_weather";
    //столбцы в табличке
    static final String ID = "id";
    static final String CITY_NAME = "city_name";
    static final String WEATHER_JSON = "weather_json";
    static final String FORECAST_JSON = "forecast_json";

    private static final int DB_VERSION = 4;
    private static final String DB_NAME = "weatherapp.db";

    private static final String DROP_TABLE = "DROP TABLE " + TABLE_WEATHER;
    private static final String DATABASE_V3 = new StringBuilder()
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
            .append(" TEXT (5000),\n")
            .append(" ")
            .append(FORECAST_JSON)
            .append(" TEXT (5000)\n")
            .append(");\n")
            .toString();

    DatabaseHelper(Context context) {
        super(context, DB_NAME, null, DB_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase sqLiteDatabase) {
        sqLiteDatabase.execSQL(DATABASE_V3);
    }

    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int oldVersion, int newVersion) {
        if (oldVersion < 4) {
            sqLiteDatabase.execSQL(DROP_TABLE);
            sqLiteDatabase.execSQL(DATABASE_V3);
        }
    }
}
