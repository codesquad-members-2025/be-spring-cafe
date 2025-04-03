package codesquad.codestagram.exception;

public class UserNotFoundException extends RuntimeException {
    public UserNotFoundException(String userId) {
        super("해당 사용자를 찾을 수 없습니다: " + userId);
    }
}
