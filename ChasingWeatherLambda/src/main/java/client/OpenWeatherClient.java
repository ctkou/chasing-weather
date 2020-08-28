package client;

import model.Location;
import model.WeatherForecast;

public class OpenWeatherClient implements WeatherForecastClient {
    @Override
    public WeatherForecast getWeatherForecastByLocation(Location location) {
        // TODO: implement open weather api client
        return WeatherForecast.builder()
                .temperature(0.0)
                .location(Location.builder().latitude(0.0).longitude(0.0).build())
                .build();
    }
}
