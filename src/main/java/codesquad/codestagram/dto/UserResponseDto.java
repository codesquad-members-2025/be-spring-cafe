package codesquad.codestagram.dto;

public class UserResponseDto {
    private Long id;
    private String userid;
    private String email;
    private String name;

    public UserResponseDto(Long id, String userid, String email, String name) {
        this.id = id;
        this.userid = userid;
        this.email = email;
        this.name = name;
    }

    public Long getId() {
        return id;
    }

    public String getUserid() {
        return userid;
    }

    public String getEmail() {
        return email;
    }

    public String getName() {
        return name;
    }
}
