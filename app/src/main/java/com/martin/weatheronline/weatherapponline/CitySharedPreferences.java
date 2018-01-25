package com.martin.weatheronline.weatherapponline;

import android.app.Activity;
import android.content.SharedPreferences;

public class CitySharedPreferences {
    private static final String KEY = "city";
    private static final String STANDART_CITY = "Riga";
    private SharedPreferences sharedPreferences;

    public CitySharedPreferences(Activity activity) {
        sharedPreferences = activity.getPreferences(Activity.MODE_PRIVATE);
    }

    void setCityInSP(String city) {
        sharedPreferences.edit().putString(KEY, MyCaesarCipher.encrypt(city)).apply();
    }

    String getCityFromSP() {
        String spCryptString = sharedPreferences.getString(KEY, STANDART_CITY);
        return MyCaesarCipher.decrypt(spCryptString);
    }
}
