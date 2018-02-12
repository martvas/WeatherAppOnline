package com.martin.weatheronline.weatherapponline.network;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.util.Log;

import com.martin.weatheronline.weatherapponline.WeatherActivity;

import java.io.IOException;

import retrofit2.Call;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;


public class WeatherLoader {
    private static final String apiKey = "b530d8eb26f286763d166441260b3652";
    private static final String OPEN_WEATHER_BASE_URL = "http://api.openweathermap.org/";
    private static final String UNIT = "metric";

    public static CityWeatherMap getWeatherMap(Context context, String city) {
        CityWeatherMap weatherMap = null;

        //когда оффлайн очень долго делал запрос через Retrofit, поэтому сделал быструю проверку на доступ к интернету
        if (!isConnectedToInternet(context)) return weatherMap;

        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(OPEN_WEATHER_BASE_URL)
                .addConverterFactory(GsonConverterFactory.create())
                .build();

        WeatherApi weatherApi = retrofit.create(WeatherApi.class);

        Call<CityWeatherMap> call = weatherApi.getWeatherByCityname(city, UNIT, apiKey);

        try {
            Response<CityWeatherMap> response = call.execute();

            if (response.isSuccessful()) {
                weatherMap = response.body();
            } else if (response.code() == 404) {
                weatherMap = new CityWeatherMap();
                weatherMap.setCod(404);
            }
        } catch (IOException e) {
            Log.d(WeatherActivity.LOG_TAG, e.getMessage());
        }
        return weatherMap;
    }

    private static boolean isConnectedToInternet(Context context) {
        ConnectivityManager connectivityManager;
        NetworkInfo activeNetwork;

        connectivityManager = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
        activeNetwork = connectivityManager.getActiveNetworkInfo();
        if (activeNetwork == null) return false;
        else if (activeNetwork.isConnected()) return true;
        else return true;
    }

}
