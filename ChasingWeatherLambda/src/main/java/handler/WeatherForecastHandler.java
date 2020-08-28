package handler;

import com.amazonaws.services.lambda.runtime.Context;
import com.amazonaws.services.lambda.runtime.events.APIGatewayProxyRequestEvent;
import com.amazonaws.services.lambda.runtime.events.APIGatewayProxyResponseEvent;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.extern.slf4j.Slf4j;

import javax.inject.Inject;
import javax.ws.rs.core.HttpHeaders;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import java.util.HashMap;
import java.util.Map;

@Slf4j
public class WeatherForecastHandler {

    private final ObjectMapper mapper;

    @Inject
    public WeatherForecastHandler(ObjectMapper objectMapper) {
        mapper = objectMapper;
    }

    public APIGatewayProxyResponseEvent handleRequest(final APIGatewayProxyRequestEvent input, final Context context) {
        APIGatewayProxyResponseEvent response = initializeAPIGatewayProxyResponse();

        String latitude = input.getQueryStringParameters().get("lat");
        String longitude = input.getQueryStringParameters().get("lon");

        Map<String, String> output = new HashMap<>();
        output.put("latitude", latitude);
        output.put("longitude", longitude);

        try {
            return response
                    .withStatusCode(Response.Status.OK.getStatusCode())
                    .withBody(mapper.writeValueAsString(output));
        } catch (JsonProcessingException e) {
            return response
                    .withStatusCode(Response.Status.INTERNAL_SERVER_ERROR.getStatusCode())
                    .withBody("{}");
        }
    }

    private APIGatewayProxyResponseEvent initializeAPIGatewayProxyResponse() {
        Map<String, String> headers = new HashMap<>();
        headers.put(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON);
        return new APIGatewayProxyResponseEvent().withHeaders(headers);
    }

}
