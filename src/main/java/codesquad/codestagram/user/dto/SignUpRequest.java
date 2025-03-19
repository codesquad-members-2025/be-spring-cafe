package codesquad.codestagram.user.dto;

public record SignUpRequest(
        String userId,
        String password,
        String name,
        String email
) {
}
