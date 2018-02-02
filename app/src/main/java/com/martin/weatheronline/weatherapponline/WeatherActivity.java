package com.martin.weatheronline.weatherapponline;

import android.content.DialogInterface;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.text.InputType;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.EditText;

public class WeatherActivity extends AppCompatActivity {

    public static final String LOG_TAG = "wlog";
    private static final String DIALOG_BTN_TEXT = "Search";
    private static final String WEATHER_INFO_TAG = "weather_info_tag123";


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_weather);

        if (savedInstanceState == null) {
            getSupportFragmentManager().beginTransaction().add(R.id.container_for_fragment,
                    new WeatherInfoFragment(), WEATHER_INFO_TAG).commit();
        }
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.search_city_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == R.id.search_city_btn) {
            showSearchCityDialog();
            return true;
        }
        return false;
    }

    private void showSearchCityDialog() {
        final EditText cityInput = new EditText(this);
        cityInput.setInputType(InputType.TYPE_CLASS_TEXT);
        new AlertDialog.Builder(WeatherActivity.this)
                .setTitle(R.string.search_city_dialog)
                .setView(cityInput)
                .setPositiveButton(DIALOG_BTN_TEXT, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        searchNewCity(cityInput.getText().toString());
                    }
                })
                .show();
    }


    public void searchNewCity(String city) {
        WeatherInfoFragment weatherInfoFragment = (WeatherInfoFragment) getSupportFragmentManager().findFragmentByTag(WEATHER_INFO_TAG);
        weatherInfoFragment.changeCity(city);
    }
}
