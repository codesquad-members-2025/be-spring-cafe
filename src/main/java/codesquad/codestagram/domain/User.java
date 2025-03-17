package codesquad.codestagram.domain;

public class User {
    private String loginId;
    private String name;
    private String password;

    public User(final String loginId, final String name, final String password) {
        this.loginId = loginId;
        this.name = name;
        this.password = password;
    }

    public String getLoginId() {
        return loginId;
    }

    public void setLoginId(String loginId) {
        this.loginId = loginId;
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
}

