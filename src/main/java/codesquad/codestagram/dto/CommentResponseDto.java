package codesquad.codestagram.dto;

public class CommentResponseDto {
    private String content;
    private String writer;
    private String date;
    private Long writerId;

    public CommentResponseDto(String content, String writer, String date, Long writerId) {
        this.content = content;
        this.writer = writer;
        this.date = date;
        this.writerId = writerId;
    }

    public String getWriter() {
        return writer;
    }

    public void setWriter(String writer) {
        this.writer = writer;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public Long getWriterId() {
        return writerId;
    }

    public void setWriterId(Long writerId) {
        this.writerId = writerId;
    }
}
