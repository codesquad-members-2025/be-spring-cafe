package codesquad.codestagram.exception;

public class UserNotAuthorException extends RuntimeException {
    public UserNotAuthorException() {
        super("작성자만 수행할 수 있는 작업입니다.");
    }
}
