package codesquad.codestagram.entity;


public class User {

    private static Long idCounter = 0L; // 아이디 자동 증가 (DB 없이 관리)

    private Long id;
    private String name;
    private String email;

    // 기본 생성자
    public User() {
    }

    // 생성자
    public User(String name, String email) {
        this.id = ++idCounter; // ID 자동 증가
        this.name = name;
        this.email = email;
    }

    // Getter
    public Long getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public String getEmail() {
        return email;
    }

    // Setter
    public void setId(Long id) {
        this.id = id;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setEmail(String email) {
        this.email = email;
    }
}
