package codesquad.codestagram.dto;

public class ReplyViewDto {
    private Long id;
    private String userId;
    private String userName;
    private String text;
    private String createdAt;

    protected ReplyViewDto() {}

    public ReplyViewDto(Long id, String userId, String userName, String text, String createdAt) {
        this.id = id;
        this.userId = userId;
        this.userName = userName;
        this.text = text;
        this.createdAt = createdAt;
    }

    public Long getId() {
        return id;
    }

    public String getUserId() {
        return userId;
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
