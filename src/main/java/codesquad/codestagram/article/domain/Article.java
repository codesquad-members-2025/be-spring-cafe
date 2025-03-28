package codesquad.codestagram.article.domain;

import jakarta.persistence.*;

@Entity
public class Article {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String writer;
    private String title;
    private String content;

    public Article(String writer, String title, String content) {
        this.writer = writer;
        this.title = title;
        this.content = content;
    }

    public Article() {

    }

    public Long getId() {
        return id;
    }

    public String getWriter() {
        return writer;
    }

    public String getTitle() {
        return title;
    }

    public String getContent() {
        return content;
    }

    public void setId(Long id) {
        this.id = id;
    }
}

