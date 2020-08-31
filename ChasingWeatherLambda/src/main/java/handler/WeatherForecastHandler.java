package handler;

import client.WeatherForecastClient;
import com.amazonaws.services.lambda.runtime.Context;
import com.amazonaws.services.lambda.runtime.events.APIGatewayProxyRequestEvent;
import com.amazonaws.services.lambda.runtime.events.APIGatewayProxyResponseEvent;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import exception.LambdaException;
import exception.QueryParameterException;
import lombok.extern.slf4j.Slf4j;
import model.Location;
import org.apache.commons.math3.util.Precision;

import javax.inject.Inject;
import javax.ws.rs.core.HttpHeaders;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import java.util.HashMap;
import java.util.Map;

@Slf4j
public class WeatherForecastHandler {

    private static final int LOCATION_DECIMAL_PLACE = 1;

    private final ObjectMapper objectMapper;

    private final WeatherForecastClient weatherClient;

    @Inject
    public WeatherForecastHandler(ObjectMapper objectMapper, WeatherForecastClient weatherClient) {
        this.objectMapper = objectMapper;
        this.weatherClient = weatherClient;
    }

    public APIGatewayProxyResponseEvent handleRequest(final APIGatewayProxyRequestEvent input, final Context context) {
        APIGatewayProxyResponseEvent response = initializeAPIGatewayProxyResponse();
        try {
            Location location = getLocation(input);
            return response
                    .withStatusCode(Response.Status.OK.getStatusCode())
                    .withBody(objectMapper.writeValueAsString(weatherClient.getWeatherForecastByLocation(location)));
        } catch (LambdaException e) {
            log.error(e.getMessage(), e);
            return response
                    .withStatusCode(Response.Status.INTERNAL_SERVER_ERROR.getStatusCode())
                    .withBody(e.getErrorMessageJsonString());
        } catch (Exception e) {
            log.error(e.getMessage(), e);
            return response
                    .withStatusCode(Response.Status.INTERNAL_SERVER_ERROR.getStatusCode())
                    .withBody("{\"message\":\"Unexpected error.\"}");
        }
    }

    private Location getLocation(APIGatewayProxyRequestEvent requestEvent) {
        if (requestEvent.getQueryStringParameters() == null || requestEvent.getQueryStringParameters().isEmpty()) {
            throw new QueryParameterException();
        }
        double latitude = Double.parseDouble(requestEvent.getQueryStringParameters().get("lat"));
        double longitude = Double.parseDouble(requestEvent.getQueryStringParameters().get("lon"));
        return Location.builder()
                .latitude(Precision.round(latitude, LOCATION_DECIMAL_PLACE))
                .longitude(Precision.round(longitude, LOCATION_DECIMAL_PLACE)).build();
    }

    private APIGatewayProxyResponseEvent initializeAPIGatewayProxyResponse() {
        Map<String, String> headers = new HashMap<>();
        headers.put(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON);
        return new APIGatewayProxyResponseEvent().withHeaders(headers);
    }

}
