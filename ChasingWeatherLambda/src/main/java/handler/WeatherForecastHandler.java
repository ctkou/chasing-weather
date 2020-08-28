package handler;

import client.WeatherForecastClient;
import com.amazonaws.services.lambda.runtime.Context;
import com.amazonaws.services.lambda.runtime.events.APIGatewayProxyRequestEvent;
import com.amazonaws.services.lambda.runtime.events.APIGatewayProxyResponseEvent;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.extern.slf4j.Slf4j;
import model.Location;

import javax.inject.Inject;
import javax.ws.rs.core.HttpHeaders;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import java.util.HashMap;
import java.util.Map;

@Slf4j
public class WeatherForecastHandler {

    private final ObjectMapper objectMapper;

    private final WeatherForecastClient weatherClient;

    @Inject
    public WeatherForecastHandler(ObjectMapper objectMapper, WeatherForecastClient weatherClient) {
        this.objectMapper = objectMapper;
        this.weatherClient = weatherClient;
    }

    public APIGatewayProxyResponseEvent handleRequest(final APIGatewayProxyRequestEvent input, final Context context) {
        APIGatewayProxyResponseEvent response = initializeAPIGatewayProxyResponse();

        // TODO: handle missing lat, lon query parameters
        Location location = getLocation(input);

        try {
            return response
                    .withStatusCode(Response.Status.OK.getStatusCode())
                    .withBody(objectMapper.writeValueAsString(weatherClient.getWeatherForecastByLocation(location)));
        } catch (JsonProcessingException e) {
            return response
                    .withStatusCode(Response.Status.INTERNAL_SERVER_ERROR.getStatusCode())
                    .withBody("{}");
        }
    }

    private Location getLocation(APIGatewayProxyRequestEvent requestEvent) {
        double latitude = Double.parseDouble(requestEvent.getQueryStringParameters().get("lat"));
        double longitude = Double.parseDouble(requestEvent.getQueryStringParameters().get("lon"));
        return Location.builder().latitude(latitude).longitude(longitude).build();
    }

    private APIGatewayProxyResponseEvent initializeAPIGatewayProxyResponse() {
        Map<String, String> headers = new HashMap<>();
        headers.put(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON);
        return new APIGatewayProxyResponseEvent().withHeaders(headers);
    }

}
