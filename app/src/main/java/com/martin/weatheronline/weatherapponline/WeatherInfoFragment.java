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
import com.martin.weatheronline.weatherapponline.database.WeatherDB;
import com.martin.weatheronline.weatherapponline.network.CityWeatherMap;
import com.martin.weatheronline.weatherapponline.network.WeatherLoaderRetrofit;
import com.martin.weatheronline.weatherapponline.sharedPreferences.CitySharedPreferences;

import java.lang.reflect.Field;
import java.util.Locale;

public class WeatherInfoFragment extends Fragment {

    private static final String LOG_TAG = "WeatherInfoFrag";
    private static final String FONT_FILENAME = "fonts/weathericons-regular-webfont.ttf";
    private final Handler handler = new Handler();
    private CitySharedPreferences sharedPreferences;
    private Gson gson;

    private Typeface weatherFont;
    private TextView cityTitle;
    private TextView cityWeatherIcon;
    private TextView tempNow;
    private TextView minTempValue;
    private TextView maxTempValue;
    private TextView pressureValue;
    private TextView humidityValue;
    private TextView windValue;
    private WeatherDB weatherDB;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        WeatherActivity weatherActivity = (WeatherActivity) getActivity();
        weatherFont = Typeface.createFromAsset(weatherActivity.getAssets(), FONT_FILENAME);
        gson = new Gson();
        weatherDB = new WeatherDB(weatherActivity, gson);
        sharedPreferences = new CitySharedPreferences(getActivity());
        updateWeatherData(true, sharedPreferences.getCityFromSP());
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


    private void updateWeatherData(final boolean firstLaunching, final String city) {
        new Thread() {
            public void run() {
                final CityWeatherMap weatherMap = WeatherLoaderRetrofit.getWeatherMap(getActivity(), city);
                //NULL - возвращает, когда проблемы с доступом к сайту или exception
                // не писал дополнительный метод для проверки доступа к интернету, так как решил что можно в ответе getWeatherMap получить нужную инфу
                if (weatherMap == null) {
                    //Получаем данные из базы данных если есть
                    final CityWeatherMap weatherMapFromDB = weatherDB.getCityWeatherFromDb(city);
                    if (weatherMapFromDB == null) {
                        handler.post(new Runnable() {
                            public void run() {
                                showToast(getString(R.string.inet_problem));
                            }
                        });
                    } else {
                        handler.post(new Runnable() {
                            public void run() {
                                showToast(getString(R.string.inet_problem_local_weather));
                                renderWeather(weatherMapFromDB);
                            }
                        });
                    }

                } else if (weatherMap.getCod() == 404) {
                    //Если у ответа код 404 - не найдено, то пишем что не найдено города
                    handler.post(new Runnable() {
                        @Override
                        public void run() {
                            showToast(getString(R.string.city_not_found));
                        }
                    });
                } else {
                    weatherDB.addOrUpdateCityWeather(weatherMap.getId(), weatherMap.getName(), gson.toJson(weatherMap));
                    handler.post(new Runnable() {
                        public void run() {
                            sharedPreferences.setCityInSP(city);
                            renderWeather(weatherMap);
                        }
                    });
                }
            }
        }.start();
    }

    private void renderWeather(CityWeatherMap map) {
        try {
            String city = map.getName().toUpperCase(Locale.US);
            Float temp = map.getMainInfo().getTemp();
            Float minTemp = map.getMainInfo().getTempMin();
            Float maxTemp = map.getMainInfo().getTempMax();
            Float pressure = map.getMainInfo().getPressure();
            Integer humidity = map.getMainInfo().getHumidity();
            Float wind = map.getWind().getSpeed();

            Integer id = map.getWeatherId();
            Long sunrise = map.getSunrise();
            Long sunset = map.getSunset();

            cityTitle.setText(city);
            tempNow.setText(String.format(Locale.ENGLISH, "%.1f", temp));
            minTempValue.setText(String.format(Locale.ENGLISH, "%.1f", minTemp));
            maxTempValue.setText(String.format(Locale.ENGLISH, "%.1f", maxTemp));
            pressureValue.setText(String.format(Locale.ENGLISH, "%.2f", pressure));
            humidityValue.setText(String.format(Locale.ENGLISH, "%d", humidity));
            windValue.setText(String.format(Locale.ENGLISH, "%.2f", wind));
            setWeatherIcon(id, sunrise, sunset);


        } catch (Exception e) {
            Log.d(LOG_TAG, e.getMessage());
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
        updateWeatherData(false, city);
    }


    private void showToast(String textForToast) {
        Toast.makeText(getActivity(), textForToast, Toast.LENGTH_LONG).show();
    }
}
