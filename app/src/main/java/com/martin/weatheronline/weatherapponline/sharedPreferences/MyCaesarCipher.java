package com.martin.weatheronline.weatherapponline.sharedPreferences;

public class MyCaesarCipher {

    private static final int CRYPT_STEPS = 3;

    public static String encrypt(String word) {
        char[] wordCharArr = word.toCharArray();
        for (int i = 0; i < wordCharArr.length; i++) {
            wordCharArr[i] = (char) ((int) wordCharArr[i] - CRYPT_STEPS);
        }
        word = new String(wordCharArr);
        return word;
    }


    public static String decrypt(String word) {
        char[] wordCharArr = word.toCharArray();
        for (int i = 0; i < wordCharArr.length; i++) {
            wordCharArr[i] = (char) ((int) wordCharArr[i] + CRYPT_STEPS);
        }
        word = new String(wordCharArr);
        return word;
    }
}

