package chasingweather;

import client.WeatherForecastClient;
import com.amazonaws.services.lambda.runtime.events.APIGatewayProxyRequestEvent;
import com.amazonaws.services.lambda.runtime.events.APIGatewayProxyResponseEvent;

import com.fasterxml.jackson.databind.ObjectMapper;
import handler.WeatherForecastHandler;
import model.Location;
import model.WeatherForecast;
import org.junit.Before;
import org.junit.Test;
import org.mockito.ArgumentCaptor;
import org.mockito.Captor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;
import org.mockito.Spy;

import javax.ws.rs.core.HttpHeaders;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import java.util.HashMap;
import java.util.Map;

import static org.junit.Assert.assertEquals;

public class WeatherForecastRequestHandlerTest {

    private static final double DELTA = 0.0001;

    private static Map<String, String> QUERY_PARAMETERS = new HashMap<>();
    static {
        QUERY_PARAMETERS.put("lat", "10.2");
        QUERY_PARAMETERS.put("lon", "11.3");
    }

    private static WeatherForecast FORECAST = WeatherForecast.builder()
            .location(Location.builder().longitude(0.0).latitude(0.0).build())
            .temperature(0.0)
            .build();

    @Captor
    private ArgumentCaptor<Location> locationArgumentCaptor;

    @Mock
    private APIGatewayProxyRequestEvent input;

    @Spy
    private ObjectMapper objectMapper = new ObjectMapper();

    @Mock
    private WeatherForecastClient weatherClient;

    @InjectMocks
    private WeatherForecastHandler weatherForecastHandler;

    @Before
    public void setup() {
        MockitoAnnotations.initMocks(this);
        Mockito.when(input.getQueryStringParameters()).thenReturn(QUERY_PARAMETERS);
        Mockito.when(weatherClient.getWeatherForecastByLocation(Mockito.any(Location.class))).thenReturn(FORECAST);
    }

    @Test
    public void successfulResponse() {
        APIGatewayProxyResponseEvent result = weatherForecastHandler.handleRequest(input, null);
        assertEquals(result.getStatusCode().intValue(), Response.Status.OK.getStatusCode());
        assertEquals(result.getHeaders().get(HttpHeaders.CONTENT_TYPE), MediaType.APPLICATION_JSON);

        Mockito.verify(weatherClient).getWeatherForecastByLocation(locationArgumentCaptor.capture());
        Location capturedLocation = locationArgumentCaptor.getValue();
        assertEquals(capturedLocation.getLatitude(), 10.2, DELTA);
        assertEquals(capturedLocation.getLongitude(), 11.3, DELTA);
    }
}
