package codesquad.codestagram.domain.reply.dto;

import codesquad.codestagram.domain.reply.Reply;

public record ReplyResponseDto(
        Long id,
        String content,
        String writerName) {
    public static ReplyResponseDto of(Reply reply) {
        return new ReplyResponseDto(
                reply.getId(),
                reply.getContent(),
                reply.getUser().getName()
        );
    }
}
