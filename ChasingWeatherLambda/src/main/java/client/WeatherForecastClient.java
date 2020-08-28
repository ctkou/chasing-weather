package client;

import model.Location;
import model.WeatherForecast;

public interface WeatherForecastClient {
    WeatherForecast getWeatherForecastByLocation(Location location);
}
