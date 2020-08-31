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
import static org.junit.Assert.assertTrue;

public class WeatherForecastRequestHandlerTest {

    private static final double DELTA = 0.0001;

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
        Mockito.when(weatherClient.getWeatherForecastByLocation(Mockito.any(Location.class))).thenReturn(FORECAST);
    }

    @Test
    public void testLatitudeAndLongitudeRoundingDownToDecimalPlaceOne() {
        Map<String, String> queryParameters = new HashMap<>();
        queryParameters.put("lat", "10.24");
        queryParameters.put("lon", "11.35");
        Mockito.when(input.getQueryStringParameters()).thenReturn(queryParameters);

        APIGatewayProxyResponseEvent result = weatherForecastHandler.handleRequest(input, null);
        assertEquals(result.getStatusCode().intValue(), Response.Status.OK.getStatusCode());
        assertEquals(result.getHeaders().get(HttpHeaders.CONTENT_TYPE), MediaType.APPLICATION_JSON);

        // verify that query to weather forecast client is rounded down to decimal place one
        Mockito.verify(weatherClient).getWeatherForecastByLocation(locationArgumentCaptor.capture());
        Location capturedLocation = locationArgumentCaptor.getValue();
        assertEquals(10.2, capturedLocation.getLatitude(), DELTA);
        assertEquals(11.4, capturedLocation.getLongitude(), DELTA);
    }

    @Test
    public void testLatitudeAndLongitudeRoundingUpToDecimalPlaceOne() {
        Map<String, String> queryParameters = new HashMap<>();
        queryParameters.put("lat", "10");
        queryParameters.put("lon", "11");
        Mockito.when(input.getQueryStringParameters()).thenReturn(queryParameters);

        APIGatewayProxyResponseEvent result = weatherForecastHandler.handleRequest(input, null);
        assertEquals(result.getStatusCode().intValue(), Response.Status.OK.getStatusCode());
        assertEquals(result.getHeaders().get(HttpHeaders.CONTENT_TYPE), MediaType.APPLICATION_JSON);

        // verify that query to weather forecast client is rounded up to decimal place one
        Mockito.verify(weatherClient).getWeatherForecastByLocation(locationArgumentCaptor.capture());
        Location capturedLocation = locationArgumentCaptor.getValue();
        assertEquals(10.0, capturedLocation.getLatitude(), DELTA);
        assertEquals(11.0, capturedLocation.getLongitude(), DELTA);
    }

    @Test
    public void testLatitudeAndLongitudeQueryParametersMissing() {
        final String EXPECTED_ERROR_MESSAGE = "Expected query parameters, lat (double), and lon (double).";
        Mockito.when(input.getQueryStringParameters()).thenReturn(null);

        APIGatewayProxyResponseEvent result = weatherForecastHandler.handleRequest(input, null);
        assertEquals(result.getStatusCode().intValue(), Response.Status.INTERNAL_SERVER_ERROR.getStatusCode());
        assertTrue(result.getBody().contains(EXPECTED_ERROR_MESSAGE));
    }
}
