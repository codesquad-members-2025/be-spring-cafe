package codesquad.codestagram.exception;

public class NotLoggedInException extends RuntimeException {
    public NotLoggedInException() {
        super("로그인 해야합니다.");
    }

    public NotLoggedInException(String message) {
        super(message);
    }
}
