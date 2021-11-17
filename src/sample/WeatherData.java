package sample;

import java.util.Date;

public class WeatherData {

    StringBuilder result;
    float lon;
    float lat;
    String weather, location;
    float temp, feels_like, temp_min, temp_max, pressure, humidity;
    float speed, deg, gust;
    Date date;


    public WeatherData(StringBuilder result, float lon, float lat, String weather, float temp, float feels_like, float temp_min, float temp_max, float pressure, float humidity, float speed, String location, Date date) {
        this.result = result;
        this.lon = lon;
        this.lat = lat;
        this.weather = weather;
        this.location = location;
        this.temp = temp;
        this.feels_like = feels_like;
        this.temp_min = temp_min;
        this.temp_max = temp_max;
        this.pressure = pressure;
        this.humidity = humidity;
        this.speed = speed;
        this.deg = deg;
        this.gust = gust;
        this.date=date;

    }


    public void everything() {
        System.out.println("Location: " + this.location);
        System.out.println("Date: " + this.date);
        System.out.println("Lon: " + this.lon + " Lat: " + this.lat);
        System.out.println("Temperature is: " + this.temp + " °C, feels like: " + this.feels_like + " °C," + " Weather is: " + this.weather);
        System.out.println("Pressure: " + this.pressure + ", Humidity: " + this.humidity);
    }
}
