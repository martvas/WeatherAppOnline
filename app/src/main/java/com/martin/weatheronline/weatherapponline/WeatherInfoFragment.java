package com.martin.weatheronline.weatherapponline;


import android.content.ActivityNotFoundException;
import android.content.Context;
import android.content.Intent;
import android.graphics.Typeface;
import android.os.Bundle;
import android.os.Handler;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.google.gson.Gson;
import com.martin.weatheronline.weatherapponline.database.WeatherDB;
import com.martin.weatheronline.weatherapponline.network.TodayWeatherMap;
import com.martin.weatheronline.weatherapponline.network.WeatherLoader;
import com.martin.weatheronline.weatherapponline.serviceScheduler.WeatherJobScheduler;
import com.martin.weatheronline.weatherapponline.sharedPreferences.CitySharedPreferences;

import java.lang.reflect.Field;
import java.util.Locale;

public class WeatherInfoFragment extends Fragment {

    public static final String FONT_FILENAME = "fonts/weathericons-regular-webfont.ttf";
    private static final String LOG_TAG = "WeatherInfoFrag";
    private final Handler handler = new Handler();
    private CitySharedPreferences sharedPreferences;
    private Gson gson;

    private ProgressBar progressBar;
    private RelativeLayout mainLayout;

    private Typeface weatherFont;
    private TextView cityTitle;
    private TextView date;
    private TextView cityWeatherIcon;
    private TextView tempNow;
    private TextView minTempValue;
    private TextView maxTempValue;


    private TextView pressureValue;
    private TextView humidityValue;
    private TextView windValue;
    private WeatherDB weatherDB;

