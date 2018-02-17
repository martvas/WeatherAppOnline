package com.martin.weatheronline.weatherapponline.database;


import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import com.google.gson.Gson;
import com.martin.weatheronline.weatherapponline.network.ForecastWeatherMap;
import com.martin.weatheronline.weatherapponline.network.TodayWeatherMap;

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

    public void open() {
        db = dbHelper.getWritableDatabase();
    }

    public void close() {
        dbHelper.close();
    }

    public void addOrUpdateCityWeather(int id, String cityName, String weatherJson, String forecastJson) {
        ContentValues values = new ContentValues();
        values.put(DatabaseHelper.ID, id);
        values.put(DatabaseHelper.CITY_NAME, cityName.toLowerCase());
        values.put(DatabaseHelper.WEATHER_JSON, weatherJson);
        values.put(DatabaseHelper.FORECAST_JSON, forecastJson);

        if (checkIdInDb(id)) {
            db.update(DatabaseHelper.TABLE_WEATHER, values, DatabaseHelper.ID + "=" + id, null);
        } else db.insert(DatabaseHelper.TABLE_WEATHER, null, values);
    }


    public TodayWeatherMap getCityWeatherFromDb(String cityName) {
        TodayWeatherMap weatherMapFromDb = null;
        cityName = cityName.toLowerCase();
        String query = "SELECT " + DatabaseHelper.WEATHER_JSON + " FROM " + DatabaseHelper.TABLE_WEATHER + " WHERE " + DatabaseHelper.CITY_NAME + " = \'" + cityName + "\'";

        Cursor cursor = db.rawQuery(query, null);

        if (cursor.moveToFirst()) {
            String jsonString = cursor.getString(0);
            weatherMapFromDb = gson.fromJson(jsonString, TodayWeatherMap.class);
        }
        cursor.close();
        return weatherMapFromDb;
    }

    public ForecastWeatherMap getForecastWeatherFromDb(String cityName) {
        open();
        ForecastWeatherMap forecastWeatherMapFromDB = null;
        cityName = cityName.toLowerCase();
        String query = "SELECT " + DatabaseHelper.FORECAST_JSON + " FROM " + DatabaseHelper.TABLE_WEATHER + " WHERE " + DatabaseHelper.CITY_NAME + " = \'" + cityName + "\'";

        Cursor cursor = db.rawQuery(query, null);

        if (cursor.moveToFirst()) {
            String jsonString = cursor.getString(0);
            forecastWeatherMapFromDB = gson.fromJson(jsonString, ForecastWeatherMap.class);
        }
        cursor.close();

        close();
        return forecastWeatherMapFromDB;
    }

    public boolean checkIdInDb(int id) {
        List<Integer> cityIdiesInDB = new ArrayList<>();
        String query = "SELECT " + DatabaseHelper.ID + " FROM " + DatabaseHelper.TABLE_WEATHER;
        Cursor cursor = db.rawQuery(query, null);
        cursor.moveToFirst();
        while (!cursor.isAfterLast()) {
            cityIdiesInDB.add(cursor.getInt(0));
            cursor.moveToNext();
        }
        cursor.close();

        return cityIdiesInDB.contains(id);
    }

}
