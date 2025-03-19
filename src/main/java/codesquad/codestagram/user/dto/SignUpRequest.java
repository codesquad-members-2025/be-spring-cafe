package codesquad.codestagram.user.dto;

public record SignUpRequest(
        String id,
        String password,
        String name,
        String email
) {
}
