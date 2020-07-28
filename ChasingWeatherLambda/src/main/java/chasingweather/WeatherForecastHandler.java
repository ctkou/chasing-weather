package chasingweather;

import java.util.HashMap;
import java.util.Map;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.extern.slf4j.Slf4j;

import com.amazonaws.services.lambda.runtime.Context;
import com.amazonaws.services.lambda.runtime.RequestHandler;
import com.amazonaws.services.lambda.runtime.events.APIGatewayProxyRequestEvent;
import com.amazonaws.services.lambda.runtime.events.APIGatewayProxyResponseEvent;

import javax.ws.rs.core.HttpHeaders;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

/**
 * Handler for requests to Lambda function.
 */
@Slf4j
public class WeatherForecastHandler implements RequestHandler<APIGatewayProxyRequestEvent, APIGatewayProxyResponseEvent> {

    private static final ObjectMapper MAPPER = new ObjectMapper();

    public APIGatewayProxyResponseEvent handleRequest(final APIGatewayProxyRequestEvent input, final Context context) {
        Map<String, String> headers = new HashMap<>();
        headers.put(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON);

        APIGatewayProxyResponseEvent response = new APIGatewayProxyResponseEvent()
                .withHeaders(headers);

        String latitude = input.getQueryStringParameters().get("lat");
        String longitude = input.getQueryStringParameters().get("lon");

        Map<String, String> output = new HashMap<>();
        output.put("latitude", latitude);
        output.put("longitude", longitude);

        try {
            return response
                    .withStatusCode(Response.Status.OK.getStatusCode())
                    .withBody(MAPPER.writeValueAsString(output));
        } catch (JsonProcessingException e) {
            return response
                    .withStatusCode(Response.Status.INTERNAL_SERVER_ERROR.getStatusCode())
                    .withBody("{}");
        }
    }

}
