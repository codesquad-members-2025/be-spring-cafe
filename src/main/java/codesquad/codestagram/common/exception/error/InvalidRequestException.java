package codesquad.codestagram.common.exception.error;

import org.springframework.http.HttpStatus;

public class InvalidRequestException extends BaseException {
    public InvalidRequestException(String message) {
        super(message, HttpStatus.BAD_REQUEST);
    }
}
