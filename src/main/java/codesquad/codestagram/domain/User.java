package codesquad.codestagram.domain;

public class User {
    private Long userSeq;
    private String id;
    private String name;
    private String password;
    private String email;

    public User(Long userSeq, String id, String name, String password, String email) {
        this.userSeq = userSeq;
        this.id = id;
        this.name = name;
        this.email = email;
        this.password = password;
    }

    public Long getUserSeq() { return userSeq; }

    public String getId() {
        return id;
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
}
