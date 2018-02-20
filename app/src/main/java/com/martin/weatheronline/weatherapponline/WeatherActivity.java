package com.martin.weatheronline.weatherapponline;

import android.content.DialogInterface;
import android.os.Bundle;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.text.InputType;
import android.view.Menu;
import android.view.MenuItem;
import android.view.ViewGroup;
import android.widget.EditText;

public class WeatherActivity extends AppCompatActivity {

    public static final String LOG_TAG = "wlog";
    private static final String DIALOG_BTN_TEXT = "Search";
    MyPagerAdapter myPagerAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_weather);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        TabLayout tabLayout = findViewById(R.id.tab_layout);
        tabLayout.setTabGravity(TabLayout.GRAVITY_FILL);
        final ViewPager viewPager = findViewById(R.id.view_pager);
        myPagerAdapter = new MyPagerAdapter(getSupportFragmentManager());
        viewPager.setAdapter(myPagerAdapter);
        viewPager.addOnPageChangeListener(new TabLayout.TabLayoutOnPageChangeListener(tabLayout));
        tabLayout.addOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                viewPager.setCurrentItem(tab.getPosition());
            }

            @Override
            public void onTabUnselected(TabLayout.Tab tab) {

            }

            @Override
            public void onTabReselected(TabLayout.Tab tab) {

            }
        });
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
        WeatherInfoFragment weatherInfoFragment = (WeatherInfoFragment) getSupportFragmentManager().findFragmentByTag(myPagerAdapter.getWeatherFragment().getTag());
        weatherInfoFragment.changeCity(city);
        ForecastFragment forecastFragment = (ForecastFragment) getSupportFragmentManager().findFragmentByTag(myPagerAdapter.getForecastFragment().getTag());
        forecastFragment.updateForecastFromApi(city);
    }


    public class MyPagerAdapter extends FragmentPagerAdapter {
        private Fragment weatherFragment;
        private Fragment forecastFragment;

        MyPagerAdapter(FragmentManager fm) {
            super(fm);
        }

        @Override
        public Fragment getItem(int position) {
            if (position == 0) {
                return new WeatherInfoFragment();
            } else {
                return new ForecastFragment();
            }
        }

        @Override
        public int getCount() {
            return 2;
        }

        @Override
        public Object instantiateItem(ViewGroup container, int position) {
            Fragment createdFragment = (Fragment) super.instantiateItem(container, position);
            switch (position) {
                case 0:
                    weatherFragment = createdFragment;
                    break;
                case 1:
                    forecastFragment = createdFragment;
                    break;
            }
            return createdFragment;
        }

        Fragment getWeatherFragment() {
            return weatherFragment;
        }

        Fragment getForecastFragment() {
            return forecastFragment;
        }
    }
}
