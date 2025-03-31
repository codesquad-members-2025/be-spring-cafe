package codesquad.codestagram.dto;

import codesquad.codestagram.domain.Reply;
import codesquad.codestagram.domain.User;
import codesquad.codestagram.utility.TextUtility;

public class ReplyViewDto {
    private Long id;
    private String userId;
    private String userName;
    private String text;
    private String createdAt;

    protected ReplyViewDto() {}

    public ReplyViewDto(Reply reply) {
        User user = reply.getUser();
        this.id = reply.getId();
        this.userId = user.getUserId();
        this.userName = user.getName();
        this.text = TextUtility.escapeAndConvertNewlines(reply.getText());
        this.createdAt = reply.getCreatedAt();
    }

    public ReplyViewDto(Long id, String userId, String userName, String text, String createdAt) {
        this.id = id;
        this.userId = userId;
        this.userName = userName;
        this.text = TextUtility.escapeAndConvertNewlines(text);
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
