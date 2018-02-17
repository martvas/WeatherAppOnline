package com.martin.weatheronline.weatherapponline;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.view.ViewGroup;

public class MyPagerAdapter extends FragmentPagerAdapter {

    private Fragment weatherFragment;
    private Fragment forecastFragment;

    public MyPagerAdapter(FragmentManager fm) {
        super(fm);
    }

    @Override
    public Fragment getItem(int position) {
        if (position == 0) {
            //fixme try to do this way if fragment already exist
//            if (weatherFragment == null){
//                return new WeatherInfoFragment();
//            } else {
//
//            }
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

    public Fragment getWeatherFragment() {
        return weatherFragment;
    }

    public Fragment getForecastFragment() {
        return forecastFragment;
    }
}
