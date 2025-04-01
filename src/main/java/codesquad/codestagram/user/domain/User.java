package codesquad.codestagram.user.domain;

import jakarta.persistence.*;

@Entity
@Table(name = "member")
public class User {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String userId;
    private String password;
    private String name;
    private String email;



    public User(String userId, String password, String name, String email) {
        this.userId = userId;
        this.password = password;
        this.name = name;
        this.email = email;
    }

    public User() {

    }

    public Long getId() {
        return id;
    }

    public String getUserId() {
        return userId;
    }

    public String getName() {
        return name;
    }

    public String getEmail() {
        return email;
    }

    public String getPassword() {
        return password;
    }

    public void updateUser(String newPassword, String newName, String newEmail) {
        this.password = newPassword;
        this.name = newName;
        this.email = newEmail;
    }

}
