package codesquad.codestagram.common.exception.error;

import org.springframework.http.HttpStatus;

public class DuplicateResourceException extends BaseException {
    public DuplicateResourceException(String message) {
        super(message, HttpStatus.CONFLICT);
    }
}
