package co.com.wolox.WChallenge.exceptions;

import org.springframework.http.HttpStatus;
import org.springframework.web.client.HttpClientErrorException;

public class ApiException extends HttpClientErrorException {
    private String message;

    public ApiException(HttpStatus status, String message) {
        super(status);
        this.message = message;
    }

    public static class NotFoundException extends ApiException{
        public NotFoundException(String message) {
            super(HttpStatus.NOT_FOUND, message);
        }
    }

    @Override
    public String getMessage(){
        return message;
    }
}
