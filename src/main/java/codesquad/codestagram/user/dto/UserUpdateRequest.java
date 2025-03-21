package codesquad.codestagram.user.dto;

public record UserUpdateRequest(
        String password,
        String name,
        String email
) {
}
