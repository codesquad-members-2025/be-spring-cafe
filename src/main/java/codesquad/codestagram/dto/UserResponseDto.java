package codesquad.codestagram.dto;

public class UserResponseDto {
    private Long userSeq;
    private String id;
    private String name;
    private String email;

    public UserResponseDto(Long userSeq, String id, String name, String email) {
        this.userSeq = userSeq;
        this.id = id;
        this.name = name;
        this.email = email;
    }

    public String getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public String getEmail() {
        return email;
    }
}
