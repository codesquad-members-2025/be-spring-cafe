package codesquad.codestagram.dto;

import codesquad.codestagram.domain.Reply;

public class ReplyResponseDto {
    private final String name;
    private final String createdAt;
    private final String text;
    private final Long articleId;
    private final Long replyId;

    public ReplyResponseDto(String name, String createdAt, String text, Long articleId, Long replyId) {
        this.name = name;
        this.createdAt = createdAt;
        this.text = text;
        this.articleId = articleId;
        this.replyId = replyId;
    }

    public String getName() {
        return name;
    }

    public String getCreatedAt() {
        return createdAt;
    }

    public String getText() {
        return text;
    }

    public Long getArticleId() {
        return articleId;
    }

    public Long getReplyId() {
        return replyId;
    }

    public static ReplyResponseDto from(Reply reply) {
        return new ReplyResponseDto(
                reply.getUser().getName(),
                reply.getCreatedAt(),
                reply.getText(),
                reply.getArticle().getId(),
                reply.getId()
        );
    }
}
