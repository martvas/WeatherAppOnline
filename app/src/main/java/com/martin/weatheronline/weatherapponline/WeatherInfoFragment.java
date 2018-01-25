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

import java.io.BufferedReader;
import java.io.File;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.IOException;
import java.lang.reflect.Field;
import java.util.Locale;

public class WeatherInfoFragment extends Fragment {

    private static final String LOG_TAG = "WeatherInfoFrag";
    private static final String FONT_FILENAME = "fonts/weathericons-regular-webfont.ttf";
    private final Handler handler = new Handler();
    private final String FILE_NAME = "last_json_city.txt";

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
        updateWeatherData(true, new CitySharedPreferences(getActivity()).getCityFromSP());
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
                final JSONObject json = WeatherDataLoaderFromApi.getJSONData(getActivity(), city);
                if (json == null) {
                    if (firstLaunching && !WeatherDataLoaderFromApi.isConnectionExist()) {
                        handler.post(new Runnable() {
                            @Override
                            public void run() {
                                renderWeather(getJsonFromInternalStorage());
                            }
                        });
                    } else {
                        handler.post(new Runnable() {
                            public void run() {
                                showToast(getString(R.string.city_not_found));
                            }
                        });
                    }
                } else {
                    saveJsonAtInternalStorage(json);
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
            tempNow.setText(String.format(Locale.ENGLISH, "%.1f", temp));
            minTempValue.setText(String.format(Locale.ENGLISH, "%d", minTemp));
            maxTempValue.setText(String.format(Locale.ENGLISH, "%d", maxTemp));
            pressureValue.setText(String.format(Locale.ENGLISH, "%d", pressure));
            humidityValue.setText(String.format(Locale.ENGLISH, "%d", humidity));
            windValue.setText(String.format(Locale.ENGLISH, "%.2f", wind));
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
        updateWeatherData(false, city);
    }


    private void showToast(String textForToast) {
        Toast.makeText(getActivity(), textForToast, Toast.LENGTH_LONG).show();
    }


    private void saveJsonAtInternalStorage(final JSONObject json) {
        new Thread(new Runnable() {
            @Override
            public void run() {
                String jsonFilePath = getContext().getFilesDir() + "/" + FILE_NAME;

                try {
                    FileOutputStream fileOutput = new FileOutputStream(jsonFilePath, false);
                    fileOutput.write(json.toString().getBytes());
                    fileOutput.flush();
                    fileOutput.close();
                } catch (IOException e) {
                    Log.d(WeatherActivity.LOG_TAG, e.getMessage());
                }

            }
        }).start();
    }

    private JSONObject getJsonFromInternalStorage() {
        JSONObject jsonObject = null;
        StringBuffer stringBuffer = new StringBuffer();

        try {
            String jsonFilePath = getContext().getFilesDir() + "/" + FILE_NAME;
            File jsonFile = new File(jsonFilePath);
            if (!jsonFile.exists()) {
                Log.d(WeatherActivity.LOG_TAG, "File in internal storage - don't exist");
            } else {
                BufferedReader br = new BufferedReader(new FileReader(jsonFile));

                String line;
                while ((line = br.readLine()) != null) {
                    stringBuffer.append(line);
                }

                jsonObject = new JSONObject(stringBuffer.toString());
            }

        } catch (Exception e) {
            Log.d(WeatherActivity.LOG_TAG, e.getMessage());
        }

        return jsonObject;
    }

}
