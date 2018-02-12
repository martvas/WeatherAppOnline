package com.martin.weatheronline.weatherapponline.sharedPreferences;

import android.app.Activity;
import android.content.SharedPreferences;

public class CitySharedPreferences {
    public static final String KEY = "city";
    public static final String STANDART_CITY = "Riga";
    private SharedPreferences sharedPreferences;

    public CitySharedPreferences(Activity activity) {
        sharedPreferences = activity.getPreferences(Activity.MODE_PRIVATE);
    }

    public void setCityInSP(String city) {
        sharedPreferences.edit().putString(KEY, MyCaesarCipher.encrypt(city)).apply();
    }

    public String getCityFromSP() {
        String spCryptString = sharedPreferences.getString(KEY, STANDART_CITY);
        return MyCaesarCipher.decrypt(spCryptString);
    }
}
