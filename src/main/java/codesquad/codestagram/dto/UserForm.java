package codesquad.codestagram.dto;

import codesquad.codestagram.domain.User;

public class UserForm {
    private String userId;
    private String name;
    private String password;
    private String changedPassword;
    private String email;

    protected UserForm() {
    }

    public UserForm(String userId, String name, String password, String email, String changedPassword) {
        this.userId = userId;
        this.name = name;
        this.password = password;
        this.email = email;
        this.changedPassword = changedPassword;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getChangedPassword() {
        return changedPassword;
    }
    public void setChangedPassword(String changedPassword) {
        this.changedPassword = changedPassword;
    }

    public User toEntity(){
        return new User(userId, name, password, email);
    }
}