    public static int getResId(String resName, Class<?> c) {
        try {
            Field idField = c.getDeclaredField(resName);
            return idField.getInt(idField);
        } catch (Exception e) {
            e.printStackTrace();
            return -1;
        }
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        WeatherActivity weatherActivity = (WeatherActivity) getActivity();
        weatherFont = Typeface.createFromAsset(weatherActivity.getAssets(), FONT_FILENAME);
        gson = new Gson();
        weatherDB = new WeatherDB(weatherActivity, gson);
        weatherDB.open();
        sharedPreferences = new CitySharedPreferences(weatherActivity);
        WeatherJobScheduler weatherJobScheduler = new WeatherJobScheduler(getContext());
        weatherJobScheduler.startSchedule();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View weatherInfoView = inflater.inflate(R.layout.fragment_weather_info, container, false);
        progressBar = weatherInfoView.findViewById(R.id.progressbar);
        mainLayout = weatherInfoView.findViewById(R.id.main_layout);
        cityTitle = weatherInfoView.findViewById(R.id.city_title);
        date = weatherInfoView.findViewById(R.id.datetime);
        cityWeatherIcon = weatherInfoView.findViewById(R.id.city_weather_icon);
        cityWeatherIcon.setTypeface(weatherFont);
        tempNow = weatherInfoView.findViewById(R.id.temp_now_txt);
        minTempValue = weatherInfoView.findViewById(R.id.min_temp_value);
        maxTempValue = weatherInfoView.findViewById(R.id.max_temp_value);
        pressureValue = weatherInfoView.findViewById(R.id.pressure_value);
        humidityValue = weatherInfoView.findViewById(R.id.humidity_value);
        windValue = weatherInfoView.findViewById(R.id.wind_value);
        FloatingActionButton fab = weatherInfoView.findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startShareIntent();
            }
        });
        updateTodayWeatherData(sharedPreferences.getCityFromSP());
        return weatherInfoView;
    }

    private void updateTodayWeatherData(final String city) {
        showProgressBar();
        new Thread() {
            public void run() {
                final TodayWeatherMap weatherMap = WeatherLoader.getTodayWeatherMap(getActivity(), city);
                //NULL - возвращает, когда проблемы с доступом к сайту или exception
                // не писал дополнительный метод для проверки доступа к интернету, так как решил что можно в ответе getTodayWeatherMap получить нужную инфу
                if (weatherMap == null) {
                    //Получаем данные из базы данных если есть
                    final TodayWeatherMap weatherMapFromDB = weatherDB.getCityWeatherFromDb(city);
                    if (weatherMapFromDB == null) {
                        handler.post(new Runnable() {
                            public void run() {
                                hideProgressBar();
                                showToast(getString(R.string.inet_problem));
                            }
                        });
                    } else {
                        handler.post(new Runnable() {
                            public void run() {
                                hideProgressBar();
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
                            hideProgressBar();
                            showToast(getString(R.string.city_not_found));
                        }
                    });
                } else {
                    weatherDB.addOrUpdateTodayWeather(weatherMap.getId(), weatherMap.getName(), gson.toJson(weatherMap));
                    handler.post(new Runnable() {
                        public void run() {
                            hideProgressBar();
                            sharedPreferences.setCityInSP(city);
                            renderWeather(weatherMap);
                        }
                    });
                }
            }
        }.start();
    }

    private void showProgressBar() {
        mainLayout.setVisibility(View.GONE);
        progressBar.setVisibility(ProgressBar.VISIBLE);
    }

    private void hideProgressBar() {
        mainLayout.setVisibility(View.VISIBLE);
        progressBar.setVisibility(ProgressBar.INVISIBLE);
    }

    private void renderWeather(TodayWeatherMap map) {
        try {
            String city = map.getName().toUpperCase(Locale.US);
            Long unixDate = map.getDateInUnix();
            Float temp = map.getMainInfo().getTemp();
            Float minTemp = map.getMainInfo().getTempMin();
            Float maxTemp = map.getMainInfo().getTempMax();
            Float pressure = map.getMainInfo().getPressure();
            Integer humidity = map.getMainInfo().getHumidity();
            Float wind = map.getWind().getSpeed();
            getContext().getSystemService(Context.CONNECTIVITY_SERVICE);
            Integer id = map.getWeatherId();
            cityTitle.setText(city);
            date.setText(String.format(Locale.ENGLISH, "Lastupdate: %1$tk:%1$tM %1$te.%1$tm.%1$ty", unixDate * 1000));
            tempNow.setText(String.format(Locale.ENGLISH, "%.1f", temp));
            minTempValue.setText(String.format(Locale.ENGLISH, "%.1f", minTemp));
            maxTempValue.setText(String.format(Locale.ENGLISH, "%.1f", maxTemp));
            pressureValue.setText(String.format(Locale.ENGLISH, "%.2f", pressure));
            humidityValue.setText(String.format(Locale.ENGLISH, "%d", humidity));
            windValue.setText(String.format(Locale.ENGLISH, "%.2f", wind));
            setWeatherIcon(id);
        } catch (Exception e) {
            Log.d(LOG_TAG, e.getMessage());
        }
    }

    private void setWeatherIcon(int actualId) {
        String iconId = "wi_owm_" + actualId;
        int id = getResId(iconId, R.string.class);
        String icon = getString(id);
        cityWeatherIcon.setText(icon);
    }

    public void changeCity(String city) {
        updateTodayWeatherData(city);
    }

    private void showToast(String textForToast) {
        Toast.makeText(getActivity(), textForToast, Toast.LENGTH_LONG).show();
    }

    private void startShareIntent() {
        Intent shareIntent = new Intent(Intent.ACTION_SEND);
        shareIntent.putExtra(Intent.EXTRA_SUBJECT, "Weather in " + cityTitle.getText().toString());
        shareIntent.putExtra(Intent.EXTRA_TEXT, "Temerature right now is: " + tempNow.getText().toString());
        shareIntent.setType("message/rfc822");
        try {
            startActivity(shareIntent);
        } catch (ActivityNotFoundException e) {
            showToast("No email client on device");
        }
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        weatherDB.close();
    }
}
