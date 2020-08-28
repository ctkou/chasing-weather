package injection;

import com.fasterxml.jackson.databind.ObjectMapper;
import dagger.Module;
import dagger.Provides;

import javax.inject.Singleton;

@Module
public class WeatherForecastHandlerModule {
    @Singleton
    @Provides
    public ObjectMapper provideObjectMapper() {
        return new ObjectMapper();
    }
}
