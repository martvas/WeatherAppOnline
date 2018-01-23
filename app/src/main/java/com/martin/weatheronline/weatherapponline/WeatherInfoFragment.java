package com.martin.weatheronline.weatherapponline;


import android.graphics.Typeface;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.google.gson.Gson;

import org.json.JSONObject;

import java.lang.reflect.Field;
import java.util.Locale;

public class WeatherInfoFragment extends Fragment {

    private static final String LOG_TAG = "WeatherInfoFrag";
    private static final String FONT_FILENAME = "fonts/weathericons-regular-webfont.ttf";
    private final Handler handler = new Handler();

    private Typeface weatherFont;
    private TextView cityTitle;
    private TextView cityWeatherIcon;
    private TextView tempNow;
    private TextView minTempValue;
    private TextView maxTempValue;
    private TextView pressureValue;
    private TextView humidityValue;
    private TextView windValue;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        WeatherActivity weatherActivity = (WeatherActivity) getActivity();
        weatherFont = Typeface.createFromAsset(weatherActivity.getAssets(), FONT_FILENAME);
        updateWeatherData(new CitySharedPreferences(getActivity()).getCityFromSP());
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View weatherInfoView = inflater.inflate(R.layout.fragment_weather_info, container, false);
        cityTitle = weatherInfoView.findViewById(R.id.city_title);
        cityWeatherIcon = weatherInfoView.findViewById(R.id.city_weather_icon);
        cityWeatherIcon.setTypeface(weatherFont);
        tempNow = weatherInfoView.findViewById(R.id.temp_now_txt);
        minTempValue = weatherInfoView.findViewById(R.id.min_temp_value);
        maxTempValue = weatherInfoView.findViewById(R.id.max_temp_value);
        pressureValue = weatherInfoView.findViewById(R.id.pressure_value);
        humidityValue = weatherInfoView.findViewById(R.id.humidity_value);
        windValue = weatherInfoView.findViewById(R.id.wind_value);
        return weatherInfoView;
    }


    private void updateWeatherData(final String city) {
        new Thread() {//Отдельный поток для получения новых данных в фоне
            public void run() {
                final JSONObject json = WeatherDataLoader.getJSONData(getActivity(), city);
                if (json == null) {
                    handler.post(new Runnable() {
                        public void run() {
                            Toast.makeText(getActivity(), getString(R.string.city_not_found),
                                    Toast.LENGTH_LONG).show();
                        }
                    });
                } else {
                    handler.post(new Runnable() {
                        public void run() {
                            renderWeather(json);
                        }
                    });
                }
            }
        }.start();
    }

    private void renderWeather(JSONObject json) {
        try {
            MyWeatherMap map = new Gson().fromJson(json.toString(), MyWeatherMap.class);
            String city = map.getName().toUpperCase(Locale.US);
            float temp = map.getMainInfo().getTemp();
            Integer minTemp = map.getMainInfo().getTempMin();
            Integer maxTemp = map.getMainInfo().getTempMax();
            Integer pressure = map.getMainInfo().getPressure();
            Integer humidity = map.getMainInfo().getHumidity();
            Float wind = map.getWind().getSpeed();

            Integer id = map.getId();
            Long sunrise = map.getSunrise();
            Long sunset = map.getSunset();

            cityTitle.setText(city);
            tempNow.setText(Float.toString(temp));
            minTempValue.setText(Integer.toString(minTemp));
            maxTempValue.setText(Integer.toString(maxTemp));
            pressureValue.setText(Integer.toString(pressure));
            humidityValue.setText(Integer.toString(humidity));
            windValue.setText(Float.toString(wind));
            setWeatherIcon(id, sunrise, sunset);


        } catch (Exception e) {
            Log.d(LOG_TAG, e.getMessage());//FIXME Обработка ошибки
        }
    }


    private void setWeatherIcon(int actualId, long sunrise, long sunset) {
        String iconId = "wi_owm_" + actualId;
        int id = getResId(iconId, R.string.class);
        String icon = getString(id);
        cityWeatherIcon.setText(icon);
    }


    public int getResId(String resName, Class<?> c) {

        try {
            Field idField = c.getDeclaredField(resName);
            return idField.getInt(idField);
        } catch (Exception e) {
            e.printStackTrace();
            return -1;
        }
    }

    public void changeCity(String city) {
        updateWeatherData(city);
    }
}
