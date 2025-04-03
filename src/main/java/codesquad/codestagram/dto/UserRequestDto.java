package codesquad.codestagram.dto;

import codesquad.codestagram.entity.User;

public class UserRequestDto {
    private String userid;
    private String password;
    private String email;
    private String name;

    public UserRequestDto() {}
    public UserRequestDto(String userid, String email, String name, String password) {
        this.userid = userid;
        this.email = email;
        this.name = name;
        this.password = password;
    }

    public String getUserid() {
        return userid;
    }
    public String getPassword() {
        return password;
    }
    public String getEmail() {
        return email;
    }
    public String getName(){
        return name;
    }

    public void setUserid(String userid) {
        this.userid = userid;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public void setName(String name) {
        this.name = name;
    }

    public User toEntity(){
        return new User(userid,password,email,name);
    }
}
