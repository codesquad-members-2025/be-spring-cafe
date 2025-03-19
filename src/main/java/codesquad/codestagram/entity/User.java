package codesquad.codestagram.entity;


import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotEmpty;

public class User {

    private static Long idCounter = 0L; // 아이디 자동 증가 (DB 없이 관리)

    private Long id;
    @NotEmpty
    private String loginId;
    @NotEmpty
    private String name;
    @Email
    private String email;
    @NotEmpty
    private String password;

    // 기본 생성자
    public User() {
    }

    // 생성자
    public User(String name, String email,String loginId, String password) {
        this.id = ++idCounter; // ID 자동 증가
        this.name = name;
        this.email = email;
        this.loginId = loginId;
        this.password = password;
    }

    // Getter
    public Long getId() {
        return id;
    }

    // Setter
    public void setId(Long id) {
        this.id = id;
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

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }
}
