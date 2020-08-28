package chasingweather;

import handler.WeatherForecastHandler;
import injection.DaggerWeatherForecastHandlerComponent;
import lombok.extern.slf4j.Slf4j;

import com.amazonaws.services.lambda.runtime.Context;
import com.amazonaws.services.lambda.runtime.RequestHandler;
import com.amazonaws.services.lambda.runtime.events.APIGatewayProxyRequestEvent;
import com.amazonaws.services.lambda.runtime.events.APIGatewayProxyResponseEvent;

/**
 * Handler for requests to Lambda function.
 */
@Slf4j
public class WeatherForecastRequestHandlerWrapper
        implements RequestHandler<APIGatewayProxyRequestEvent, APIGatewayProxyResponseEvent> {

    private final WeatherForecastHandler handler;

    public WeatherForecastRequestHandlerWrapper() {
        handler = DaggerWeatherForecastHandlerComponent.create().buildWeatherForecastHandler();
    }

    public APIGatewayProxyResponseEvent handleRequest(final APIGatewayProxyRequestEvent input, final Context context) {
        return handler.handleRequest(input, context);
    }
}
