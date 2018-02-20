package com.martin.weatheronline.weatherapponline;


import android.graphics.Typeface;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.app.Fragment;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.google.gson.Gson;
import com.martin.weatheronline.weatherapponline.database.WeatherDB;
import com.martin.weatheronline.weatherapponline.network.ForecastWeatherMap;
import com.martin.weatheronline.weatherapponline.network.WeatherLoader;
import com.martin.weatheronline.weatherapponline.sharedPreferences.CitySharedPreferences;

import java.util.List;
import java.util.Locale;

public class ForecastFragment extends Fragment {

    RecyclerView forecastRecyclerView;
    private Typeface weatherFont;
    private WeatherDB weatherDB;
    private Gson gson;
    private Handler handler;
    private CitySharedPreferences citySharedPreferences;
    private TextView dayOfTheWeek;
    private TextView date;
    private TextView time;
    private TextView icon;
    private TextView temperature;
    private TextView description;
    private TextView wind;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        citySharedPreferences = new CitySharedPreferences(getActivity());
        gson = new Gson();
        weatherDB = new WeatherDB(getContext(), gson);
        weatherDB.open();
        handler = new Handler();
        weatherFont = Typeface.createFromAsset(getActivity().getAssets(), WeatherInfoFragment.FONT_FILENAME);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View forecastView = inflater.inflate(R.layout.fragment_forecast, container, false);
        String city = citySharedPreferences.getCityFromSP();
        updateForecastFromApi(city);
        return forecastView;
    }

    public void updateForecastFromApi(final String city) {
        new Thread(new Runnable() {
            @Override
            public void run() {
                final ForecastWeatherMap forecastWeatherMap = WeatherLoader.getForecastWeatherMap(getContext(), city);
                if (forecastWeatherMap == null) {
                    //Получаем данные из базы данных если есть
                    final ForecastWeatherMap forecastWeatherFromDb = weatherDB.getForecastWeatherFromDb(city);
                    if (forecastWeatherFromDb == null) {
                        handler.post(new Runnable() {
                            public void run() {
                                showToast(getString(R.string.inet_problem));
                            }
                        });
                    } else {
                        handler.post(new Runnable() {
                            public void run() {
                                showToast(getString(R.string.inet_problem_local_weather));
                                renderForecast(forecastWeatherFromDb);
                            }
                        });
                    }

                } else if (forecastWeatherMap.getCod() == 404) {
                    //Если у ответа код 404 - не найдено, то пишем что не найдено города
                    handler.post(new Runnable() {
                        @Override
                        public void run() {
                            showToast(getString(R.string.city_not_found));
                        }
                    });
                } else {
                    weatherDB.addOrUpdateForecast(forecastWeatherMap.getCity().getId(), forecastWeatherMap.getCity().getName(), gson.toJson(forecastWeatherMap));
                    handler.post(new Runnable() {
                        public void run() {
                            renderForecast(forecastWeatherMap);
                        }
                    });
                }
            }
        }).start();
    }

    public void renderForecast(ForecastWeatherMap forecastWeatherMap) {
        forecastRecyclerView = getActivity().findViewById(R.id.forecast_recycler_view);
        LinearLayoutManager layoutManager = new LinearLayoutManager(getContext());
        layoutManager.setOrientation(LinearLayout.VERTICAL);
        forecastRecyclerView.setLayoutManager(layoutManager);
        DividerItemDecoration mDividerItemDecoration = new DividerItemDecoration(
                forecastRecyclerView.getContext(),
                layoutManager.getOrientation()
        );
        forecastRecyclerView.addItemDecoration(mDividerItemDecoration);
        forecastRecyclerView.setAdapter(new ForecastAdapter(forecastWeatherMap));
    }

    private void showToast(String textForToast) {
        Toast.makeText(getActivity(), textForToast, Toast.LENGTH_LONG).show();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        weatherDB.close();
    }

    private String getWeatherIcon(int actualId) {
        String iconId = "wi_owm_" + actualId;
        int id = WeatherInfoFragment.getResId(iconId, R.string.class);
        return getString(id);
    }

    class ForecastViewHolder extends RecyclerView.ViewHolder {
        ForecastWeatherMap forecastWeatherMap;

        ForecastViewHolder(LayoutInflater inflater, ViewGroup parent, ForecastWeatherMap forecastWeatherMap) {
            super(inflater.inflate(R.layout.forecast_list_item, parent, false));
            dayOfTheWeek = itemView.findViewById(R.id.day_of_week_forecast);
            date = itemView.findViewById(R.id.date_forecast);
            time = itemView.findViewById(R.id.time_forecast);
            icon = itemView.findViewById(R.id.forecast_icon_item);
            icon.setTypeface(weatherFont);
            temperature = itemView.findViewById(R.id.forecast_temperature);
            description = itemView.findViewById(R.id.forecast_description);
            wind = itemView.findViewById(R.id.wind_forecast);
            this.forecastWeatherMap = forecastWeatherMap;
        }

        void bind(int position) {
            List forecastList = forecastWeatherMap.getForecastList();
            ForecastWeatherMap.Forecast forecast = (ForecastWeatherMap.Forecast) forecastList.get(position);
            List<ForecastWeatherMap.Weather> forecastWeather = forecast.getWeather();
            Long dateInMillis = forecast.getDt() * 1000;
            String dayOfWeekStr = String.format(Locale.ENGLISH, "%ta", dateInMillis);
            String dateStr = String.format(Locale.ENGLISH, "%1$te.%1$tm.%1$ty", dateInMillis);
            String timeStr = String.format(Locale.ENGLISH, "%tR", dateInMillis);
            int weatherId = forecastWeather.get(0).getId();
            String iconStr = getWeatherIcon(weatherId);
            Float tempFloat = forecast.getMainInformation().getTemp();
            String descriptionStr = forecastWeather.get(0).getDescription();
            Float windFloat = forecast.getWind().getSpeed();

            dayOfTheWeek.setText(dayOfWeekStr);
            date.setText(dateStr);
            time.setText(timeStr);
            icon.setText(iconStr);
            temperature.setText(String.format(Locale.ENGLISH, "%d", Math.round(tempFloat)));
            description.setText(descriptionStr);
            wind.setText(String.format(Locale.ENGLISH, "%d", Math.round(windFloat)));
        }
    }

    public class ForecastAdapter extends RecyclerView.Adapter<ForecastViewHolder> {
        ForecastWeatherMap forecastWeatherMap;

        ForecastAdapter(ForecastWeatherMap forecastWeatherMap) {
            this.forecastWeatherMap = forecastWeatherMap;
        }

        @Override
        public int getItemViewType(int position) {
            return position;
        }

        @Override
        public ForecastViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            LayoutInflater inflater = LayoutInflater.from(getContext());
            return new ForecastViewHolder(inflater, parent, forecastWeatherMap);
        }

        @Override
        public void onBindViewHolder(ForecastViewHolder holder, int position) {
            holder.bind(position);
        }

        @Override
        public int getItemCount() {
            return forecastWeatherMap.getForecastList().size();
        }
    }
}
