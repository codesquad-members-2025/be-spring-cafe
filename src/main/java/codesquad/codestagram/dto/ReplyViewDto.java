package codesquad.codestagram.dto;

public class ReplyViewDto {
    private Long id;
    private String userName;
    private String text;
    private String createdAt;

    protected ReplyViewDto() {}

    public ReplyViewDto(Long id, String userName, String text, String createdAt) {
        this.id = id;
        this.userName = userName;
        this.text = text;
        this.createdAt = createdAt;
    }

    public Long getId() {
        return id;
    }

    public String getUserName() {
        return userName;
    }

    public String getText() {
        return text;
    }

    public String getCreatedAt() {
        return createdAt;
    }
}
