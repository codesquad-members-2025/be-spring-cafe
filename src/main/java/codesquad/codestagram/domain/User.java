package codesquad.codestagram.domain;

import codesquad.codestagram.dto.UserForm;
import codesquad.codestagram.exception.InvalidPasswordException;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;

import java.util.Objects;

@Entity
@Table(name = "users")
public class User {
    @Id
    private String userId;
    private String name;
    private String password;
    private String email;

    protected User() {}

    public User(String userId, String name, String password, String email) {
        this.userId = userId;
        this.name = name;
        this.password = password;
        this.email = email;
    }

    public String getEmail() {
        return email;
    }

    public String getName() {
        return name;
    }

    public String getUserId() {
        return userId;
    }

    public boolean isPasswordValid(String password) {
        return this.password.equals(password);
    }

    public void updateIfPasswordValid(String password,String changedPassword, String name, String email) {
        if(!isPasswordValid(password)){
            throw new InvalidPasswordException();
        }
        this.name = name;
        this.email = email;
        this.password = changedPassword;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) return true;
        if (obj == null || getClass() != obj.getClass()) return false;
        User user = (User) obj;
        return Objects.equals(userId, user.userId);
    }

    @Override
    public int hashCode() {
        return Objects.hash(userId);
    }
}

