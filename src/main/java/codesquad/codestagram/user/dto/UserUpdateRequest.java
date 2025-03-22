package codesquad.codestagram.user.dto;

public record UserUpdateRequest(
        String userId,
        String password,
        String name,
        String email
) {
}
