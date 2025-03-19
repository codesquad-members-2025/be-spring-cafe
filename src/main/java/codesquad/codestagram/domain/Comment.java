package codesquad.codestagram.domain;

import java.time.LocalDateTime;

public class Comment {
    private static int commentCounter = 1;
    private int id;
    private String writer;
    private String content;
    private LocalDateTime uploadDate;

    public Comment(String writer, String content) {
        this.id = commentCounter++;
        this.writer = writer;
        this.content = content;
        this.uploadDate = LocalDateTime.now();
    }

    public int getId() { return id; }
    public String getWriter() { return writer; }
    public String getContent() { return content; }
    public LocalDateTime getUploadDate() { return uploadDate; }
}
