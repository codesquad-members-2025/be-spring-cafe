package codesquad.codestagram.user.dto;

public record LoginRequest(
        String userId,
        String password
) {
}
