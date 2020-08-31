package exception;

public class QueryParameterException extends LambdaException {
    private static final String ERROR_MESSAGE = "Expected query parameters, lat (double), and lon (double).";

    public QueryParameterException() {
        super(ERROR_MESSAGE);
    }
}
