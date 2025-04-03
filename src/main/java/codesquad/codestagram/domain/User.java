package codesquad.codestagram.domain;

import codesquad.codestagram.dto.UserForm;
import jakarta.persistence.*;


@Entity
@Table(name = "users")
public class User {
    @Id  @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String loginId;
    private String name;
    private String password;
    private String email; //프로필 페이지에서 필요

    protected User() {
    }

    public User(String loginId, String name, String password, String email) {
        this.loginId = loginId;
        this.name = name;
        this.password = password;
        this.email = email;

    }

    public static User form(UserForm form) {
        return new User(form.getLoginId(), form.getName(), form.getPassword(), form.getEmail());
    }

    public void updateform(UserForm.UpdateUser update) {
        this.password = update.getNewPassword();
        this.name = update.getName();
        this.email = update.getEmail();
    }


    public Long getId() {
        return id;
    }

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
}
