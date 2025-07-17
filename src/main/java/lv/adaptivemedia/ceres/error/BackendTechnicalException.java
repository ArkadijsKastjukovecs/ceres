package lv.adaptivemedia.ceres.error;

import org.springframework.http.HttpStatus;

public class BackendTechnicalException extends RuntimeException {

    private final HttpStatus statusCode;

    public BackendTechnicalException(String message, HttpStatus statusCode) {
        super(message);
        this.statusCode = statusCode;
    }

    public BackendTechnicalException(String message, HttpStatus statusCode, Throwable cause) {
        super(message, cause);
        this.statusCode = statusCode;
    }

    public HttpStatus getStatusCode() {
        return statusCode;
    }
}