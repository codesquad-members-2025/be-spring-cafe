package codesquad.codestagram.exception;

public class UnauthorizedAccessException extends RuntimeException {
    public UnauthorizedAccessException() {
        super("허용되지 않은 접근입니다.");
    }

    public UnauthorizedAccessException(String message) {
        super(message);
    }
}
