package chasingweather;

import com.amazonaws.services.lambda.runtime.events.APIGatewayProxyRequestEvent;
import com.amazonaws.services.lambda.runtime.events.APIGatewayProxyResponseEvent;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;

import javax.ws.rs.core.HttpHeaders;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import java.util.HashMap;
import java.util.Map;

import static org.junit.Assert.assertEquals;

public class WeatherForecastHandlerTest {

    private static ObjectMapper MAPPER = new ObjectMapper();
    private static Map<String, String> QUERY_PARAMETERS = new HashMap<>();
    static {
        QUERY_PARAMETERS.put("lat", "10.2");
        QUERY_PARAMETERS.put("lon", "11.3");
    }

    @Mock
    private APIGatewayProxyRequestEvent input;

    @Before
    public void setup() {
        MockitoAnnotations.initMocks(this);
        Mockito.when(input.getQueryStringParameters()).thenReturn(QUERY_PARAMETERS);
    }

    @Test
    public void successfulResponse() throws JsonProcessingException {
        WeatherForecastHandler weatherForecastHandler = new WeatherForecastHandler();
        APIGatewayProxyResponseEvent result = weatherForecastHandler.handleRequest(input, null);
        assertEquals(result.getStatusCode().intValue(), Response.Status.OK.getStatusCode());
        assertEquals(result.getHeaders().get(HttpHeaders.CONTENT_TYPE), MediaType.APPLICATION_JSON);
        Map content = MAPPER.readValue(result.getBody(), Map.class);
        assertEquals(content.get("latitude"), "10.2");
        assertEquals(content.get("longitude"), "11.3");
    }
}
