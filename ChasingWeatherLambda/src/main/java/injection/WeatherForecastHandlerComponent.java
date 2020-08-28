package injection;

import dagger.Component;
import handler.WeatherForecastHandler;

@Component(modules = WeatherForecastHandlerModule.class)
public interface WeatherForecastHandlerComponent {
    WeatherForecastHandler buildWeatherForecastHandler();
}
