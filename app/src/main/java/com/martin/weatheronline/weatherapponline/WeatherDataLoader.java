package com.martin.weatheronline.weatherapponline;

import android.content.Context;

import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

public class WeatherDataLoader {
    private static final String apiKey = "b530d8eb26f286763d166441260b3652";
    private static final String OPEN_WEATHER_API_CALL = "http://api.openweathermap.org/data/2.5/weather?q=%s&units=metric&appid=%s";
    private static final String RESPONSE = "cod";
    private static final String NEW_LINE = "\n";
    private static final int ALL_GOOD = 200;

    static JSONObject getJSONData(Context context, String city) {
        try {
            URL url = new URL(String.format(OPEN_WEATHER_API_CALL, city, apiKey));
            HttpURLConnection connection = (HttpURLConnection) url.openConnection();

            BufferedReader reader = new BufferedReader(new InputStreamReader(connection.getInputStream()));
            StringBuilder rawData = new StringBuilder(1024);
            String tempVariable;
            while ((tempVariable = reader.readLine()) != null) {
                rawData.append(tempVariable).append(NEW_LINE);
            }
            reader.close();

            JSONObject jsonObject = new JSONObject(rawData.toString());
            if (jsonObject.getInt(RESPONSE) != ALL_GOOD) {
                return null;
            }
            return jsonObject;
        } catch (Exception e) {
            return null; //FIXME Обработка ошибки
        }
    }
}
