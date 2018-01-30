package com.martin.weatheronline.weatherapponline.database;


import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import com.google.gson.Gson;
import com.martin.weatheronline.weatherapponline.network.CityWeatherMap;

import java.util.ArrayList;
import java.util.List;

public class WeatherDB {
    private Gson gson;
    private DatabaseHelper dbHelper;
    private SQLiteDatabase db;

    private String[] cityWeatherCollumns = {
            DatabaseHelper.ID,
            DatabaseHelper.CITY_NAME,
            DatabaseHelper.WEATHER_JSON
    };

    public WeatherDB(Context context, Gson gson) {
        this.dbHelper = new DatabaseHelper(context);
        this.gson = gson;
    }

    //FIXME Когда стоит открывать и закрвать доступ к БД, каждый раз когда делаешь запрос как у меня...
    //или как было в примере с урока в OnCreate  - открыли, в onDestriy - закрыли
    //в методе getWritableDatabase написано (Make sure to call close() when you no longer need the database.)
    public void open() {
        db = dbHelper.getWritableDatabase();
    }

    public void close() {
        dbHelper.close();
    }

    public void addOrUpdateCityWeather(int id, String cityName, String weatherJson) {
        open();
        ContentValues values = new ContentValues();
        values.put(DatabaseHelper.ID, id);
        values.put(DatabaseHelper.CITY_NAME, cityName.toLowerCase());
        values.put(DatabaseHelper.WEATHER_JSON, weatherJson);

        if (checkIdInDb(id)) {
            db.update(DatabaseHelper.TABLE_WEATHER, values, DatabaseHelper.ID + "=" + id, null);
        } else db.insert(DatabaseHelper.TABLE_WEATHER, null, values);

        close();
    }


    public CityWeatherMap getCityWeatherFromDb(String cityName) {
        open();
        CityWeatherMap weatherMapFromDb = null;
        cityName = cityName.toLowerCase();
        String query = "SELECT " + DatabaseHelper.WEATHER_JSON + " FROM " + DatabaseHelper.TABLE_WEATHER + " WHERE " + DatabaseHelper.CITY_NAME + " = \'" + cityName + "\'";

        Cursor cursor = db.rawQuery(query, null);

        if (cursor.moveToFirst()) {
            String jsonString = cursor.getString(0);
            weatherMapFromDb = gson.fromJson(jsonString, CityWeatherMap.class);
        }
        cursor.close();

        close();
        return weatherMapFromDb;
    }

    public boolean checkIdInDb(int id) {
        List<Integer> cityIdiesInDB = new ArrayList<>();
        String query = "SELECT " + DatabaseHelper.ID + " FROM " + DatabaseHelper.TABLE_WEATHER;
        Cursor cursor = db.rawQuery(query, null);

        //FIXME почему не удается сделать запрос?
        //Когда в SQLiteStudio запрос - SELECT id FROM city_weather - работает нормально
        //Cursor cursor = db.rawQuery("SELECT ? FROM ?", new String[]{DatabaseHelper.ID, DatabaseHelper.TABLE_WEATHER});

        cursor.moveToFirst();
        while (!cursor.isAfterLast()) {
            cityIdiesInDB.add(cursor.getInt(0));
            cursor.moveToNext();
        }
        cursor.close();

        return cityIdiesInDB.contains(id);
    }

}
