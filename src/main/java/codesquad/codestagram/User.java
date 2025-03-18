package codesquad.codestagram;

public class User {
    private static int idCounter = 1;
    private int id;
    private String userId;
    private String password;
    private String name;
    private String email;

    public User(String userId, String password, String name, String email) {
        id = idCounter++;
        this.userId = userId;
        this.password = password;
        this.name = name;
        this.email = email;
    }

    public int getId() {
        return id;
    }

    public String getUserId() {
        return userId;
    }

    public String getPassword() {
        return password;
    }

    public String getName() {
        return name;
    }

    public String getEmail() {
        return email;
    }

}
