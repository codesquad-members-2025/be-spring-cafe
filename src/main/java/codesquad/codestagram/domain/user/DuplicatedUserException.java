package codesquad.codestagram.domain.user;

public class DuplicatedUserException extends RuntimeException {
    public DuplicatedUserException(String message) {
        super(message);
    }
}

