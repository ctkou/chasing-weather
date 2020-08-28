package injection;

import client.OpenWeatherClient;
import client.WeatherForecastClient;
import com.fasterxml.jackson.databind.ObjectMapper;
import dagger.Module;
import dagger.Provides;

@Module
public class WeatherForecastHandlerModule {
    @Provides
    public ObjectMapper provideObjectMapper() {
        return new ObjectMapper();
    }

    @Provides
    public WeatherForecastClient provideWeatherForecastClient() {
        return new OpenWeatherClient();
    }
}
