package codesquad.codestagram.dto;

import codesquad.codestagram.entity.User;

public class UserResponseDto {
    private Long id;
    private String userid;
    private String email;
    private String name;

    public UserResponseDto(User user) {
        this.id = user.getId();
        this.userid = user.getUserid();
        this.email = user.getEmail();
        this.name = user.getName();
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
