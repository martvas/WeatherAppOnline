package com.martin.weatheronline.weatherapponline.network;


import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Query;

public interface WeatherApi {

    @GET("data/2.5/weather")
    Call<TodayWeatherMap> getTodayWeatherByCityname(@Query("q") String cityName, @Query("units") String unitType, @Query("appid") String apiKey);

    @GET("data/2.5/forecast")
    Call<ForecastWeatherMap> getForecastWeatherByCityname(@Query("q") String cityName, @Query("units") String unitType, @Query("appid") String apiKey);
}
