package codesquad.codestagram.dto;

import codesquad.codestagram.domain.Comment;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;

public class BoardResponseDto {
    private Long id;
    private String title;
    private String content;
    private String writer;
    private String uploadDate;
    private ArrayList<Comment> comments;
    private static final DateTimeFormatter FORMATTER = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm");

    public BoardResponseDto(Long id, String title, String content, String writer, LocalDateTime uploadDate) {
        this.id = id;
        this.title = title;
        this.content = content;
        this.writer = writer;
        this.uploadDate = uploadDate.format(FORMATTER);
    }

    public Long getId() { return id; }
    public String getTitle() { return title; }
    public String getContent() { return content; }
    public String getWriter() { return writer; }
    public String getUploadDate() { return uploadDate; }
}
