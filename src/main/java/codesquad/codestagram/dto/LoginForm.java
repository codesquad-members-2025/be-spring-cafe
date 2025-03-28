package codesquad.codestagram.dto;

import jakarta.validation.constraints.NotEmpty;

public class LoginForm {

    @NotEmpty(message = "아이디를 입력해 주세요")
    private String userId;

    @NotEmpty(message = "비밀번호를 입력해 주세요")
    private String password;

    public LoginForm(String userId, String password) {
        this.userId = userId;
        this.password = password;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

}
