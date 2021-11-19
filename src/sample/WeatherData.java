package sample;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.time.format.FormatStyle;
import java.util.Date;

public class WeatherData {

    StringBuilder result;
    float lon;
    float lat;
    String weather, location;
    float temp, feels_like, pressure, humidity;
    int temp_min, temp_max;
    float speed, deg, gust;
    LocalDateTime date;


    public WeatherData(StringBuilder result, float lon, float lat, String weather, float temp, float feels_like, float temp_min, float temp_max, float pressure, float humidity, float speed, String location, LocalDateTime date) {
        this.result = result;
        this.lon = lon;
        this.lat = lat;
        this.weather = weather;
        this.location = location;
        this.temp = temp;
        this.feels_like = feels_like;
        this.temp_min = (int) temp_min;
        this.temp_max = (int) temp_max;
        this.pressure = pressure;
        this.humidity = humidity;
        this.speed = speed;
        this.deg = deg;
        this.gust = gust;
        this.date = date;

    }

    public void everything() {
        System.out.println("Location: " + this.location);
        System.out.println("Date: " + this.date.format(DateTimeFormatter.ofLocalizedDateTime(FormatStyle.MEDIUM)));
        System.out.println("Lon: " + this.lon + " Lat: " + this.lat);
        System.out.println("Temperature is: " + this.temp + " °C, feels like: " + this.feels_like + " °C," + " Weather is: " + this.weather);
        System.out.println("High of: " + this.temp_max + " And a low of: " + this.temp_min);
        System.out.println("Pressure: " + this.pressure + ", Humidity: " + this.humidity);
    }

    public float getLon() {
        return lon;
    }

    public float getLat() {
        return lat;
    }

    public String getWeather() {
        return weather;
    }

    public String getLocation() {
        return location;
    }

    public float getTemp() {
        return temp;
    }

    public float getFeels_like() {
        return feels_like;
    }

    public float getTemp_min() {
        return temp_min;
    }

    public float getTemp_max() {
        return temp_max;
    }

    public float getDeg() {
        return deg;
    }

    public LocalDateTime getDate() {
        return date;
    }
}
