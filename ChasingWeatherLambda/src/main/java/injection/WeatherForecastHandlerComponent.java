package injection;

import dagger.Component;
import handler.WeatherForecastHandler;

import javax.inject.Singleton;

@Singleton
@Component(modules = WeatherForecastHandlerModule.class)
public interface WeatherForecastHandlerComponent {
    WeatherForecastHandler buildWeatherForecastHandler();
}
