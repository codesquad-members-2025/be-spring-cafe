package codesquad.codestagram.dto;

public class UserDto {

    private String name;
    private String email;

    // 기본 생성자
    public UserDto() {
    }

    // 생성자
    public UserDto(String name, String email) {
        this.name = name;
        this.email = email;
    }

    // Getter 메서드
    public String getName() {
        return name;
    }

    public String getEmail() {
        return email;
    }

    // Setter 메서드
    public void setName(String name) {
        this.name = name;
    }

    public void setEmail(String email) {
        this.email = email;
    }
}
