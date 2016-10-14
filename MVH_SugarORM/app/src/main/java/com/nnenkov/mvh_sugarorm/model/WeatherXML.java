package com.nnenkov.mvh_sugarorm.model;

/**
 * Created by nik on 13.10.16.
 */

public class WeatherXML {

    String datetime;
    String pressure;
    String temperature;
    String humidity;
    String cloudcover;
    String windspeed;
    String windgust;
    String winddir;
    String precipitation;

    public WeatherXML() {
    }

    public WeatherXML(String datetime, String pressure, String temperature, String humidity, String cloudcover, String windspeed, String windgust, String winddir, String precipitation) {
        this.datetime = datetime;
        this.pressure = pressure;
        this.temperature = temperature;
        this.humidity = humidity;
        this.cloudcover = cloudcover;
        this.windspeed = windspeed;
        this.windgust = windgust;
        this.winddir = winddir;
        this.precipitation = precipitation;
    }

    public String getDatetime() {
        return datetime;
    }

    public void setDatetime(String datetime) {
        this.datetime = datetime;
    }

    public String getPressure() {
        return pressure;
    }

    public void setPressure(String pressure) {
        this.pressure = pressure;
    }

    public String getTemperature() {
        return temperature;
    }

    public void setTemperature(String temperature) {
        this.temperature = temperature;
    }

    public String getHumidity() {
        return humidity;
    }

    public void setHumidity(String humidity) {
        this.humidity = humidity;
    }

    public String getCloudcover() {
        return cloudcover;
    }

    public void setCloudcover(String cloudcover) {
        this.cloudcover = cloudcover;
    }

    public String getWindspeed() {
        return windspeed;
    }

    public void setWindspeed(String windspeed) {
        this.windspeed = windspeed;
    }

    public String getWindgust() {
        return windgust;
    }

    public void setWindgust(String windgust) {
        this.windgust = windgust;
    }

    public String getWinddir() {
        return winddir;
    }

    public void setWinddir(String winddir) {
        this.winddir = winddir;
    }

    public String getPrecipitation() {
        return precipitation;
    }

    public void setPrecipitation(String precipitation) {
        this.precipitation = precipitation;
    }
}
