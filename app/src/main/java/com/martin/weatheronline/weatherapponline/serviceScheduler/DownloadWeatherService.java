package com.martin.weatheronline.weatherapponline.serviceScheduler;

import android.content.Context;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;
import android.util.Log;

import com.firebase.jobdispatcher.JobParameters;
import com.firebase.jobdispatcher.JobService;
import com.google.gson.Gson;
import com.martin.weatheronline.weatherapponline.database.WeatherDB;
import com.martin.weatheronline.weatherapponline.network.ForecastWeatherMap;
import com.martin.weatheronline.weatherapponline.network.TodayWeatherMap;
import com.martin.weatheronline.weatherapponline.network.WeatherLoader;
import com.martin.weatheronline.weatherapponline.sharedPreferences.CitySharedPreferences;

public class DownloadWeatherService extends JobService {
    public static final String SERVICE_TAG = "weather_service_tag";

    @Override
    public boolean onStartJob(final JobParameters job) {
        Log.d(SERVICE_TAG, "onStartJob");
        new Thread(new Runnable() {
            @Override
            public void run() {
                downloadWeatherToDB(job);
            }
        }).start();
        return true;
    }

    @Override
    public boolean onStopJob(JobParameters job) {
        return false;
    }


    private void downloadWeatherToDB(JobParameters jobParameters) {
        Context context = getApplicationContext();
        Gson gson = new Gson();
        WeatherDB weatherDB = new WeatherDB(context, gson);
        SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(context);
        String city = sharedPreferences.getString(CitySharedPreferences.KEY, CitySharedPreferences.STANDART_CITY);
        TodayWeatherMap todayWeatherMap = WeatherLoader.getTodayWeatherMap(context, city);
        ForecastWeatherMap forecastWeatherMap = WeatherLoader.getForecastWeatherMap(context, city);
        if (todayWeatherMap != null || todayWeatherMap.getCod() != 404 || forecastWeatherMap != null || forecastWeatherMap.getCod() != 404) {
            weatherDB.addOrUpdateTodayWeather(todayWeatherMap.getId(), todayWeatherMap.getName(), gson.toJson(todayWeatherMap));
            weatherDB.addOrUpdateForecast(forecastWeatherMap.getCity().getId(), forecastWeatherMap.getCity().getName(), gson.toJson(forecastWeatherMap));
        }
//        jobFinished(jobParameters, true);
    }
}
