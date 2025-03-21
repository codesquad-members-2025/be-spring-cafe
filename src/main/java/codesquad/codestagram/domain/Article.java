package codesquad.codestagram.domain;

import java.time.LocalDateTime;

public class Article {

    private static long sequence = 0L;

    private Long id;
    private String writer;
    private String title;
    private String contents;
    private final LocalDateTime createAt;

    public Article(String writer, String title, String contents) {
        this.id = ++sequence;
        this.writer = writer;
        this.title = title;
        this.contents = contents;
        this.createAt = LocalDateTime.now();
    }

    public Long getId() {
        return id;
    }

    public String getWriter() {
        return writer;
    }

    public void setWriter(String writer) {
        this.writer = writer;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getContents() {
        return contents;
    }

    public void setContents(String contents) {
        this.contents = contents;
    }

    public LocalDateTime getCreateAt() {
        return createAt;
    }

}
