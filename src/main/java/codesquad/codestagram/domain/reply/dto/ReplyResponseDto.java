package codesquad.codestagram.domain.reply.dto;

import codesquad.codestagram.domain.reply.Reply;

import java.time.LocalDateTime;

public record ReplyResponseDto(
        Long id,
        String content,
        String writerName,
        LocalDateTime createdAt) {
    public static ReplyResponseDto of(Reply reply) {
        return new ReplyResponseDto(
                reply.getId(),
                reply.getContent(),
                reply.getUser().getName(),
                reply.getCreatedAt()
        );
    }
}
