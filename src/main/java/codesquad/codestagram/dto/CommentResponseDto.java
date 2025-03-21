package codesquad.codestagram.dto;

import codesquad.codestagram.Entity.Comment;
import java.time.format.DateTimeFormatter;

public class CommentResponseDto {
    private Long id;
    private Long userId;
    private Long boardId;
    private String content;
    private UserResponseDto user;
    private String createdAt;
    private static final DateTimeFormatter FORMATTER = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm");

    public CommentResponseDto(Comment comment) {
        this.id = comment.getId();
        this.content = comment.getContent();
        this.boardId = comment.getBoard() != null ? comment.getBoard().getId() : null;
        this.userId = comment.getUser() != null ? comment.getUser().getUserSeq() : null;
        this.createdAt = comment.getCreatedAt().format(FORMATTER);

        if (comment.getUser() != null) {
            this.user = new UserResponseDto(
                    comment.getUser().getUserSeq(),
                    comment.getUser().getId(),
                    comment.getUser().getName(),
                    comment.getUser().getEmail()
            );
        } else {
            this.user = null;
        }
    }

    public Long getId() { return id; }
    public Long getUserId() { return userId; }
    public Long getBoardId() { return boardId; }
    public String getContent() { return content; }
    public UserResponseDto getUser() { return user; }
    public String getCreatedAt() { return createdAt; }
}
