package codesquad.codestagram.common.exception.error;

import org.springframework.http.HttpStatus;

public class BaseException extends RuntimeException {
    private final HttpStatus httpStatus;

    protected BaseException(String message, HttpStatus httpStatus) {
        super(message);
        this.httpStatus = httpStatus;
    }

    public HttpStatus getHttpStatus() {
        return httpStatus;
    }
}
