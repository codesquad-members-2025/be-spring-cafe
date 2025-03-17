package codesquad.codestagram.domain;

public class User {
    private String loginId;
    private String userName;
    private String password;

    public User(final String loginId, final String userName, final String password) {
        this.loginId = loginId;
        this.userName = userName;
        this.password = password;
    }

    public String getLoginId() {
        return loginId;
    }

    public void setLoginId(String loginId) {
        this.loginId = loginId;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }
}

