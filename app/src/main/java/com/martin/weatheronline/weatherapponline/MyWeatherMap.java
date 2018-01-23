package com.martin.weatheronline.weatherapponline;

import com.google.gson.annotations.SerializedName;

import java.util.List;


public class MyWeatherMap {

    @SerializedName("coord")
    public Coord coord;

    @SerializedName("weather")
    public List<Weather> weather = null;

    @SerializedName("base")
    public String base;
    @SerializedName("main")
    public MainInfo mainInfo;
    @SerializedName("visibility")
    public Integer visibility;
    @SerializedName("wind")
    public Wind wind;
    @SerializedName("clouds")
    public Clouds clouds;
    @SerializedName("dt")
    public Integer dt;
    @SerializedName("sys")
    public Sys sys;
    @SerializedName("id")
    public Integer id;
    @SerializedName("name")
    public String name;
    @SerializedName("cod")
    public Integer cod;

    public String getBase() {
        return base;
    }

    public Integer getId() {
        return weather.get(0).id;
    }

    public Long getSunrise() {
        return sys.sunrise;
    }

    public Long getSunset() {
        return sys.sunset;
    }

    public String getName() {
        return name;
    }

    public MainInfo getMainInfo() {
        return mainInfo;
    }

    public Wind getWind() {
        return wind;
    }

    public class Clouds {

        @SerializedName("all")
        public Integer all;

    }

    public class Coord {

        @SerializedName("lon")
        public Float lon;

        @SerializedName("lat")
        public Float lat;

    }

    public class MainInfo {

        @SerializedName("temp")
        public Float temp;

        @SerializedName("pressure")
        public Integer pressure;

        @SerializedName("humidity")
        public Integer humidity;

        @SerializedName("temp_min")
        public Integer tempMin;

        @SerializedName("temp_max")
        public Integer tempMax;

        public Float getTemp() {
            return temp;
        }

        public Integer getPressure() {
            return pressure;
        }

        public Integer getHumidity() {
            return humidity;
        }

        public Integer getTempMin() {
            return tempMin;
        }

        public Integer getTempMax() {
            return tempMax;
        }
    }

    public class Sys {

        @SerializedName("type")
        public Integer type;

        @SerializedName("id")
        public Integer id;

        @SerializedName("message")
        public Float message;

        @SerializedName("country")
        public String country;

        @SerializedName("sunrise")
        public Long sunrise;

        @SerializedName("sunset")
        public Long sunset;

    }

    public class Weather {

        @SerializedName("id")
        public Integer id;

        @SerializedName("mainInfo")
        public String main;

        @SerializedName("description")
        public String description;

        @SerializedName("icon")
        public String icon;

    }

    public class Wind {

        @SerializedName("speed")
        public Float speed;

        @SerializedName("deg")
        public Float deg;

        public Float getSpeed() {
            return speed;
        }
    }
}