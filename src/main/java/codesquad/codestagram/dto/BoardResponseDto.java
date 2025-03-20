package codesquad.codestagram.dto;

import codesquad.codestagram.Entity.Board;
import java.time.format.DateTimeFormatter;

public class BoardResponseDto {
    private Long id;
    private String title;
    private String content;
    private String writerId;
    private String writerName;
    private String uploadDate;
    private static final DateTimeFormatter FORMATTER = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm");

    public BoardResponseDto(Board board) {
        this.id = board.getId();
        this.title = board.getTitle();
        this.content = board.getContent();
        this.writerId = board.getUser().getId();
        this.writerName = board.getUser().getName();
        this.uploadDate = board.getUploadDate().format(FORMATTER);
    }

    public Long getId() { return id; }
    public String getTitle() { return title; }
    public String getContent() { return content; }
    public String getWriterId() { return writerId; }
    public String getWriterName() { return writerName; }
    public String getUploadDate() { return uploadDate; }
}
