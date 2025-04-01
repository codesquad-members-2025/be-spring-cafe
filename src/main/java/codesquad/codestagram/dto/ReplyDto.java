package codesquad.codestagram.dto;

import codesquad.codestagram.domain.Reply;
import java.time.LocalDateTime;

public class ReplyDto {
    public static class AddReplyResponseDto{
        private String content;
        private Long articleId;
        private String userId;
        private LocalDateTime createdAt;

        public AddReplyResponseDto(String content, Long articleId, String userId, LocalDateTime createdAt) {
            this.content = content;
            this.articleId = articleId;
            this.userId = userId;
            this.createdAt = createdAt;
        }

        public static AddReplyResponseDto ReplyToDto(Reply reply) {
            return new AddReplyResponseDto(reply.getContent(), reply.getArticle().getId(), reply.getUser().getUserId(),
                    reply.getCreatedAt());
        }

        public String getContent() {
            return content;
        }

        public Long getArticleId() {
            return articleId;
        }

        public String getUserId() {
            return userId;
        }

        public LocalDateTime getCreatedAt() {
            return createdAt;
        }
    }
}
