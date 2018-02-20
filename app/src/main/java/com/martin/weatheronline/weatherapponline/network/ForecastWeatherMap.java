package com.martin.weatheronline.weatherapponline.network;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;


public class ForecastWeatherMap {

    @SerializedName("cod")
    @Expose
    private Integer cod;
    @SerializedName("message")
    @Expose
    private Float message;
    @SerializedName("cnt")
    @Expose
    private Integer cnt;
    @SerializedName("list")
    @Expose
    private List<Forecast> list = null;
    @SerializedName("city")
    @Expose
    private City city;

    public Integer getCod() {
        return cod;
    }

    void setCod(Integer cod) {
        this.cod = cod;
    }

    public Float getMessage() {
        return message;
    }

    public void setMessage(Float message) {
        this.message = message;
    }

    public Integer getCnt() {
        return cnt;
    }

    public void setCnt(Integer cnt) {
        this.cnt = cnt;
    }

    public List<Forecast> getForecastList() {
        return list;
    }

    public void setList(List<Forecast> list) {
        this.list = list;
    }

    public City getCity() {
        return city;
    }

    public void setCity(City city) {
        this.city = city;
    }


    public class City {

        @SerializedName("id")
        @Expose
        private Integer id;
        @SerializedName("name")
        @Expose
        private String name;
        @SerializedName("coord")
        @Expose
        private Coord coord;
        @SerializedName("country")
        @Expose
        private String country;
        @SerializedName("population")
        @Expose
        private Integer population;

        public Integer getId() {
            return id;
        }

        public void setId(Integer id) {
            this.id = id;
        }

        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }

        public Coord getCoord() {
            return coord;
        }

        public void setCoord(Coord coord) {
            this.coord = coord;
        }

        public String getCountry() {
            return country;
        }

        public void setCountry(String country) {
            this.country = country;
        }

        public Integer getPopulation() {
            return population;
        }

        public void setPopulation(Integer population) {
            this.population = population;
        }

    }

    public class Clouds {

        @SerializedName("all")
        @Expose
        private Integer all;

        public Integer getAll() {
            return all;
        }

        public void setAll(Integer all) {
            this.all = all;
        }

    }

    public class Coord {

        @SerializedName("lat")
        @Expose
        private Float lat;
        @SerializedName("lon")
        @Expose
        private Float lon;

        public Float getLat() {
            return lat;
        }

        public void setLat(Float lat) {
            this.lat = lat;
        }

        public Float getLon() {
            return lon;
        }

        public void setLon(Float lon) {
            this.lon = lon;
        }

    }

    public class Forecast {

        @SerializedName("dt")
        @Expose
        private Long dt;
        @SerializedName("main")
        @Expose
        private MainInformation mainInformation;
        @SerializedName("weather")
        @Expose
        private List<Weather> weather = null;
        @SerializedName("clouds")
        @Expose
        private Clouds clouds;
        @SerializedName("wind")
        @Expose
        private Wind wind;
        @SerializedName("rain")
        @Expose
        private Rain rain;
        @SerializedName("sys")
        @Expose
        private Sys sys;
        @SerializedName("dt_txt")
        @Expose
        private String dtTxt;

        public Long getDt() {
            return dt;
        }

        public void setDt(Long dt) {
            this.dt = dt;
        }

        public MainInformation getMainInformation() {
            return mainInformation;
        }

        public void setMainInformation(MainInformation mainInformation) {
            this.mainInformation = mainInformation;
        }

        public List<Weather> getWeather() {
            return weather;
        }

        public void setWeather(List<Weather> weather) {
            this.weather = weather;
        }

        public Clouds getClouds() {
            return clouds;
        }

        public void setClouds(Clouds clouds) {
            this.clouds = clouds;
        }

        public Wind getWind() {
            return wind;
        }

        public void setWind(Wind wind) {
            this.wind = wind;
        }

        public Rain getRain() {
            return rain;
        }

        public void setRain(Rain rain) {
            this.rain = rain;
        }

        public Sys getSys() {
            return sys;
        }

        public void setSys(Sys sys) {
            this.sys = sys;
        }

        public String getDtTxt() {
            return dtTxt;
        }

        public void setDtTxt(String dtTxt) {
            this.dtTxt = dtTxt;
        }

    }

    public class MainInformation {

        @SerializedName("temp")
        @Expose
        private Float temp;
        @SerializedName("temp_min")
        @Expose
        private Float tempMin;
        @SerializedName("temp_max")
        @Expose
        private Float tempMax;
        @SerializedName("pressure")
        @Expose
        private Float pressure;
        @SerializedName("sea_level")
        @Expose
        private Float seaLevel;
        @SerializedName("grnd_level")
        @Expose
        private Float grndLevel;
        @SerializedName("humidity")
        @Expose
        private Integer humidity;
        @SerializedName("temp_kf")
        @Expose
        private Float tempKf;

        public Float getTemp() {
            return temp;
        }

        public void setTemp(Float temp) {
            this.temp = temp;
        }

        public Float getTempMin() {
            return tempMin;
        }

        public void setTempMin(Float tempMin) {
            this.tempMin = tempMin;
        }

        public Float getTempMax() {
            return tempMax;
        }

        public void setTempMax(Float tempMax) {
            this.tempMax = tempMax;
        }

        public Float getPressure() {
            return pressure;
        }

        public void setPressure(Float pressure) {
            this.pressure = pressure;
        }

        public Float getSeaLevel() {
            return seaLevel;
        }

        public void setSeaLevel(Float seaLevel) {
            this.seaLevel = seaLevel;
        }

        public Float getGrndLevel() {
            return grndLevel;
        }

        public void setGrndLevel(Float grndLevel) {
            this.grndLevel = grndLevel;
        }

        public Integer getHumidity() {
            return humidity;
        }

        public void setHumidity(Integer humidity) {
            this.humidity = humidity;
        }

        public Float getTempKf() {
            return tempKf;
        }

        public void setTempKf(Float tempKf) {
            this.tempKf = tempKf;
        }

    }

    public class Rain {

        @SerializedName("3h")
        @Expose
        private Float _3h;

        public Float get3h() {
            return _3h;
        }

        public void set3h(Float _3h) {
            this._3h = _3h;
        }

    }

    public class Sys {

        @SerializedName("pod")
        @Expose
        private String pod;

        public String getPod() {
            return pod;
        }

        public void setPod(String pod) {
            this.pod = pod;
        }

    }

    public class Weather {

        @SerializedName("id")
        @Expose
        private Integer id;
        @SerializedName("mainInformation")
        @Expose
        private String main;
        @SerializedName("description")
        @Expose
        private String description;
        @SerializedName("icon")
        @Expose
        private String icon;

        public Integer getId() {
            return id;
        }

        public void setId(Integer id) {
            this.id = id;
        }

        public String getMain() {
            return main;
        }

        public void setMain(String main) {
            this.main = main;
        }

        public String getDescription() {
            return description;
        }

        public void setDescription(String description) {
            this.description = description;
        }

        public String getIcon() {
            return icon;
        }

        public void setIcon(String icon) {
            this.icon = icon;
        }

    }

    public class Wind {

        @SerializedName("speed")
        @Expose
        private Float speed;
        @SerializedName("deg")
        @Expose
        private Float deg;

        public Float getSpeed() {
            return speed;
        }

        public void setSpeed(Float speed) {
            this.speed = speed;
        }

        public Float getDeg() {
            return deg;
        }

        public void setDeg(Float deg) {
            this.deg = deg;
        }

    }
}