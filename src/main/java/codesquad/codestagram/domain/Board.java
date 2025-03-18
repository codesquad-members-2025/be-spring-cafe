package codesquad.codestagram.domain;

import java.time.LocalDateTime;
import java.util.ArrayList;

public class Board {
    private Long id;
    private String title;
    private String content;
    private String writer;
    private LocalDateTime uploadDate;
    private ArrayList<Comment> comments;

    public Board(Long id, String title, String content, String writer) {
        this.id = id;
        this.title = title;
        this.content = content;
        this.writer = writer;
        this.uploadDate = LocalDateTime.now();
        this.comments = new ArrayList<>();
    }

    public Long getId() { return id; }
    public String getTitle() { return title; }
    public void setTitle(String title) { this.title = title; }
    public String getContent() { return content; }
    public void setContent(String content) { this.content = content; }
    public String getWriter() { return writer; }
    public LocalDateTime getUploadDate() { return uploadDate; }
    public ArrayList<Comment> getComments() { return comments; }
}