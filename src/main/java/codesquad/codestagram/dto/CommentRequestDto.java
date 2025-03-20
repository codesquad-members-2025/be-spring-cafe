package codesquad.codestagram.dto;

public class CommentRequestDto {
    private Long userId;
    private Long boardId;
    private String content;

    public CommentRequestDto() {}
    public CommentRequestDto(Long userId, Long boardId, String content) {
        this.userId = userId;
        this.boardId = boardId;
        this.content = content;
    }

    public Long getUserId() { return userId; }
    public void setUserId(Long userId) { this.userId = userId; }
    public Long getBoardId() { return boardId; }
    public void setBoardId(Long boardId) { this.boardId = boardId; }
    public String getContent() { return content; }
    public void setContent(String content) { this.content = content; }
}
