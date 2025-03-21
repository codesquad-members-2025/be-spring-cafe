package codesquad.codestagram.domain;

public class User {

    Long id;

    String userId;

    String password;

    String name;

    String email;

    // 기본 생성자 -> 객체를 먼저 생성하고 값을 나중에 설정할 수 있도록 함
    public User() {}

    // 생성자 -> 객체를 생성하면서 동시에 값을 설정
    public User(String userId, String password, String name, String email) {
        this.userId = userId;
        this.password = password;
        this.name = name;
        this.email = email;
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

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }
}
